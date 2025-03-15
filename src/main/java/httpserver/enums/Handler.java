package httpserver.enums;

public enum Handler {

    ROOT("/"),
    ECHO("/echo/"),
    USER_AGENT("/user-agent"),
    FILE("/files/");


    private final String value;

    Handler(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
