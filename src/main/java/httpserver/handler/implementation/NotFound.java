package httpserver.handler.implementation;

import httpserver.dto.Request;
import httpserver.handler.RequestHandler;

import java.io.IOException;

import static httpserver.constant.Constant.NOT_FOUND;

public class NotFound implements RequestHandler {

    @Override
    public void handle(Request request) throws IOException {
        request.getOutputStream().write(NOT_FOUND.getBytes());
        request.getOutputStream().flush();
    }
}