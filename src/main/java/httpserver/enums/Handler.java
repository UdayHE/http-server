package httpserver.enums;

public enum Handler {

    ROOT("/"),
    ECHO("/echo/");


    private final String value;

    Handler(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
