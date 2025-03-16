package httpserver.handler.implementation;

import httpserver.dto.Request;
import httpserver.handler.RequestHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class UserAgent implements RequestHandler {

    @Override
    public void handle(Request request) throws IOException {
        OutputStream outputStream = request.getOutputStream();
        Map<String, String> headers = request.getHeaders();

        String userAgent = headers.getOrDefault("User-Agent", "Unknown");

        String response = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/plain\r\n" +
                "Content-Length: " + userAgent.length() + "\r\n\r\n" +
                userAgent;

        outputStream.write(response.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
    }

}
