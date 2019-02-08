/* KE Software Open Source Licence
** 
** Notice: Copyright (c) 2011-2013 KE SOFTWARE PTY LTD (ACN 006 213 298)
** (the "Owner"). All rights reserved.
** 
** Licence: Permission is hereby granted, free of charge, to any person
** obtaining a copy of this software and associated documentation files
** (the "Software"), to deal with the Software without restriction,
** including without limitation the rights to use, copy, modify, merge,
** publish, distribute, sublicense, and/or sell copies of the Software,
** and to permit persons to whom the Software is furnished to do so,
** subject to the following conditions.
** 
** Conditions: The Software is licensed on condition that:
** 
** (1) Redistributions of source code must retain the above Notice,
**     these Conditions and the following Limitations.
** 
** (2) Redistributions in binary form must reproduce the above Notice,
**     these Conditions and the following Limitations in the
**     documentation and/or other materials provided with the distribution.
** 
** (3) Neither the names of the Owner, nor the names of its contributors
**     may be used to endorse or promote products derived from this
**     Software without specific prior written permission.
** 
** Limitations: Any person exercising any of the permissions in the
** relevant licence will be taken to have accepted the following as
** legally binding terms severally with the Owner and any other
** copyright owners (collectively "Participants"):
** 
** TO THE EXTENT PERMITTED BY LAW, THE SOFTWARE IS PROVIDED "AS IS",
** WITHOUT ANY REPRESENTATION, WARRANTY OR CONDITION OF ANY KIND, EXPRESS
** OR IMPLIED, INCLUDING (WITHOUT LIMITATION) AS TO MERCHANTABILITY,
** FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. TO THE EXTENT
** PERMITTED BY LAW, IN NO EVENT SHALL ANY PARTICIPANT BE LIABLE FOR ANY
** CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
** TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
** SOFTWARE OR THE USE OR OTHER DEALINGS WITH THE SOFTWARE.
** 
** WHERE BY LAW A LIABILITY (ON ANY BASIS) OF ANY PARTICIPANT IN RELATION
** TO THE SOFTWARE CANNOT BE EXCLUDED, THEN TO THE EXTENT PERMITTED BY
** LAW THAT LIABILITY IS LIMITED AT THE OPTION OF THE PARTICIPANT TO THE
** REPLACEMENT, REPAIR OR RESUPPLY OF THE RELEVANT GOODS OR SERVICES
** (INCLUDING BUT NOT LIMITED TO SOFTWARE) OR THE PAYMENT OF THE COST OF SAME.
*/
package com.kesoftware.imu;

import java.io.*;
import java.net.*;
import java.util.AbstractMap;
import java.util.ArrayList;

class Stream
{
	/* Static Properties */
	public static int
	getBlockSize()
	{
		return _blockSize;
	}

	public static void
	setBlockSize(int size)
	{
		_blockSize = size;
	}

	/* Constructor */
	public
	Stream(Socket socket) throws IMuException
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
			throw new IMuException("StreamIOSetup", e.getMessage());
		}
		
		_next = ' ';
		_token = null;
		_string = null;
		_file = null;
	}

	/* Methods */
	public Object
	get() throws IMuException
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
			throw new IMuException("StreamGet", e.getMessage());
		}
		return what;
	}

	public void
	put(Object what) throws IMuException
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
			throw new IMuException("StreamPut", e.getMessage());
		}
	}

	private static int _blockSize = 8192;

	private Socket _socket;
	
	private MixedInputStream _input;
	private MixedOutputStream _output;
	
	private char _next;
	private String _token;
	private String _string;
	private InputStream _file;

	private Object
	getValue() throws Exception
	{
		if (_token.equals("end"))
			return null;
		if (_token.equals("string"))
			return _string;
		if (_token.equals("number"))
		{
			if (_string.indexOf('.') >= 0)
				return Double.parseDouble(_string);
			return Long.parseLong(_string);
		}
		if (_token.equals("{"))
		{
			Map map = new Map();
			getToken();
			while (! _token.equals("}"))
			{
				String name = "";
				if (_token.equals("string"))
					name = _string;
				else if (_token.equals("identifier"))
					// Extension - allow simple identifiers
					name = _string;
				else
					throw new IMuException("StreamSyntaxName", _token);

				getToken();
				if (! _token.equals(":"))
					throw new IMuException("StreamSyntaxColon", _token);

				getToken();
				map.put(name, getValue());

				getToken();
				if (_token.equals(","))
					getToken();
			}
			return map;
		}
		if (_token.equals("["))
		{
			ArrayList<Object> list = new ArrayList<Object>();
			getToken();
			while (! _token.equals("]"))
			{
				list.add(getValue());

				getToken();
				if (_token.equals(","))
					getToken();
			}
			return list.toArray();
		}
		if (_token.equals("true"))
			return true;
		if (_token.equals("false"))
			return false;
		if (_token.equals("null"))
			return null;
		if (_token.equals("binary"))
			return _file;
		
		throw new IMuException("StreamSyntaxToken", _token);
	}

	private void
	getToken() throws Exception
	{
		while (Character.isWhitespace(_next))
			getNext();
		_string = "";
		_file = null;
		if (_next == '"')
		{
			_token = "string";
			getNext();
			while (_next != '"')
			{
				if (_next == '\\')
				{
					getNext();
					if (_next == 'b')
						_next = '\b';
					else if (_next == 'f')
						_next = '\f';
					else if (_next == 'n')
						_next = '\n';
					else if (_next == 'r')
						_next = '\r';
					else if (_next == 't')
						_next = '\t';
					else if (_next == 'u')
					{
						getNext();
						String str = "";
						for (int i = 0; i < 4; i++)
						{
							if (Character.isDigit(_next))
								str += _next;
							else if (Character.isLetter(_next))
							{
								char lower = Character.toLowerCase(_next);
								if (lower > 'f')
									break;
								str += lower;
							}
							else
								break;
							getNext();
						}
						if (str.length() == 0)
							throw new IMuException("StreamSyntaxUnicode");
						int num = Integer.parseInt(str, 16);
						char[] chars = Character.toChars(num);
						_next = chars[0];
					}
				}
				_string += _next;
				getNext();
			}
			getNext();
		}
		else if (Character.isDigit(_next) || _next == '-')
		{
			_token = "number";
			_string += _next;
			getNext();
			while (Character.isDigit(_next))
			{
				_string += _next;
				getNext();
			}
			if (_next == '.')
			{
				_string += _next;
				getNext();
				while (Character.isDigit(_next))
				{
					_string += _next;
					getNext();
				}
			}
			if (_next == 'e' || _next == 'E')
			{
				if (!_string.endsWith(".") && !_string.contains("."))
				{
					_string += '.';
				}
				_string += 'e';
				getNext();
				if (_next == '+')
				{
					_string += '+';
					getNext();
				}
				else if (_next == '-')
				{
					_string += '-';
					getNext();
				}
				while (Character.isDigit(_next))
				{
					_string += _next;
					getNext();
				}
			}
		}
		else if (Character.isLetter(_next) || _next == '_')
		{
			_token = "identifier";
			while (Character.isLetterOrDigit(_next) || _next == '_')
			{
				_string += _next;
				getNext();
			}
			String lower = _string.toLowerCase();
			if (lower.equals("false"))
				_token = "false";
			else if (lower.equals("null"))
				_token = "null";
			else if (lower.equals("true"))
				_token = "true";
		}
		else if (_next == '*')
		{
			// Extension - allow embedded binary data
			_token = "binary";
			getNext();
			while (Character.isDigit(_next))
			{
				_string += _next;
				getNext();
			}
			if (_string.length() == 0)
				throw new IMuException("StreamSyntaxBinary");
			long size = Long.parseLong(_string);
			while (_next != '\n')
				getNext();

			// Save data in a temporary file
			TempFile temp = new TempFile();
			OutputStream stream = temp.getOutputStream();
			byte[] data = new byte[_blockSize];
			long left = size;
			while (left > 0)
			{
				int read = _blockSize;
				if ((long) read > left)
						read = (int) left;
				int done = _input.read(data, 0, read);
				if (done <= 0)
					throw new IMuException("StreamEOF", "binary");
				stream.write(data, 0, done);
				left -= done;
			}
			_file = temp.getInputStream();
			
			getNext();
		}
		else
		{
			_token = "" + _next;
			getNext();
		}
	}

	private char
	getNext() throws Exception
	{
		int c = _input.readChar();
		if (c < 0)
			throw new IMuException("StreamEOF", "character");
		_next = (char) c;
		return _next;
	}

	@SuppressWarnings("unchecked")
	private void
	putValue(Object what, int indent) throws Exception
	{
		if (what == null)
			putData("null");
		else if (what instanceof String)
			putString((String) what);
		else if (what instanceof Integer)
			putData(((Integer) what).toString());
		else if (what instanceof Long)
			putData(((Long) what).toString());
		else if (what instanceof Double)
			putData(((Double) what).toString());
		else if (what instanceof AbstractMap)
			putObject((AbstractMap<Object,Object>) what, indent);
		else if (what instanceof Object[])
			putArray((Object[]) what, indent);
		else if (what instanceof ArrayList)
			putArray(((ArrayList<Object>) what).toArray(), indent);
		else if (what instanceof Boolean)
			putData(((Boolean) what) ? "true" : "false");
		else if (what instanceof File)
			putFile((File) what);
		else if (what instanceof InputStream)
			putStream((InputStream) what);
		else
			throw new IMuException("StreamType", what.getClass().getName());
	}

	private void
	putString(String what) throws Exception
	{
		putData('"');
		char[] chars = what.toCharArray();
		for (int i = 0; i < chars.length; i++)
		{
			if (chars[i] == '"' || chars[i] == '\\')
				putData('\\');
			putData(chars[i]);
		}
		putData('"');
	}

	private void
	putObject(AbstractMap<Object,Object> what, int indent) throws Exception
	{
		putData('{');
		putLine();
		Object[] keys = what.keySet().toArray();
		int size = keys.length;
		for (int i = 0; i < size; i++)
		{
			Object key = keys[i];
			putIndent(indent + 1);
			putString(key.toString());
			putData(" : ");
			putValue(what.get(key), indent + 1);
			if (i < size - 1)
				putData(',');
			putLine();
		}
		putIndent(indent);
		putData('}');
	}

	private void
	putArray(Object[] what, int indent) throws Exception
	{
		putData('[');
		putLine();
		for (int i = 0; i < what.length; i++)
		{
			putIndent(indent + 1);
			putValue(what[i], indent + 1);
			if (i < what.length - 1)
				putData(',');
			putLine();
		}
		putIndent(indent);
		putData(']');
	}

	private void
	putFile(File what) throws Exception
	{
		long size = what.length();
		InputStream is = new FileInputStream(what);
		putBytes(size, is);
		is.close();
	}

	private void
	putStream(InputStream what) throws Exception
	{
		/* Copy stream - to determine its size */
		TempFile temp = new TempFile();
		OutputStream os = temp.getOutputStream();
		byte[] data = new byte[_blockSize];
		for (;;)
		{
			int need = _blockSize;
			int done = what.read(data, 0, need);
			if (done <= 0)
				break;
			os.write(data, 0, done);
		}
		os.flush();

		long size = temp.length();
		InputStream is = temp.getInputStream();
		putBytes(size, is);
		is.close();
	}

	private void
	putBytes(long size, InputStream stream) throws Exception
	{
		putData('*');
		putData(String.format("%d", size));
		putLine();

		byte[] data = new byte[_blockSize];
		long left = size;
		while (left > 0)
		{
			int need = _blockSize;
			if ((long) need > left)
				need = (int) left;
			int done = stream.read(data, 0, need);
			if (done <= 0)
				break;
			_output.write(data, 0, done);
			left -= done;
		}
		if (left > 0)
		{
			/* The file did not contain enough bytes
			** so the output is padded with nulls
			*/
			data = new byte[_blockSize];
			while (left > 0)
			{
				int need = _blockSize;
				if ((long) need > left)
					need = (int) left;
				_output.write(data, 0, need);
				left -= need;
			}
		}
	}

	private void
	putIndent(int indent) throws Exception
	{
		for (int i = 0; i < indent; i++)
			putData('\t');
	}

	private void
	putLine() throws Exception
	{
		putData('\r');
		putData('\n');
	}

	private void
	putData(char chr) throws Exception
	{
		_output.write(chr);
	}

	private void
	putData(String str) throws Exception
	{
		_output.write(str);
	}
}
