package httpserver.handler.implementation;

import httpserver.dto.Request;
import httpserver.handler.RequestHandler;
import httpserver.helper.RequestResponseHelper;

import java.io.IOException;

import static httpserver.constant.Constant.NOT_FOUND;

public class NotFoundHandler implements RequestHandler {

    private final RequestResponseHelper requestResponseHelper;

    public NotFoundHandler(RequestResponseHelper requestResponseHelper) {
        this.requestResponseHelper = requestResponseHelper;
    }


    @Override
    public void handle(Request request) throws IOException {
        requestResponseHelper.sendResponse(request.getOutputStream(), NOT_FOUND);
    }
}