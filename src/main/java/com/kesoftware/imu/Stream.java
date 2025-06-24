package com.kesoftware.imu;

import java.io.*;
import java.net.*;
import java.util.AbstractMap;
import java.util.ArrayList;

class Stream
{
    /* Static Properties */
    public static int getBlockSize()
    {
        return _blockSize;
    }

    public static void setBlockSize(int size)
    {
        _blockSize = size;
    }

    /* Constructor */
    public Stream(Socket socket) throws IMuException
    {
        _socket = socket;

        try
        {
            InputStream is = _socket.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            _input = new MixedInputStream(bis);

            OutputStream os = _socket.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(os);
            _output = new MixedOutputStream(bos);
        }
        catch (Exception e)
        {
            throw new IMuException("StreamIOSetup", e);
        }

        _next = ' ';
        _token = null;
        _string = null;
        _file = null;

        // Reusable buffer for binary I/O
        _buffer = new byte[_blockSize];
    }

    /* Methods */
    public Object get() throws IMuException
    {
        Object what = null;
        try
        {
            getNext();
            getToken();
            what = getValue();
        }
        catch (IMuException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new IMuException("StreamGet", e);
        }
        return what;
    }

    public void put(Object what) throws IMuException
    {
        try
        {
            putValue(what, 0);
            putLine();
            _output.flush();
        }
        catch (IMuException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new IMuException("StreamPut", e);
        }
    }

    private static int _blockSize = 8192;

    private final Socket _socket;

    private final MixedInputStream _input;
    private final MixedOutputStream _output;

    private char _next;
    private String _token;
    private String _string;
    private InputStream _file;

    // Reusable buffer for binary IO
    private final byte[] _buffer;

    private Object getValue() throws Exception
    {
        return switch (_token)
        {
            case "end" -> null;
            case "string" -> _string;
            case "number" -> (_string.indexOf('.') >= 0 || _string.toLowerCase().indexOf('e') >= 0)
	            ? Double.parseDouble(_string)
	            : Long.parseLong(_string);
            case "true" -> true;
            case "false" -> false;
            case "null" -> null;
            case "binary" -> _file;
            case "{" -> {
            	Map map = new Map();
                getToken();
                while (!_token.equals("}"))
                {
                    String name;
                    if (_token.equals("string") || _token.equals("identifier"))
                    {
                        name = _string;
                    }
                    else
                    {
                        throw new IMuException("StreamSyntaxName", _token);
                    }

                    getToken();
                    if (!_token.equals(":"))
                        throw new IMuException("StreamSyntaxColon", _token);

                    getToken();
                    map.put(name, getValue());

                    getToken();
                    if (_token.equals(","))
                        getToken();
                }
                yield map;
            }
            case "[" -> {
                ArrayList<Object> list = new ArrayList<>();
                getToken();
                while (!_token.equals("]"))
                {
                    list.add(getValue());
                    getToken();
                    if (_token.equals(","))
                        getToken();
                }
                yield list.toArray();
            }
            default -> throw new IMuException("StreamSyntaxToken", _token);
        };
    }

    private void getToken() throws Exception
    {
        while (Character.isWhitespace(_next))
            getNext();

        _string = null;
        _file = null;

        if (_next == '"')
        {
            _token = "string";
            StringBuilder sb = new StringBuilder();
            getNext();
            while (_next != '"')
            {
                if (_next == '\\')
                {
                    getNext();
                    switch (_next)
                    {
                        case 'b' -> sb.append('\b');
                        case 'f' -> sb.append('\f');
                        case 'n' -> sb.append('\n');
                        case 'r' -> sb.append('\r');
                        case 't' -> sb.append('\t');
                        case 'u' -> {
                            getNext();
                            StringBuilder hex = new StringBuilder();
                            for (int i = 0; i < 4; i++)
                            {
                                if (Character.isDigit(_next) ||
                                    (_next >= 'a' && _next <= 'f') ||
                                    (_next >= 'A' && _next <= 'F'))
                                {
                                    hex.append(_next);
                                    if (i < 3)
                                        getNext();
                                }
                                else
                                {
                                    break;
                                }
                            }
                            if (hex.length() == 0)
                                throw new IMuException("StreamSyntaxUnicode");
                            int codePoint = Integer.parseInt(hex.toString(), 16);
                            sb.appendCodePoint(codePoint);
                            continue; // skip getNext here as already advanced
                        }
                        default -> sb.append(_next);
                    }
                }
                else
                {
                    sb.append(_next);
                }
                getNext();
            }
            _string = sb.toString();
            getNext();
        }
        else if (Character.isDigit(_next) || _next == '-')
        {
            _token = "number";
            StringBuilder sb = new StringBuilder();
            sb.append(_next);
            getNext();
            while (Character.isDigit(_next))
            {
                sb.append(_next);
                getNext();
            }
            if (_next == '.')
            {
                sb.append(_next);
                getNext();
                while (Character.isDigit(_next))
                {
                    sb.append(_next);
                    getNext();
                }
            }
            if (_next == 'e' || _next == 'E')
            {
                if (!sb.toString().endsWith(".") && !sb.toString().contains("."))
                {
                    sb.append('.');
                }
                sb.append('e');
                getNext();
                if (_next == '+')
                {
                    sb.append('+');
                    getNext();
                }
                else if (_next == '-')
                {
                    sb.append('-');
                    getNext();
                }
                while (Character.isDigit(_next))
                {
                    sb.append(_next);
                    getNext();
                }
            }
            _string = sb.toString();
        }
        else if (Character.isLetter(_next) || _next == '_')
        {
            _token = "identifier";
            StringBuilder sb = new StringBuilder();
            while (Character.isLetterOrDigit(_next) || _next == '_')
            {
                sb.append(_next);
                getNext();
            }
            String lower = sb.toString().toLowerCase();
            if (lower.equals("false"))
                _token = "false";
            else if (lower.equals("null"))
                _token = "null";
            else if (lower.equals("true"))
                _token = "true";
            else
            {
                _string = sb.toString();
            }
        }
        else if (_next == '*')
        {
            _token = "binary";
            StringBuilder sb = new StringBuilder();
            getNext();
            while (Character.isDigit(_next))
            {
                sb.append(_next);
                getNext();
            }
            if (sb.length() == 0)
                throw new IMuException("StreamSyntaxBinary");

            long size = Long.parseLong(sb.toString());
            while (_next != '\n')
                getNext();

            TempFile temp = new TempFile();
            try (OutputStream stream = temp.getOutputStream())
            {
                long left = size;
                while (left > 0)
                {
                    int read = _buffer.length;
                    if ((long) read > left)
                        read = (int) left;
                    int done = _input.read(_buffer, 0, read);
                    if (done <= 0)
                        throw new IMuException("StreamEOF", "binary");
                    stream.write(_buffer, 0, done);
                    left -= done;
                }
            }
            _file = temp.getInputStream();

            getNext();
        }
        else
        {
            _token = Character.toString(_next);
            getNext();
        }
    }

    private char getNext() throws Exception
    {
        int c = _input.readChar();
        if (c < 0)
            throw new IMuException("StreamEOF", "character");
        _next = (char) c;
        return _next;
    }

    @SuppressWarnings("unchecked")
    private void putValue(Object what, int indent) throws Exception
    {
        if (what == null)
            putData("null");
        else if (what instanceof String s)
            putString(s);
        else if (what instanceof Integer i)
            putData(Integer.toString(i));
        else if (what instanceof Long l)
            putData(Long.toString(l));
        else if (what instanceof Double d)
            putData(Double.toString(d));
        else if (what instanceof AbstractMap<?, ?> map)
            putObject((AbstractMap<Object,Object>) map, indent);
        else if (what instanceof Object[] array)
            putArray(array, indent);
        else if (what instanceof ArrayList<?> list)
            putArray(list.toArray(), indent);
        else if (what instanceof Boolean b)
            putData(b ? "true" : "false");
        else if (what instanceof File file)
            putFile(file);
        else if (what instanceof InputStream is)
            putStream(is);
        else
            throw new IMuException("StreamType", what.getClass().getName());
    }

    private void putString(String what) throws Exception
    {
        putData('"');
        char[] chars = what.toCharArray();
        for (char c : chars)
        {
            if (c == '"' || c == '\\')
                putData('\\');
            putData(c);
        }
        putData('"');
    }

    private void putObject(AbstractMap<Object,Object> what, int indent) throws Exception
    {
        putData('{');
        putLine();
        int i = 0;
        int size = what.size();
        for (var entry : what.entrySet())
        {
            putIndent(indent + 1);
            putString(entry.getKey().toString());
            putData(" : ");
            putValue(entry.getValue(), indent + 1);
            if (i < size - 1)
                putData(',');
            putLine();
            i++;
        }
        putIndent(indent);
        putData('}');
    }

    private void putArray(Object[] what, int indent) throws Exception
    {
        putData('[');
        putLine();
        int last = what.length - 1;
        for (int i = 0; i < what.length; i++)
        {
            putIndent(indent + 1);
            putValue(what[i], indent + 1);
            if (i < last)
                putData(',');
            putLine();
        }
        putIndent(indent);
        putData(']');
    }

    private void putFile(File what) throws Exception
    {
        long size = what.length();
        try (InputStream is = new FileInputStream(what))
        {
            putBytes(size, is);
        }
    }

    private void putStream(InputStream what) throws Exception
    {
        // Copy stream to temp file to get size
        TempFile temp = new TempFile();
        try (OutputStream os = temp.getOutputStream())
        {
            int done;
            while ((done = what.read(_buffer)) > 0)
            {
                os.write(_buffer, 0, done);
            }
            os.flush();
        }
        try (InputStream is = temp.getInputStream())
        {
            putBytes(temp.length(), is);
        }
    }

    private void putBytes(long size, InputStream stream) throws Exception
    {
        putData('*');
        putData(Long.toString(size));
        putLine();

        long left = size;
        while (left > 0)
        {
            int need = _buffer.length;
            if ((long) need > left)
                need = (int) left;
            int done = stream.read(_buffer, 0, need);
            if (done <= 0)
                break;
            _output.write(_buffer, 0, done);
            left -= done;
        }
        if (left > 0)
        {
            // Pad remaining with zeros if file too short
            for (; left > 0; left -= _buffer.length)
            {
                int need = (int) Math.min(left, _buffer.length);
                _output.write(_buffer, 0, need);
            }
        }
    }

    private void putIndent(int indent) throws Exception
    {
        for (int i = 0; i < indent; i++)
            putData('\t');
    }

    private void putLine() throws Exception
    {
        putData('\r');
        putData('\n');
    }

    private void putData(char chr) throws Exception
    {
        _output.write(chr);
    }

    private void putData(String str) throws Exception
    {
        _output.write(str);
    }
}
