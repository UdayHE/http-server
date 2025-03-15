package httpserver.handler.implementation;

import httpserver.handler.RequestHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;

public class Echo implements RequestHandler {

    @Override
    public void handle(String method, String path, BufferedReader reader, OutputStream outputStream) throws IOException {
        String echoString = path.substring(6);
        String response = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/plain\r\n" +
                "Content-Length: " + echoString.length() + "\r\n\r\n" +
                echoString;
        outputStream.write(response.getBytes());
    }
}
