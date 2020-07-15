package exceptions;

public class ResourceSharedException extends Exception {
    private static final long serialVersionUID = 1L;

    public ResourceSharedException() {
        super();
    }

    public ResourceSharedException(String message) {
        super(message);
    }

    public ResourceSharedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceSharedException(Throwable cause) {
        super(cause);
    }
}