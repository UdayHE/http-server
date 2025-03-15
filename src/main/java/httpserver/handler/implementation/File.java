package httpserver.handler.implementation;

import httpserver.dto.Request;
import httpserver.enums.HttpMethod;
import httpserver.handler.RequestHandler;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static httpserver.constant.Constant.CONTENT_LENGTH;

public class File implements RequestHandler {

    private final String directory;

    public File(String directory) {
        this.directory = directory;
    }

    @Override
    public void handle(Request request) throws IOException {
        String method = request.getMethod();
        String path = request.getPath();
        OutputStream outputStream = request.getOutputStream();

        String filename = path.substring(7);
        filename = URLDecoder.decode(filename, StandardCharsets.UTF_8);
        java.io.File file = new java.io.File(directory, filename);

        if (method.equals(HttpMethod.GET.name())) {
            processGetRequest(outputStream, file);
        } else if (method.equals(HttpMethod.POST.name())) {
            processPostRequest(request, file);
        }
    }

    private void processPostRequest(Request request,
                                    java.io.File file) throws IOException {

        OutputStream outputStream = request.getOutputStream();
        InputStream inputStream = request.getInputStream();
        Map<String, String> headers = request.getHeaders();

        String contentLengthHeader = headers.get("Content-Length");
        if (contentLengthHeader == null) {
            String response = "HTTP/1.1 400 Bad Request\r\n\r\n";
            outputStream.write(response.getBytes());
            return;
        }

        int contentLength = Integer.parseInt(contentLengthHeader);
        byte[] body = new byte[contentLength];
        int totalBytesRead = 0;

        while (totalBytesRead < contentLength) {
            int bytesRead = inputStream.read(body, totalBytesRead, contentLength - totalBytesRead);
            if (bytesRead == -1) break; // Stop if no more data
            totalBytesRead += bytesRead;
        }

        if (totalBytesRead != contentLength) {
            String response = "HTTP/1.1 400 Bad Request\r\n\r\n";
            outputStream.write(response.getBytes());
            return;
        }

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(body);
        }

        String response = "HTTP/1.1 201 Created\r\n\r\n";
        outputStream.write(response.getBytes());
    }

    private void processGetRequest(OutputStream outputStream, java.io.File file) throws IOException {
        if (!file.exists() || file.isDirectory()) {
            String response = "HTTP/1.1 404 Not Found\r\n\r\n";
            outputStream.write(response.getBytes());
            return;
        }

        byte[] fileContent = new byte[(int) file.length()];
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(fileContent);
        }

        String response = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: application/octet-stream\r\n" +
                "Content-Length: " + fileContent.length + "\r\n\r\n";
        outputStream.write(response.getBytes());
        outputStream.write(fileContent);
    }
}
