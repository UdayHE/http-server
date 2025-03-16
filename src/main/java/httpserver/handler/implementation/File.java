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
import java.util.logging.Logger;

import static httpserver.constant.Constant.CONTENT_LENGTH;
import static httpserver.enums.Handler.FILE;

public class File implements RequestHandler {
    private static final Logger LOGGER = Logger.getLogger(File.class.getName());
    private final String directory;

    public File(String directory) {
        this.directory = directory != null ? directory : ""; // Ensure directory is never null
    }

    @Override
    public void handle(Request request) throws IOException {
        String method = request.getMethod();
        String path = request.getPath();
        OutputStream outputStream = request.getOutputStream();

        // Validate path and extract filename safely
        if (!path.startsWith(FILE.name()) || path.length() <= 7) {
            sendResponse(outputStream, "HTTP/1.1 400 Bad Request\r\n\r\n");
            return;
        }

        String filename = path.substring(7); // After "/files/"
        filename = URLDecoder.decode(filename, StandardCharsets.UTF_8);
        java.io.File file = new java.io.File(directory, filename);

        if (method.equals(HttpMethod.GET.name())) {
            processGetRequest(outputStream, file);
        } else if (method.equals(HttpMethod.POST.name())) {
            processPostRequest(request, file);
        } else {
            sendResponse(outputStream, "HTTP/1.1 405 Method Not Allowed\r\n\r\n");
        }
    }

    private void processPostRequest(Request request, java.io.File file) throws IOException {
        OutputStream outputStream = request.getOutputStream();
        InputStream inputStream = request.getInputStream();
        Map<String, String> headers = request.getHeaders();

        String contentLengthHeader = headers.get(CONTENT_LENGTH);
        if (contentLengthHeader == null) {
            sendResponse(outputStream, "HTTP/1.1 400 Bad Request\r\n\r\n");
            return;
        }

        int contentLength = Integer.parseInt(contentLengthHeader);
        byte[] body = new byte[contentLength];
        int totalBytesRead = 0;

        while (totalBytesRead < contentLength) {
            int bytesRead = inputStream.read(body, totalBytesRead, contentLength - totalBytesRead);
            if (bytesRead == -1) break;
            totalBytesRead += bytesRead;
        }

        if (totalBytesRead != contentLength) {
            sendResponse(outputStream, "HTTP/1.1 400 Bad Request\r\n\r\n");
            return;
        }

        Files.createDirectories(file.getParentFile().toPath());
        Files.write(file.toPath(), body);
        sendResponse(outputStream, "HTTP/1.1 201 Created\r\n\r\n");
    }

    private void processGetRequest(OutputStream outputStream, java.io.File file) throws IOException {
        if (!file.exists() || file.isDirectory()) {
            LOGGER.info("File not found or is a directory: " + file.getAbsolutePath());
            sendResponse(outputStream, "HTTP/1.1 404 Not Found\r\n\r\n");
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
            LOGGER.info("File served: " + file.getAbsolutePath());
        } catch (IOException e) {
            LOGGER.severe("Failed to read file: " + e.getMessage());
            sendResponse(outputStream, "HTTP/1.1 500 Internal Server Error\r\n\r\n");
        }
    }

    private void sendResponse(OutputStream outputStream, String response) throws IOException {
        outputStream.write(response.getBytes());
        outputStream.flush();
    }
}
