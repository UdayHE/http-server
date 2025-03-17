package httpserver.handler.implementation;

import httpserver.dto.Request;
import httpserver.handler.RequestHandler;
import httpserver.helper.RequestResponseHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import static httpserver.constant.Constant.*;
import static java.nio.charset.StandardCharsets.UTF_8;

public class EchoHandler implements RequestHandler {

    private final RequestResponseHelper requestResponseHelper;

    public EchoHandler(RequestResponseHelper requestResponseHelper) {
        this.requestResponseHelper = requestResponseHelper;
    }


    @Override
    public void handle(Request request) throws IOException {
        OutputStream outputStream = request.getOutputStream();
        try {
            String path = request.getPath();
            validatePath(path);

            String message = path.substring(6);
            boolean useGzip = isGzipAccepted(request.getHeaders());
            byte[] responseBody = processMessage(message, useGzip);

            sendResponse(outputStream, responseBody, useGzip);
        } catch (IllegalArgumentException e) {
            requestResponseHelper.sendErrorResponse(outputStream, 400, BAD_REQUEST_STR);
        } catch (Exception e) {
            requestResponseHelper.sendErrorResponse(outputStream, 500, INTERNAL_SERVER_STR);
        }
    }

    private void validatePath(String path) {
        if (path.length() <= 6)
            throw new IllegalArgumentException(INVALID_PATH);
    }

    private boolean isGzipAccepted(Map<String, String> headers) {
        String acceptEncoding = headers.getOrDefault(ACCEPT_ENCODING, EMPTY);
        return acceptEncoding.toLowerCase().contains(GZIP);
    }

    private byte[] processMessage(String message, boolean useGzip) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

        if (useGzip) {
            try (GZIPOutputStream gzipStream = new GZIPOutputStream(byteStream)) {
                gzipStream.write(message.getBytes(UTF_8));
            }
        } else
            byteStream.write(message.getBytes(UTF_8));
        return byteStream.toByteArray();
    }

    private void sendResponse(OutputStream outputStream, byte[] responseBody, boolean useGzip) throws IOException {
        String responseHeaders = requestResponseHelper.buildHeaders(useGzip, responseBody);
        outputStream.write(responseHeaders.getBytes(UTF_8));
        outputStream.write(responseBody);
        outputStream.flush();
    }



}
