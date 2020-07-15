package exceptions;

public class PlaneInconsistentInfoException extends Exception {
    private static final long serialVersionUID = 1L;

    public PlaneInconsistentInfoException() {
        super();
    }

    public PlaneInconsistentInfoException(String message) {
        super(message);
    }

    public PlaneInconsistentInfoException(String message, Throwable cause) {
        super(message, cause);
    }

    public PlaneInconsistentInfoException(Throwable cause) {
        super(cause);
    }
}