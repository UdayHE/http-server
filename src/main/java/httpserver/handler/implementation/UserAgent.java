package httpserver.handler.implementation;

import httpserver.dto.Request;
import httpserver.handler.RequestHandler;
import httpserver.helper.RequestResponseHelper;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import static httpserver.constant.Constant.UNKNOWN;
import static httpserver.constant.Constant.USER_AGENT;

public class UserAgent implements RequestHandler {

    private final RequestResponseHelper requestResponseHelper;

    public UserAgent(RequestResponseHelper requestResponseHelper) {
        this.requestResponseHelper = requestResponseHelper;
    }

    @Override
    public void handle(Request request) throws IOException {
        OutputStream outputStream = request.getOutputStream();
        Map<String, String> headers = request.getHeaders();

        String userAgent = headers.getOrDefault(USER_AGENT, UNKNOWN);

        String response = getResponse(userAgent);

        requestResponseHelper.sendResponse(outputStream, response);
//        outputStream.write(response.getBytes(StandardCharsets.UTF_8));
//        outputStream.flush();
    }

    private  String getResponse(String userAgent) {
        return "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/plain\r\n" +
                "Content-Length: " + userAgent.length() + "\r\n\r\n" +
                userAgent;
    }

}
