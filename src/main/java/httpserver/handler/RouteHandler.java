package httpserver.handler;

import httpserver.enums.HttpMethod;
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
        this.handlers.put(ROOT.getValue(), new Root(requestResponseHelper));
        this.handlers.put(ECHO.getValue(), new Echo(requestResponseHelper));
        this.handlers.put(USER_AGENT.getValue(), new UserAgent(requestResponseHelper));
        this.handlers.put(FILE.getValue(), new File(requestResponseHelper, fileHandlerStrategyFactory));
        this.notFoundHandler = new NotFound(requestResponseHelper);
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