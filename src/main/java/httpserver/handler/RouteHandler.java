package httpserver.handler;

import httpserver.handler.factory.FileHandlerStrategyFactory;
import httpserver.handler.implementation.*;
import httpserver.helper.RequestResponseHelper;

import java.util.HashMap;
import java.util.Map;

import static httpserver.enums.Handler.*;

public class RouteHandler {

    private final Map<String, RequestHandler> handlers;
    private final RequestHandler notFoundHandler;


    public RouteHandler(String[] args) {
        RequestResponseHelper requestResponseHelper = new RequestResponseHelper(args);
        FileHandlerStrategyFactory fileHandlerStrategyFactory = new FileHandlerStrategyFactory(requestResponseHelper);
        this.handlers = new HashMap<>();
        this.handlers.put(ROOT.getValue(), new RootHandler(requestResponseHelper));
        this.handlers.put(ECHO.getValue(), new EchoHandler(requestResponseHelper));
        this.handlers.put(USER_AGENT.getValue(), new UserAgentHandler(requestResponseHelper));
        this.handlers.put(FILE.getValue(), new FileHandler(requestResponseHelper, fileHandlerStrategyFactory));
        this.notFoundHandler = new NotFoundHandler(requestResponseHelper);
    }

    public RequestHandler get(String path) {
        RequestHandler handler = this.handlers.get(path);
        if (handler == null && path.startsWith(ECHO.getValue()))
            handler = this.handlers.get(ECHO.getValue());
        else if (path.equals(USER_AGENT.getValue()))
            handler = this.handlers.get(USER_AGENT.getValue());
        else if (path.startsWith(FILE.getValue()))
            handler = this.handlers.get(FILE.getValue());
        return handler != null ? handler : this.notFoundHandler;
    }

}