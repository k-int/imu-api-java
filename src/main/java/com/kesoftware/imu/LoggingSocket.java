package com.kesoftware.imu;

import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

class LoggingSocket extends Socket {
    private final Socket originalSocket;
    private final StringBuilder inputLog = new StringBuilder();
    private final StringBuilder outputLog = new StringBuilder();

    private LoggingInputStream cachedInputStream;
    private LoggingOutputStream cachedOutputStream;

    public LoggingSocket(Socket socket) {
        this.originalSocket = socket;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        if (cachedInputStream == null) {
            cachedInputStream = new LoggingInputStream(originalSocket.getInputStream());
        }
        return cachedInputStream;
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        if (cachedOutputStream == null) {
            cachedOutputStream = new LoggingOutputStream(originalSocket.getOutputStream());
        }
        return cachedOutputStream;
    }

    private class LoggingInputStream extends FilterInputStream {
        public LoggingInputStream(InputStream in) {
            super(in);
        }

        @Override
        public int read() throws IOException {
            int b = super.read();
            if (b != -1) {
                inputLog.append((char) b);
            }
            return b;
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            int bytesRead = super.read(b, off, len);
            if (bytesRead > 0) {
                inputLog.append(new String(b, off, bytesRead, "UTF-8"));
            }
            return bytesRead;
        }
    }

    private class LoggingOutputStream extends FilterOutputStream {
        public LoggingOutputStream(OutputStream out) {
            super(out);
        }

        @Override
        public void write(int b) throws IOException {
            super.write(b);
            outputLog.append((char) b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            super.write(b, off, len);
            outputLog.append(new String(b, off, len, "UTF-8"));
        }
    }

    public String getInputLog() {
        return inputLog.toString();
    }

    public String getOutputLog() {
        return outputLog.toString();
    }

    public void clearInputLog() {
        inputLog.setLength(0);
    }

    public void clearOutputLog() {
        outputLog.setLength(0);
    }

    public void clearLogs() {
        clearInputLog();
        clearOutputLog();
    }
}
