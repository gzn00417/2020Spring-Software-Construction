package exceptions;

public class LocationSharedException extends Exception {
    private static final long serialVersionUID = 1L;

    public LocationSharedException() {
        super();
    }

    public LocationSharedException(String message) {
        super(message);
    }

    public LocationSharedException(String message, Throwable cause) {
        super(message, cause);
    }

    public LocationSharedException(Throwable cause) {
        super(cause);
    }
}