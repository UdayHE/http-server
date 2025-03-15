package httpserver.handler.implementation;

import httpserver.handler.RequestHandler;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class File implements RequestHandler {

    private final String directory;

    public File(String directory) {
        this.directory = directory;
    }

    @Override
    public void handle(String path, BufferedReader reader, OutputStream outputStream) throws IOException {
        String filename = path.substring(7);
        java.io.File file = new java.io.File(directory, filename);

        if (!file.exists() || file.isDirectory()) {
            String response = "HTTP/1.1 404 Not Found\r\n\r\n";
            outputStream.write(response.getBytes());
            return;
        }

        byte[] fileContent = new byte[(int) file.length()];
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(fileContent);
        }

        String response = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: application/octet-stream\r\n" +
                "Content-Length: " + fileContent.length + "\r\n\r\n";
        outputStream.write(response.getBytes());
        outputStream.write(fileContent);
    }
}
