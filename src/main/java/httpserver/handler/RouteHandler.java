package httpserver.handler;

import httpserver.handler.implementation.*;

import java.util.HashMap;
import java.util.Map;

import static httpserver.constant.Constant.ARGS_DIRECTORY;
import static httpserver.constant.Constant.DEFAULT_DIRECTORY;
import static httpserver.enums.Handler.*;

public class RouteHandler {

    private final Map<String, RequestHandler> handlers;
    private final RequestHandler notFoundHandler;

    public RouteHandler(String[] args) {
        handlers = new HashMap<>();
        handlers.put(ROOT.getValue(), new Root());
        handlers.put(ECHO.getValue(), new Echo());
        handlers.put(USER_AGENT.getValue(), new UserAgent());
        handlers.put(FILE.getValue(), new File(getDirectory(args)));
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

    private String getDirectory(String[] args) {
        String directory = DEFAULT_DIRECTORY;
        if (args.length == 2 && ARGS_DIRECTORY.equals(args[0]))
            directory = args[1];
        return directory;
    }
}