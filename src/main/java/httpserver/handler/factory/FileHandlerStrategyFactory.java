package httpserver.handler.factory;

import httpserver.enums.HttpMethod;
import httpserver.handler.FileHandlerStrategy;
import httpserver.handler.implementation.strategy.Get;
import httpserver.handler.implementation.strategy.Post;
import httpserver.helper.RequestResponseHelper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import static httpserver.enums.HttpMethod.GET;
import static httpserver.enums.HttpMethod.POST;

public class FileHandlerStrategyFactory {

    private static final Logger log = Logger.getLogger(FileHandlerStrategyFactory.class.getName());

    private final Map<HttpMethod, FileHandlerStrategy> strategies = new ConcurrentHashMap<>();

    public FileHandlerStrategyFactory(RequestResponseHelper requestResponseHelper) {
        try {
            synchronized (FileHandlerStrategyFactory.class) {
                this.strategies.put(GET, new Get(requestResponseHelper));
                this.strategies.put(POST, new Post(requestResponseHelper));
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "Exception in FileHandlerStrategyFactory()..{0}", e.getMessage());
        }
    }

    public FileHandlerStrategy getStrategy(HttpMethod method) {
        return this.strategies.get(method);
    }
}