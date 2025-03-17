package httpserver.handler.factory;

import httpserver.enums.HttpMethod;
import httpserver.handler.FileHandlerStrategy;
import httpserver.handler.implementation.strategy.Get;
import httpserver.handler.implementation.strategy.Post;
import httpserver.helper.RequestResponseHelper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static httpserver.enums.HttpMethod.GET;
import static httpserver.enums.HttpMethod.POST;

public class FileHandlerStrategyFactory {

    private final Map<HttpMethod, FileHandlerStrategy> strategies = new ConcurrentHashMap<>();

    public FileHandlerStrategyFactory(RequestResponseHelper requestResponseHelper) {
        this.strategies.put(GET, new Get(requestResponseHelper));
        this.strategies.put(POST, new Post(requestResponseHelper));
    }

    public FileHandlerStrategy getStrategy(HttpMethod method) {
        return this.strategies.get(method);
    }
}