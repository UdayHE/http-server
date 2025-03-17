package httpserver.handler.implementation.strategy;

import httpserver.dto.Request;
import httpserver.handler.FileHandlerStrategy;
import httpserver.helper.RequestResponseHelper;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.logging.Logger;

import static httpserver.constant.Constant.INTERNAL_SERVER_ERROR;
import static httpserver.constant.Constant.NOT_FOUND;

public class Get implements FileHandlerStrategy {

    private static final Logger log = Logger.getLogger(Get.class.getName());
    private final RequestResponseHelper requestResponseHelper;

    public Get(RequestResponseHelper requestResponseHelper) {
        this.requestResponseHelper = requestResponseHelper;
    }

    @Override
    public void handle(Request request, java.io.File file) throws IOException {
        OutputStream outputStream = request.getOutputStream();
        if (!file.exists() || file.isDirectory()) {
            log.info("File not found or is a directory: " + file.getAbsolutePath());
            requestResponseHelper.sendResponse(outputStream, NOT_FOUND);
            return;
        }

        try {
            byte[] fileContent = Files.readAllBytes(file.toPath());
            String response = requestResponseHelper.octateStreamOKResponse(fileContent);
            outputStream.write(response.getBytes());
            outputStream.write(fileContent, 0, fileContent.length);
            outputStream.flush();
            log.info("File served: " + file.getAbsolutePath());
        } catch (IOException e) {
            log.severe("Failed to read file: " + e.getMessage());
            requestResponseHelper.sendResponse(outputStream, INTERNAL_SERVER_ERROR);
        }
    }


}