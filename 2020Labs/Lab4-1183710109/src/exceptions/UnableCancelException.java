package exceptions;

public class UnableCancelException extends Exception {
    private static final long serialVersionUID = 1L;

    public UnableCancelException() {
        super();
    }

    public UnableCancelException(String message) {
        super(message);
    }

    public UnableCancelException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnableCancelException(Throwable cause) {
        super(cause);
    }
}