package httpserver;

import httpserver.dto.Request;
import httpserver.handler.RouteHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
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
                BufferedReader reader = new BufferedReader(new InputStreamReader(rawInputStream));
                OutputStream outputStream = clientSocket.getOutputStream()
        ) {
            handleRequest(routeHandler, reader, outputStream, rawInputStream);
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

    private void handleRequest(RouteHandler routeHandler,
                               BufferedReader reader,
                               OutputStream outputStream,
                               InputStream inputStream) throws IOException {
        String requestLine = reader.readLine();
        if (requestLine == null) return;

        log.log(Level.INFO, "Request: {0}", requestLine);
        String[] requestParts = requestLine.split(SPACE);
        if (requestParts.length < 3) return;

        String method = requestParts[0];
        String path = requestParts[1];
        Map<String, String> headers = getHeaders(reader);

        routeHandler.get(path).handle(new Request.Builder()
                .method(method)
                .path(path)
                .reader(reader)
                .outputStream(outputStream)
                .inputStream(inputStream)
                .headers(headers)
                .build());
    }

    private Map<String, String> getHeaders(BufferedReader reader) throws IOException {
        Map<String, String> headers = new HashMap<>();
        String line;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            String[] headerParts = line.split(COLON, 2);
            if (headerParts.length == 2)
                headers.put(headerParts[0].trim(), headerParts[1].trim());
        }
        return headers;
    }
}