package httpserver.handler.implementation;

import httpserver.dto.Request;
import httpserver.handler.RequestHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static httpserver.constant.Constant.EMPTY;
import static httpserver.constant.Constant.USER_AGENT;

public class UserAgent implements RequestHandler {

    @Override
    public void handle(Request request) throws IOException {
        BufferedReader reader = request.getReader();
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

    private String getUserAgent(BufferedReader reader) throws IOException {
        String userAgent = EMPTY;
        String line;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            if (line.startsWith(USER_AGENT)) {
                userAgent = line.substring(12).trim();
                break;
            }
        }
        return userAgent;
    }
}
