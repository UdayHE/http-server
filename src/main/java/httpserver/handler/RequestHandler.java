package httpserver.handler;

import httpserver.dto.Request;

import java.io.IOException;

public interface RequestHandler {

    void handle(Request request) throws IOException;
}
