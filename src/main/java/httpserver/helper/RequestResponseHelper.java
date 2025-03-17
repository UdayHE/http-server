package httpserver.helper;

import java.io.IOException;
import java.io.OutputStream;

import static httpserver.constant.Constant.ARGS_DIRECTORY;
import static httpserver.constant.Constant.EMPTY;
import static java.nio.charset.StandardCharsets.UTF_8;

public class RequestResponseHelper {

    private final String[] args;

    public RequestResponseHelper(String[] args) {
        this.args = args;
    }

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

    public String okResponse(String userAgent) {
        return "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/plain\r\n" +
                "Content-Length: " + userAgent.length() + "\r\n\r\n" +
                userAgent;
    }

    public String octateStreamOKResponse(byte[] fileContent) {
        return "HTTP/1.1 200 OK\r\n" +
                "Content-Type: application/octet-stream\r\n" +
                "Content-Length: " + fileContent.length + "\r\n\r\n";
    }

    public void sendResponse(OutputStream outputStream, String response) throws IOException {
        outputStream.write(response.getBytes());
        outputStream.flush();
    }

    public String getDirectory() {
        String directory = EMPTY;
        if (this.args.length == 2 && ARGS_DIRECTORY.equals(this.args[0]))
            directory = args[1];
        return directory;
    }


}
