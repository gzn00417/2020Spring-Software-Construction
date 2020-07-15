package exceptions;

public class SameEntrySameDayException extends Exception {
    private static final long serialVersionUID = 1L;

    public SameEntrySameDayException() {
        super();
    }

    public SameEntrySameDayException(String message) {
        super(message);
    }

    public SameEntrySameDayException(String message, Throwable cause) {
        super(message, cause);
    }

    public SameEntrySameDayException(Throwable cause) {
        super(cause);
    }
}