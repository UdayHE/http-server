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

import static httpserver.constant.Constant.*;

public class Echo implements RequestHandler {

//    @Override
//    public void handle(Request request) throws IOException {
//        String path = request.getPath();
//        OutputStream outputStream = request.getOutputStream();
//        Map<String, String> headers = request.getHeaders();
//
//        String message = path.substring(6);
//
//        String acceptEncoding = headers.getOrDefault(ACCEPT_ENCODING, EMPTY);
//        boolean useGzip = Arrays.stream(acceptEncoding.split(COMMA))
//                .map(String::trim)
//                .anyMatch(GZIP::equalsIgnoreCase);
//
//        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
//        OutputStream responseStream = useGzip ? new GZIPOutputStream(byteStream) : byteStream;
//        responseStream.write(message.getBytes(StandardCharsets.UTF_8));
//        responseStream.close();
//
//        byte[] responseBody = byteStream.toByteArray();
//
//        String responseHeaders = getResponseHeaders(useGzip, responseBody);
//        outputStream.write(responseHeaders.getBytes(StandardCharsets.UTF_8));
//        outputStream.write(responseBody);
//        outputStream.flush();
//    }
//
//    private String getResponseHeaders(boolean useGzip, byte[] responseBody) {
//        return  "HTTP/1.1 200 OK\r\n" +
//                "Content-Type: text/plain\r\n" +
//                (useGzip ? "Content-Encoding: gzip\r\n" : "") +
//                "Content-Length: " + responseBody.length + "\r\n\r\n";
//    }

    @Override
    public void handle(Request request) throws IOException {
        OutputStream outputStream = request.getOutputStream();
        try {
            String path = request.getPath();
            validatePath(path);

            String message = path.substring(6);
            boolean useGzip = isGzipAccepted(request.getHeaders());
            byte[] responseBody = processMessage(message, useGzip);

            sendResponse(outputStream, responseBody, useGzip);
        } catch (IllegalArgumentException e) {
            sendErrorResponse(outputStream, 400, "Bad Request");
        } catch (Exception e) {
            sendErrorResponse(outputStream, 500, "Internal Server Error");
        }
    }

    private void validatePath(String path) {
        if (path.length() <= 6) {
            throw new IllegalArgumentException("Invalid path");
        }
    }

    private boolean isGzipAccepted(Map<String, String> headers) {
        String acceptEncoding = headers.getOrDefault(ACCEPT_ENCODING, EMPTY);
        return acceptEncoding.toLowerCase().contains(GZIP);
    }

    private byte[] processMessage(String message, boolean useGzip) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

        if (useGzip) {
            try (GZIPOutputStream gzipStream = new GZIPOutputStream(byteStream)) {
                gzipStream.write(message.getBytes(StandardCharsets.UTF_8));
            }
        } else {
            byteStream.write(message.getBytes(StandardCharsets.UTF_8));
        }
        return byteStream.toByteArray();
    }

    private void sendResponse(OutputStream outputStream, byte[] responseBody, boolean useGzip) throws IOException {
        String responseHeaders = buildHeaders(useGzip, responseBody);
        outputStream.write(responseHeaders.getBytes(StandardCharsets.UTF_8));
        outputStream.write(responseBody);
        outputStream.flush();
    }

    private String buildHeaders(boolean useGzip, byte[] responseBody) {
        return "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/plain\r\n" +
                (useGzip ? "Content-Encoding: gzip\r\n" : "") +
                "Content-Length: " + responseBody.length + "\r\n\r\n";
    }

    private void sendErrorResponse(OutputStream outputStream, int statusCode, String statusText) throws IOException {
        String response = "HTTP/1.1 " + statusCode + " " + statusText + "\r\n" +
                "Content-Length: 0\r\n\r\n";
        outputStream.write(response.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
    }
}
