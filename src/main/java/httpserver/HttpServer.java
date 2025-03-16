package httpserver;

import httpserver.dto.Request;
import httpserver.handler.RouteHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import static httpserver.constant.Constant.*;

public class HttpServer {

    private static final Logger log = Logger.getLogger(HttpServer.class.getName());
    private static final HttpServer INSTANCE = new HttpServer();

    private HttpServer() {
    }

    public static HttpServer getInstance() {
        return INSTANCE;
    }

    public void start(String[] args) {
        log.info("HTTP Server Started.");
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            serverSocket.setReuseAddress(true);
            ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
            RouteHandler routeHandler = new RouteHandler(args);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                log.info("Accepted connection");
                executorService.execute(() -> handleClient(clientSocket, routeHandler));
            }
        } catch (IOException e) {
            log.log(Level.SEVERE, "IOException: {0}", e.getMessage());
        }
    }

    private void handleClient(Socket clientSocket, RouteHandler routeHandler) {
        try (
                InputStream rawInputStream = clientSocket.getInputStream();
                OutputStream outputStream = clientSocket.getOutputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(rawInputStream));
        ) {
            PushbackInputStream pushbackInput = new PushbackInputStream(rawInputStream, 8192);
            handleRequest(routeHandler, pushbackInput, outputStream, reader);
        } catch (IOException e) {
            log.log(Level.SEVERE, "Error handling client: {0}", e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                log.log(Level.SEVERE, "Error closing client socket: {0}", e.getMessage());
            }
        }
    }

    private void handleRequest(RouteHandler routeHandler, PushbackInputStream inputStream, OutputStream outputStream, BufferedReader reader) throws IOException {
        // Read request line manually
        String requestLine = readLine(inputStream);
        if (requestLine == null) return;

        log.log(Level.INFO, "Request: {0}", requestLine);
        String[] requestParts = requestLine.split(SPACE);
        if (requestParts.length < 3) return;

        String method = requestParts[0];
        String path = requestParts[1];
        Map<String, String> headers = getHeaders(inputStream);

        routeHandler.get(path).handle(new Request.Builder()
                .method(method)
                .path(path)
                .reader(reader)
                .outputStream(outputStream)
                .inputStream(inputStream)
                .headers(headers)
                .build());
    }

    private String readLine(PushbackInputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int prev = -1;
        int curr;
        while ((curr = inputStream.read()) != -1) {
            if (prev == '\r' && curr == '\n') {
                byte[] bytes = byteArrayOutputStream.toByteArray();
                // Exclude \r\n from the line
                return new String(bytes, 0, bytes.length - 1, StandardCharsets.UTF_8);
            }
            byteArrayOutputStream.write(curr);
            prev = curr;
        }
        return null; // End of stream
    }

    private Map<String, String> getHeaders(PushbackInputStream inputStream) throws IOException {
        Map<String, String> headers = new HashMap<>();
        String line;
        while (true) {
            line = readLine(inputStream);
            if (line == null || line.isEmpty())
                break;

            int colonIdx = line.indexOf(COLON);
            if (colonIdx == -1) {
                log.log(Level.WARNING, "Invalid header format: {0}", line);
                continue;
            }
            String key = line.substring(0, colonIdx).trim();
            String value = line.substring(colonIdx + 1).trim();
            headers.put(key, value);
        }
        return headers;
    }

}