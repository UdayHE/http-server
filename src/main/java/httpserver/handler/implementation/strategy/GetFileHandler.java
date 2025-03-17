package httpserver.handler.implementation.strategy;

import httpserver.dto.Request;
import httpserver.handler.FileHandlerStrategy;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.logging.Logger;

import static httpserver.constant.Constant.NOT_FOUND;

public class GetFileHandler implements FileHandlerStrategy {
    private static final Logger log = Logger.getLogger(GetFileHandler.class.getName());

    @Override
    public void handle(Request request, java.io.File file) throws IOException {
        OutputStream outputStream = request.getOutputStream();

        if (!file.exists() || file.isDirectory()) {
            log.info("File not found: " + file.getAbsolutePath());
            outputStream.write(NOT_FOUND.getBytes());
        } else {
            byte[] fileContent = Files.readAllBytes(file.toPath());
            String response = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: application/octet-stream\r\n" +
                    "Content-Length: " + fileContent.length + "\r\n\r\n";
            outputStream.write(response.getBytes());
            outputStream.write(fileContent);
        }
        outputStream.flush();
    }
}