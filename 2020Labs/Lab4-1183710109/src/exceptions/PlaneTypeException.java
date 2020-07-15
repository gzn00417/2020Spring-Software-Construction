package exceptions;

public class PlaneTypeException extends Exception {
    private static final long serialVersionUID = 1L;

    public PlaneTypeException() {
        super();
    }

    public PlaneTypeException(String message) {
        super(message);
    }

    public PlaneTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public PlaneTypeException(Throwable cause) {
        super(cause);
    }
}