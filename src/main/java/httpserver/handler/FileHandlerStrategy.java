package httpserver.handler;

import httpserver.dto.Request;

import java.io.IOException;

public interface FileHandlerStrategy {

    void handle(Request request, java.io.File file) throws IOException;
}