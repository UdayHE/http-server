package httpserver.handler.implementation;

import httpserver.dto.Request;
import httpserver.handler.RequestHandler;
import httpserver.helper.RequestResponseHelper;

import java.io.IOException;

import static httpserver.constant.Constant.OK;

public class Root implements RequestHandler {

    private final RequestResponseHelper requestResponseHelper;

    public Root(RequestResponseHelper requestResponseHelper) {
        this.requestResponseHelper = requestResponseHelper;
    }

    @Override
    public void handle(Request request) throws IOException {
        requestResponseHelper.sendResponse(request.getOutputStream(), OK);
    }
}
