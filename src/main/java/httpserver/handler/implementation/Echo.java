package httpserver.handler.implementation;

import httpserver.dto.Request;
import httpserver.handler.RequestHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

public class Echo implements RequestHandler {

    @Override
    public void handle(Request request) throws IOException {
        String path = request.getPath();
        OutputStream outputStream = request.getOutputStream();
        Map<String, String> headers = request.getHeaders();

        String message = path.substring(6);
//        boolean useGzip = "gzip".equals(headers.get("Accept-Encoding"));
//
//        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
//        OutputStream responseStream = useGzip ? new GZIPOutputStream(byteStream) : byteStream;
//        responseStream.write(message.getBytes(StandardCharsets.UTF_8));
//        responseStream.close();
//
//        byte[] responseBody = byteStream.toByteArray();
//
//        String responseHeaders = "HTTP/1.1 200 OK\r\n" +
//                "Content-Type: text/plain\r\n" +
//                (useGzip ? "Content-Encoding: gzip\r\n" : "") +
//                "Content-Length: " + responseBody.length + "\r\n\r\n";
//
//        outputStream.write(responseHeaders.getBytes());
//        outputStream.write(responseBody, 0, responseBody.length);
//        outputStream.flush();

        String acceptEncoding = headers.getOrDefault("Accept-Encoding", "");
        boolean useGzip = Arrays.stream(acceptEncoding.split(", "))
                .map(String::trim)
                .anyMatch(enc -> "gzip".equalsIgnoreCase(enc));

        String responseHeaders = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/plain\r\n" +
                (useGzip ? "Content-Encoding: gzip\r\n" : "") +
                "Content-Length: " + message.length() + "\r\n\r\n";

        outputStream.write(responseHeaders.getBytes(StandardCharsets.UTF_8));
        outputStream.write(message.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
    }
}
