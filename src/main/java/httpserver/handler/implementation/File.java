package httpserver.handler.implementation;

import httpserver.dto.Request;
import httpserver.enums.HttpMethod;
import httpserver.handler.RequestHandler;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import static httpserver.constant.Constant.COLON;
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
        BufferedReader reader = request.getReader();
        OutputStream outputStream = request.getOutputStream();

        String filename = path.substring(7);
        filename = URLDecoder.decode(filename, StandardCharsets.UTF_8);
        java.io.File file = new java.io.File(directory, filename);

        if (method.equals(HttpMethod.GET.name())) {
            processGetRequest(outputStream, file);
        } else if (method.equals(HttpMethod.POST.name())) {
            processPostRequest(reader, outputStream, file);
        }
    }

    private void processPostRequest(BufferedReader reader, OutputStream outputStream, java.io.File file) throws IOException {
        String contentLengthHeader = null;
        String line;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            if (line.startsWith(CONTENT_LENGTH)) {
                contentLengthHeader = line.split(COLON)[1];
            }
        }

        if (contentLengthHeader == null) {
            String response = "HTTP/1.1 400 Bad Request\r\n\r\n";
            outputStream.write(response.getBytes());
            return;
        }

        int contentLength = Integer.parseInt(contentLengthHeader);
        char[] body = new char[contentLength];
        reader.read(body, 0, contentLength);

        try (FileWriter writer = new FileWriter(String.valueOf(file))) {
            writer.write(body);
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
