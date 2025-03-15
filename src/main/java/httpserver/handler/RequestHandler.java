package httpserver.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;

public interface RequestHandler {

    void handle(String path, BufferedReader reader, OutputStream outputStream) throws IOException;
}
