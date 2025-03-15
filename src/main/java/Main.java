import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import static constant.Constant.ROOT;
import static constant.Constant.SPACE;

public class Main {

    private static final Logger log = Logger.getLogger(Main.class.getName());


    public static void main(String[] args) {
        log.info("HTTP Server Started.");
        try {
            ServerSocket serverSocket = new ServerSocket(4221);
            // Since the tester restarts your program quite often, setting SO_REUSEADDR
            // ensures that we don't run into 'Address already in use' errors
            serverSocket.setReuseAddress(true);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                log.info("Accepted connection");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                OutputStream outputStream = clientSocket.getOutputStream();

                String requestLine = bufferedReader.readLine();
                log.log(Level.INFO, "Request:{0}", requestLine);

                if (requestLine != null && !requestLine.isBlank()) {
                    String[] requestParts = requestLine.split(SPACE);
                    if (requestParts.length > 2) {
                        String path = requestParts[1];
                        String response;
                        if (ROOT.equals(path))
                            response = "HTTP/1.1 200 OK\r\n\r\n";
                        else
                            response = "HTTP/1.1 404 Not Found\r\n\r\n";
                        outputStream.write(response.getBytes());
                        outputStream.flush();
                    }
                }
                outputStream.flush();
                clientSocket.close();
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }
}

