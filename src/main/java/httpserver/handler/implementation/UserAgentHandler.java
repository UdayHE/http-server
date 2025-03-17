package httpserver.handler.implementation;

import httpserver.dto.Request;
import httpserver.handler.RequestHandler;
import httpserver.helper.RequestResponseHelper;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import static httpserver.constant.Constant.UNKNOWN;
import static httpserver.constant.Constant.USER_AGENT;

public class UserAgentHandler implements RequestHandler {

    private final RequestResponseHelper requestResponseHelper;

    public UserAgentHandler(RequestResponseHelper requestResponseHelper) {
        this.requestResponseHelper = requestResponseHelper;
    }

    @Override
    public void handle(Request request) throws IOException {
        OutputStream outputStream = request.getOutputStream();
        Map<String, String> headers = request.getHeaders();
        String userAgent = headers.getOrDefault(USER_AGENT, UNKNOWN);
        requestResponseHelper.sendResponse(outputStream, requestResponseHelper.okResponse(userAgent));
    }


}
