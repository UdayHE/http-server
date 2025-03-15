package handler.implementation;

import handler.RequestHandler;

import java.io.IOException;
import java.io.OutputStream;

public class Root implements RequestHandler {

    @Override
    public void handle(String path, OutputStream outputStream) throws IOException {
        String response = "HTTP/1.1 200 OK\r\n\r\n";
        outputStream.write(response.getBytes());
    }
}
