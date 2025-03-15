package handler.implementation;

import handler.RequestHandler;

import java.io.IOException;
import java.io.OutputStream;

public class Echo implements RequestHandler {

    @Override
    public void handle(String path, OutputStream outputStream) throws IOException {
        String echoString = path.substring(6);
        String response = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/plain\r\n" +
                "Content-Length: " + echoString.length() + "\r\n\r\n" +
                echoString;
        outputStream.write(response.getBytes());
    }
}
