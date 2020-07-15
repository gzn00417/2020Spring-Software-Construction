package exceptions;

public class PlaneNumberFormatException extends Exception {
    private static final long serialVersionUID = 1L;

    public PlaneNumberFormatException() {
        super();
    }

    public PlaneNumberFormatException(String message) {
        super(message);
    }

    public PlaneNumberFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public PlaneNumberFormatException(Throwable cause) {
        super(cause);
    }
}