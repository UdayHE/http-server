package httpserver.handler.implementation;

import httpserver.dto.Request;
import httpserver.handler.RequestHandler;

import java.io.IOException;

import static httpserver.constant.Constant.OK;

public class Root implements RequestHandler {

    @Override
    public void handle(Request request) throws IOException {
        request.getOutputStream().write(OK.getBytes());
        request.getOutputStream().flush();
    }
}
