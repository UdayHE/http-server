package httpserver;

import httpserver.handler.RouteHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import static httpserver.constant.Constant.PORT;
import static httpserver.constant.Constant.SPACE;

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
            // Since the tester restarts your program quite often, setting SO_REUSEADDR
            // ensures that we don't run into 'Address already in use' errors
            serverSocket.setReuseAddress(true);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                log.info("Accepted connection");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                OutputStream outputStream = clientSocket.getOutputStream();
                processRequest(bufferedReader, outputStream);
                clientSocket.close();
            }
        } catch (IOException e) {
            log.log(Level.SEVERE, "IOException: {0}", e.getMessage());
        }
    }

    private static void processRequest(BufferedReader bufferedReader,
                                       OutputStream outputStream) throws IOException {
        String requestLine = bufferedReader.readLine();
        log.log(Level.INFO, "Request:{0}", requestLine);
        RouteHandler routeHandler = new RouteHandler();
        if (requestLine != null && !requestLine.isBlank()) {
            String[] requestParts = requestLine.split(SPACE);
            if (requestParts.length >= 2) {
                String path = requestParts[1];
                routeHandler.get(path).handle(path, bufferedReader, outputStream);
            }
        }
    }
}
