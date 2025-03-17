package httpserver.helper;

import java.io.IOException;
import java.io.OutputStream;

import static java.nio.charset.StandardCharsets.UTF_8;

public class RequestResponseHelper {

    public void sendErrorResponse(OutputStream outputStream, int statusCode, String statusText) throws IOException {
        String response = "HTTP/1.1 " + statusCode + " " + statusText + "\r\n" +
                "Content-Length: 0\r\n\r\n";
        outputStream.write(response.getBytes(UTF_8));
        outputStream.flush();
    }

    public String buildHeaders(boolean useGzip, byte[] responseBody) {
        return "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/plain\r\n" +
                (useGzip ? "Content-Encoding: gzip\r\n" : "") +
                "Content-Length: " + responseBody.length + "\r\n\r\n";
    }

    public void sendResponse(OutputStream outputStream, String response) throws IOException {
        outputStream.write(response.getBytes());
        outputStream.flush();
    }


}
