package httpserver.handler;

import httpserver.handler.implementation.Echo;
import httpserver.handler.implementation.NotFound;
import httpserver.handler.implementation.Root;
import httpserver.handler.implementation.UserAgent;

import java.util.HashMap;
import java.util.Map;

import static httpserver.enums.Handler.*;

public class RouteHandler {

    private final Map<String, RequestHandler> handlers;
    private final RequestHandler notFoundHandler;

    public RouteHandler() {
        handlers = new HashMap<>();
        handlers.put(ROOT.getValue(), new Root());
        handlers.put(ECHO.getValue(), new Echo());
        handlers.put(USER_AGENT.getValue(), new UserAgent());
        notFoundHandler = new NotFound();
    }

    public RequestHandler get(String path) {
        RequestHandler handler = handlers.get(path);
        if (handler == null && path.startsWith(ECHO.getValue()))
            handler = handlers.get(ECHO.getValue());
        else if (path.equals(USER_AGENT.getValue()))
            handler = handlers.get(USER_AGENT.getValue());
        return handler != null ? handler : notFoundHandler;
    }
}