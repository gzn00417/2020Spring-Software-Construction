package exceptions;

public class DataPatternException extends Exception {
    private static final long serialVersionUID = 1L;

    public DataPatternException() {
        super();
    }

    public DataPatternException(String message) {
        super(message);
    }

    public DataPatternException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataPatternException(Throwable cause) {
        super(cause);
    }
}