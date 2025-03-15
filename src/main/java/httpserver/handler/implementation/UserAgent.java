package httpserver.handler.implementation;

import httpserver.handler.RequestHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;

import static httpserver.constant.Constant.EMPTY;
import static httpserver.constant.Constant.USER_AGENT;

public class UserAgent implements RequestHandler {

    @Override
    public void handle(String method, String path, BufferedReader reader, OutputStream outputStream) throws IOException {
        String userAgent = getUserAgent(reader);

        String response = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/plain\r\n" +
                "Content-Length: " + userAgent.length() + "\r\n\r\n" +
                userAgent;
        outputStream.write(response.getBytes());
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
