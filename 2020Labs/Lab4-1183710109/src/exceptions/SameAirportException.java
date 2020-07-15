package exceptions;

public class SameAirportException extends Exception {
    private static final long serialVersionUID = 1L;

    public SameAirportException() {
        super();
    }

    public SameAirportException(String message) {
        super(message);
    }

    public SameAirportException(String message, Throwable cause) {
        super(message, cause);
    }

    public SameAirportException(Throwable cause) {
        super(cause);
    }
}