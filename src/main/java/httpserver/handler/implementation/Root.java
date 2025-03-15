package httpserver.handler.implementation;

import httpserver.dto.Request;
import httpserver.handler.RequestHandler;

import java.io.IOException;

public class Root implements RequestHandler {

    @Override
    public void handle(Request request) throws IOException {
        String response = "HTTP/1.1 200 OK\r\n\r\n";
        request.getOutputStream().write(response.getBytes());
    }
}
