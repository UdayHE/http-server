package httpserver.handler.factory;

import httpserver.enums.HttpMethod;
import httpserver.handler.FileHandlerStrategy;
import httpserver.handler.implementation.strategy.GetFileHandler;
import httpserver.handler.implementation.strategy.PostFileHandler;

import java.util.HashMap;
import java.util.Map;

public class FileHandlerStrategyFactory {

    private FileHandlerStrategyFactory() {}

    private static final Map<HttpMethod, FileHandlerStrategy> strategies = new HashMap<>();

    static {
        strategies.put(HttpMethod.GET, new GetFileHandler());
        strategies.put(HttpMethod.POST, new PostFileHandler());
    }

    public static FileHandlerStrategy getStrategy(HttpMethod method) {
        return strategies.get(method);
    }
}