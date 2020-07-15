package exceptions;

public class PlaneSeatRangeException extends Exception {
    private static final long serialVersionUID = 1L;

    public PlaneSeatRangeException() {
        super();
    }

    public PlaneSeatRangeException(String message) {
        super(message);
    }

    public PlaneSeatRangeException(String message, Throwable cause) {
        super(message, cause);
    }

    public PlaneSeatRangeException(Throwable cause) {
        super(cause);
    }
}