package httpserver.handler.implementation;

import httpserver.dto.Request;
import httpserver.enums.HttpMethod;
import httpserver.handler.FileHandlerStrategy;
import httpserver.handler.RequestHandler;
import httpserver.handler.factory.FileHandlerStrategyFactory;
import httpserver.helper.RequestResponseHelper;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import static httpserver.constant.Constant.*;
import static httpserver.enums.Handler.FILE;

public class File implements RequestHandler {

    private final String directory;
    private final RequestResponseHelper requestResponseHelper;

    public File(RequestResponseHelper requestResponseHelper, String directory) {
        this.directory = directory != null ? directory : EMPTY;
        this.requestResponseHelper = requestResponseHelper;
    }

    @Override
    public void handle(Request request) throws IOException {
        String method = request.getMethod();
        String path = request.getPath();
        OutputStream outputStream = request.getOutputStream();

        if (isBadRequest(path, outputStream)) return;

        String filename = path.substring(7);
        filename = URLDecoder.decode(filename, StandardCharsets.UTF_8);
        java.io.File file = new java.io.File(directory, filename);
        FileHandlerStrategy strategy = FileHandlerStrategyFactory.getStrategy(HttpMethod.valueOf(method));
        if (strategy != null)
            strategy.handle(request, file);
        else
            requestResponseHelper.sendResponse(outputStream, METHOD_NOT_ALLOWED);
    }

    private boolean isBadRequest(String path, OutputStream outputStream) throws IOException {
        if (!path.startsWith(FILE.getValue()) || path.length() <= 7) {
            requestResponseHelper.sendResponse(outputStream, BAD_REQUEST);
            return true;
        }
        return false;
    }
}
