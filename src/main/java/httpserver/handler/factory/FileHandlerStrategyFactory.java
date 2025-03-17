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
import static java.util.Objects.nonNull;

public class FileHandlerStrategyFactory {

    private final Map<HttpMethod, FileHandlerStrategy> strategies = new HashMap<>();

    public FileHandlerStrategyFactory(RequestResponseHelper requestResponseHelper) {
        if(nonNull(requestResponseHelper)) {
            this.strategies.put(GET, new Get(requestResponseHelper));
            this.strategies.put(POST, new Post(requestResponseHelper));
        }
    }

    public FileHandlerStrategy getStrategy(HttpMethod method) {
        if (method == null) {
            throw new IllegalArgumentException("HTTP method cannot be null");
        }
        FileHandlerStrategy strategy = this.strategies.get(method);
        if (strategy == null) {
            throw new UnsupportedOperationException("Unsupported HTTP method: " + method);
        }
        return strategy;
    }
}