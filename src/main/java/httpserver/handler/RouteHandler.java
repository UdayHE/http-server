package httpserver.handler;

import httpserver.handler.implementation.Echo;
import httpserver.handler.implementation.NotFound;
import httpserver.handler.implementation.Root;

import java.util.HashMap;
import java.util.Map;

import static httpserver.enums.Handler.ECHO;
import static httpserver.enums.Handler.ROOT;

public class RouteHandler {

    private final Map<String, RequestHandler> handlers;
    private final RequestHandler notFoundHandler;

    public RouteHandler() {
        handlers = new HashMap<>();
        handlers.put(ROOT.getValue(), new Root());
        handlers.put(ECHO.getValue(), new Echo());
        notFoundHandler = new NotFound();
    }

    public RequestHandler get(String path) {
        RequestHandler handler = handlers.get(path);
        if (handler == null && path.startsWith(ECHO.getValue())) {
            handler = handlers.get(ECHO.getValue());
        }
        return handler != null ? handler : notFoundHandler;
    }
}