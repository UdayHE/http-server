package httpserver.handler.implementation;

import httpserver.dto.Request;
import httpserver.enums.HttpMethod;
import httpserver.handler.RequestHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static httpserver.constant.Constant.*;
import static httpserver.enums.Handler.FILE;

public class File implements RequestHandler {

    private static final Logger log = Logger.getLogger(File.class.getName());
    private final String directory;

    public File(String directory) {
        this.directory = directory != null ? directory : EMPTY;
    }

    @Override
    public void handle(Request request) throws IOException {
        String method = request.getMethod();
        String path = request.getPath();
        OutputStream outputStream = request.getOutputStream();

        if (!path.startsWith(FILE.name()) || path.length() <= 7) {
            sendResponse(outputStream, BAD_REQUEST);
            return;
        }

        String filename = path.substring(7);
        filename = URLDecoder.decode(filename, StandardCharsets.UTF_8);
        java.io.File file = new java.io.File(directory, filename);

        if (method.equals(HttpMethod.GET.name())) {
            processGetRequest(outputStream, file);
        } else if (method.equals(HttpMethod.POST.name())) {
            processPostRequest(request, file);
        } else {
            sendResponse(outputStream, METHOD_NOT_ALLOWED);
        }
    }

    private void processPostRequest(Request request, java.io.File file) throws IOException {
        OutputStream outputStream = request.getOutputStream();
        InputStream inputStream = request.getInputStream();
        Map<String, String> headers = request.getHeaders();

        String contentLengthHeader = headers.get(CONTENT_LENGTH);
        if (contentLengthHeader == null) {
            log.warning("Missing Content-Length header for POST request");
            sendResponse(outputStream, LENGTH_REQUIRED);
            return;
        }

        int contentLength;
        try {
            contentLength = Integer.parseInt(contentLengthHeader);
            if (contentLength < 0) {
                log.log(Level.WARNING, "Negative Content-Length: {0}", contentLength);
                sendResponse(outputStream, BAD_REQUEST);
                return;
            }
        } catch (NumberFormatException e) {
            log.warning("Invalid Content-Length: " + contentLengthHeader);
            sendResponse(outputStream, BAD_REQUEST);
            return;
        }

        // Read exactly contentLength bytes
        byte[] body = new byte[contentLength];
        int totalBytesRead = 0;
        while (totalBytesRead < contentLength) {
            int bytesRead = inputStream.read(body, totalBytesRead, contentLength - totalBytesRead);
            if (bytesRead == -1) {
                log.log(Level.WARNING, "Unexpected end of stream after {0} bytes", totalBytesRead);
                sendResponse(outputStream, BAD_REQUEST);
                return;
            }
            totalBytesRead += bytesRead;
        }

        try {
            Files.createDirectories(file.getParentFile().toPath());
            Files.write(file.toPath(), body);
            log.info("File written: " + file.getAbsolutePath());
            sendResponse(outputStream, CREATED);
        } catch (IOException e) {
            log.severe("Failed to write file: " + e.getMessage());
            sendResponse(outputStream, INTERNAL_SERVER_ERROR);
        }
    }

    private void processGetRequest(OutputStream outputStream, java.io.File file) throws IOException {
        if (!file.exists() || file.isDirectory()) {
            log.info("File not found or is a directory: " + file.getAbsolutePath());
            sendResponse(outputStream, NOT_FOUND);
            return;
        }

        try {
            byte[] fileContent = Files.readAllBytes(file.toPath());
            String response = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: application/octet-stream\r\n" +
                    "Content-Length: " + fileContent.length + "\r\n\r\n";
            outputStream.write(response.getBytes());
            outputStream.write(fileContent);
            outputStream.flush();
            log.info("File served: " + file.getAbsolutePath());
        } catch (IOException e) {
            log.severe("Failed to read file: " + e.getMessage());
            sendResponse(outputStream, INTERNAL_SERVER_ERROR);
        }
    }

    private void sendResponse(OutputStream outputStream, String response) throws IOException {
        outputStream.write(response.getBytes());
        outputStream.flush();
    }
}
