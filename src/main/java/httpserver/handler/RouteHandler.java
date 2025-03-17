package httpserver.handler;

import httpserver.handler.implementation.*;
import httpserver.helper.RequestResponseHelper;

import java.util.HashMap;
import java.util.Map;

import static httpserver.constant.Constant.ARGS_DIRECTORY;
import static httpserver.constant.Constant.EMPTY;
import static httpserver.enums.Handler.*;

public class RouteHandler {

    private final Map<String, RequestHandler> handlers;
    private final RequestHandler notFoundHandler;


    public RouteHandler(String[] args) {
        handlers = new HashMap<>();
        RequestResponseHelper requestResponseHelper = new RequestResponseHelper();
        handlers.put(ROOT.getValue(), new Root(requestResponseHelper));
        handlers.put(ECHO.getValue(), new Echo(requestResponseHelper));
        handlers.put(USER_AGENT.getValue(), new UserAgent(requestResponseHelper));
        handlers.put(FILE.getValue(), new File(requestResponseHelper, getDirectory(args)));
        notFoundHandler = new NotFound(requestResponseHelper);
    }

    public RequestHandler get(String path) {
        RequestHandler handler = handlers.get(path);
        if (handler == null && path.startsWith(ECHO.getValue()))
            handler = handlers.get(ECHO.getValue());
        else if (path.equals(USER_AGENT.getValue()))
            handler = handlers.get(USER_AGENT.getValue());
        else if (path.startsWith(FILE.getValue()))
            handler = handlers.get(FILE.getValue());
        return handler != null ? handler : notFoundHandler;
    }

    private String getDirectory(String[] args) {
        String directory = EMPTY;
        if (args.length == 2 && ARGS_DIRECTORY.equals(args[0]))
            directory = args[1];
        return directory;
    }
}