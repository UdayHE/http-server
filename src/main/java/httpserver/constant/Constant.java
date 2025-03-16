package httpserver.constant;

public class Constant {

    private Constant() {}

    public static final String SPACE = " ";
    public static final String EMPTY = "";
    public static final String USER_AGENT = "User-Agent:";
    public static final String DEFAULT_DIRECTORY = "/tmp";
    public static final String ARGS_DIRECTORY = "--directory";
    public static final String COLON = ": ";
    public static final String CONTENT_LENGTH = "Content-Length";

    public static final String BAD_REQUEST = "HTTP/1.1 400 Bad Request\r\n\r\n";
    public static final String METHOD_NOT_ALLOWED = "HTTP/1.1 405 Method Not Allowed\r\n\r\n";
    public static final String LENGTH_REQUIRED = "HTTP/1.1 411 Length Required\r\n\r\n";
    public static final String CREATED = "HTTP/1.1 201 Created\r\n\r\n";
    public static final String INTERNAL_SERVER_ERROR = "HTTP/1.1 500 Internal Server Error\r\n\r\n";
    public static final String NOT_FOUND = "HTTP/1.1 404 Not Found\r\n\r\n";

    public static final int PORT = 4221;
    public static final int THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors() * 2;

}
