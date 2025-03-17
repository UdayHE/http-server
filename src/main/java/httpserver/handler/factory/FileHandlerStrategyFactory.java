package httpserver.handler.factory;

import httpserver.enums.HttpMethod;
import httpserver.handler.FileHandlerStrategy;
import httpserver.handler.implementation.strategy.Get;
import httpserver.handler.implementation.strategy.Post;

import java.util.HashMap;
import java.util.Map;

import static httpserver.enums.HttpMethod.GET;
import static httpserver.enums.HttpMethod.POST;

public class FileHandlerStrategyFactory {

    private FileHandlerStrategyFactory() {}

    private static final Map<HttpMethod, FileHandlerStrategy> strategies = new HashMap<>();

    static {
        strategies.put(GET, new Get());
        strategies.put(POST, new Post());
    }

    public static FileHandlerStrategy getStrategy(HttpMethod method) {
        return strategies.get(method);
    }
}