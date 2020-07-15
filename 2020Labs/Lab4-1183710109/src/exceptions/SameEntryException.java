package exceptions;

public class SameEntryException extends Exception {
    private static final long serialVersionUID = 1L;

    public SameEntryException() {
        super();
    }

    public SameEntryException(String message) {
        super(message);
    }

    public SameEntryException(String message, Throwable cause) {
        super(message, cause);
    }

    public SameEntryException(Throwable cause) {
        super(cause);
    }
}