package httpserver.handler.implementation.strategy;

import httpserver.dto.Request;
import httpserver.handler.FileHandlerStrategy;
import httpserver.helper.RequestResponseHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Map;

import static httpserver.constant.Constant.*;

public class Post implements FileHandlerStrategy {

    private final RequestResponseHelper requestResponseHelper;

    public Post(RequestResponseHelper requestResponseHelper) {
        this.requestResponseHelper = requestResponseHelper;
    }

    @Override
    public void handle(Request request, java.io.File file) throws IOException {
        OutputStream outputStream = request.getOutputStream();
        InputStream inputStream = request.getInputStream();
        Map<String, String> headers = request.getHeaders();

        String contentLengthHeader = headers.get(CONTENT_LENGTH);
        if (contentLengthHeader == null) {
            requestResponseHelper.sendResponse(outputStream, BAD_REQUEST);
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
            requestResponseHelper.sendResponse(outputStream, BAD_REQUEST);
            return;
        }

        Files.write(file.toPath(), body);
        requestResponseHelper.sendResponse(outputStream, CREATED);
    }
}