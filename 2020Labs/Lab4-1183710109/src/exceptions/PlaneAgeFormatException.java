package exceptions;

public class PlaneAgeFormatException extends Exception {
    private static final long serialVersionUID = 1L;

    public PlaneAgeFormatException() {
        super();
    }

    public PlaneAgeFormatException(String message) {
        super(message);
    }

    public PlaneAgeFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public PlaneAgeFormatException(Throwable cause) {
        super(cause);
    }
}