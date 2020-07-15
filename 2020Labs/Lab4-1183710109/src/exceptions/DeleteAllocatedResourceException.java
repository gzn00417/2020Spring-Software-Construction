package exceptions;

public class DeleteAllocatedResourceException extends Exception {
    private static final long serialVersionUID = 1L;

    public DeleteAllocatedResourceException() {
        super();
    }

    public DeleteAllocatedResourceException(String message) {
        super(message);
    }

    public DeleteAllocatedResourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeleteAllocatedResourceException(Throwable cause) {
        super(cause);
    }
}