package httpserver.handler.implementation;

import httpserver.handler.RequestHandler;

import java.io.IOException;
import java.io.OutputStream;

public class NotFound implements RequestHandler {

    @Override
    public void handle(String path, OutputStream outputStream) throws IOException {
        String response = "HTTP/1.1 404 Not Found\r\n\r\n";
        outputStream.write(response.getBytes());
    }
}