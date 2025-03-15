package httpserver.dto;

import java.io.BufferedReader;
import java.io.OutputStream;
import java.util.Map;

public class Request {

    private String method;
    private String path;
    private BufferedReader reader;
    private OutputStream outputStream;
    private Map<String, String> headers;

    private Request(Builder builder) {
        this.method = builder.method;
        this.path = builder.path;
        this.reader = builder.reader;
        this.outputStream = builder.outputStream;
        this.headers = builder.headers;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public BufferedReader getReader() {
        return reader;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public static class Builder {
        private String method;
        private String path;
        private BufferedReader reader;
        private OutputStream outputStream;
        private Map<String, String> headers;

        public Builder method(String method) {
            this.method = method;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder reader(BufferedReader reader) {
            this.reader = reader;
            return this;
        }

        public Builder outputStream(OutputStream outputStream) {
            this.outputStream = outputStream;
            return this;
        }

        public Builder headers(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public Request build() {
            return new Request(this);
        }
    }
}
