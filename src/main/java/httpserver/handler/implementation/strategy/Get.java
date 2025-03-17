package httpserver.handler.implementation.strategy;

import httpserver.dto.Request;
import httpserver.handler.FileHandlerStrategy;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.logging.Logger;

import static httpserver.constant.Constant.INTERNAL_SERVER_ERROR;
import static httpserver.constant.Constant.NOT_FOUND;

public class Get implements FileHandlerStrategy {

    private static final Logger log = Logger.getLogger(Get.class.getName());

    @Override
    public void handle(Request request, java.io.File file) throws IOException {
        OutputStream outputStream = request.getOutputStream();
        if (!file.exists() || file.isDirectory()) {
            log.info("File not found or is a directory: " + file.getAbsolutePath());
            sendResponse(outputStream, NOT_FOUND);
            return;
        }

        try {
            byte[] fileContent = Files.readAllBytes(file.toPath());
            String response = getResponse(fileContent);
            outputStream.write(response.getBytes());
            outputStream.write(fileContent, 0, fileContent.length);
            outputStream.flush();
            log.info("File served: " + file.getAbsolutePath());
        } catch (IOException e) {
            log.severe("Failed to read file: " + e.getMessage());
            sendResponse(outputStream, INTERNAL_SERVER_ERROR);
        }
    }

    private String getResponse(byte[] fileContent) {
        String response = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: application/octet-stream\r\n" +
                "Content-Length: " + fileContent.length + "\r\n\r\n";
        return response;
    }

    private void sendResponse(OutputStream outputStream, String response) throws IOException {
        outputStream.write(response.getBytes());
        outputStream.flush();
    }
}