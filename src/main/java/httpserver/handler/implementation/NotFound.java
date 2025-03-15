package httpserver.handler.implementation;

import httpserver.dto.Request;
import httpserver.handler.RequestHandler;

import java.io.IOException;

public class NotFound implements RequestHandler {

    @Override
    public void handle(Request request) throws IOException {
        String response = "HTTP/1.1 404 Not Found\r\n\r\n";
        request.getOutputStream().write(response.getBytes());
        request.getOutputStream().flush();
    }
}