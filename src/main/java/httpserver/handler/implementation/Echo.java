package httpserver.handler.implementation;

import httpserver.dto.Request;
import httpserver.handler.RequestHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;

import static httpserver.constant.Constant.*;

public class Echo implements RequestHandler {

    @Override
    public void handle(Request request) throws IOException {
        String path = request.getPath();
        OutputStream outputStream = request.getOutputStream();
        Map<String, String> headers = request.getHeaders();

        String message = path.substring(6);
        String acceptEncoding = headers.getOrDefault(ACCEPT_ENCODING, EMPTY);
        boolean useGzip = Arrays.stream(acceptEncoding.split(COMMA))
                .map(String::trim)
                .anyMatch(GZIP::equalsIgnoreCase);

        String responseHeaders = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/plain\r\n" +
                (useGzip ? "Content-Encoding: gzip\r\n" : "") +
                "Content-Length: " + message.length() + "\r\n\r\n";

        outputStream.write(responseHeaders.getBytes(StandardCharsets.UTF_8));
        outputStream.write(message.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
    }
}
