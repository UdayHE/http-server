package httpserver.handler.factory;

import httpserver.enums.HttpMethod;
import httpserver.handler.FileHandlerStrategy;
import httpserver.handler.implementation.strategy.Get;
import httpserver.handler.implementation.strategy.Post;
import httpserver.helper.RequestResponseHelper;

import java.util.HashMap;
import java.util.Map;

import static httpserver.enums.HttpMethod.GET;
import static httpserver.enums.HttpMethod.POST;

public class FileHandlerStrategyFactory {

    private final Map<HttpMethod, FileHandlerStrategy> strategies = new HashMap<>();

    public FileHandlerStrategyFactory(RequestResponseHelper requestResponseHelper) {
        strategies.put(GET, new Get(requestResponseHelper));
        strategies.put(POST, new Post(requestResponseHelper));
    }


    public FileHandlerStrategy getStrategy(HttpMethod method) {
        return this.strategies.get(method);
    }
}