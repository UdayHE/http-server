package httpserver.handler.implementation;

import httpserver.handler.RequestHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;

public class Root implements RequestHandler {

    @Override
    public void handle(String method, String path, BufferedReader reader, OutputStream outputStream) throws IOException {
        String response = "HTTP/1.1 200 OK\r\n\r\n";
        outputStream.write(response.getBytes());
    }
}
