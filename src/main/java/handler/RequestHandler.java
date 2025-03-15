package handler;

import java.io.IOException;
import java.io.OutputStream;

public interface RequestHandler {

    void handle(String path, OutputStream outputStream) throws IOException;
}
