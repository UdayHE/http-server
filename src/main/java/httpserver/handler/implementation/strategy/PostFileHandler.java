package httpserver.handler.implementation.strategy;

import httpserver.dto.Request;
import httpserver.handler.FileHandlerStrategy;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.logging.Logger;

import static httpserver.constant.Constant.BAD_REQUEST;
import static httpserver.constant.Constant.CREATED;

public class PostFileHandler implements FileHandlerStrategy {
    private static final Logger log = Logger.getLogger(PostFileHandler.class.getName());

    @Override
    public void handle(Request request, java.io.File file) throws IOException {
        OutputStream outputStream = request.getOutputStream();
        byte[] body = request.getInputStream().readAllBytes();

        if (body.length == 0) {
            outputStream.write(BAD_REQUEST.getBytes());
        } else {
            Files.write(file.toPath(), body);
            outputStream.write(CREATED.getBytes());
            log.info("File saved: " + file.getAbsolutePath());
        }
        outputStream.flush();
    }
}