package exceptions;

public class HugeTimeGapException extends Exception {
    private static final long serialVersionUID = 1L;

    public HugeTimeGapException() {
        super();
    }

    public HugeTimeGapException(String message) {
        super(message);
    }

    public HugeTimeGapException(String message, Throwable cause) {
        super(message, cause);
    }

    public HugeTimeGapException(Throwable cause) {
        super(cause);
    }
}