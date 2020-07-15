package exceptions;

public class TimeOrderException extends Exception {
    private static final long serialVersionUID = 1L;

    public TimeOrderException() {
        super();
    }

    public TimeOrderException(String message) {
        super(message);
    }

    public TimeOrderException(String message, Throwable cause) {
        super(message, cause);
    }

    public TimeOrderException(Throwable cause) {
        super(cause);
    }
}
