package httpserver.handler.implementation;

import httpserver.dto.Request;
import httpserver.handler.RequestHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import static httpserver.constant.Constant.ACCEPT_ENCODING;
import static httpserver.constant.Constant.GZIP;

public class Echo implements RequestHandler {

    @Override
    public void handle(Request request) throws IOException {
        String path = request.getPath();
        OutputStream outputStream = request.getOutputStream();
        Map<String, String> headers = request.getHeaders();

        String echoString = path.substring(6);
        boolean useGzip = GZIP.equals(headers.get(ACCEPT_ENCODING));

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        OutputStream responseStream = useGzip ? new GZIPOutputStream(byteStream) : byteStream;
        responseStream.write(echoString.getBytes(StandardCharsets.UTF_8));
        responseStream.close();

        byte[] responseBody = byteStream.toByteArray();

        String response = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/plain\r\n" +
                (useGzip ? "Content-Encoding: gzip\r\n" : "") +
                "Content-Length: " + responseBody.length + "\r\n\r\n";

        outputStream.write(response.getBytes());
        outputStream.write(responseBody);
    }
}
