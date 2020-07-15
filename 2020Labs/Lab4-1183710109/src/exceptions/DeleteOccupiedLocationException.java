package exceptions;

public class DeleteOccupiedLocationException extends Exception {
    private static final long serialVersionUID = 1L;

    public DeleteOccupiedLocationException() {
        super();
    }

    public DeleteOccupiedLocationException(String message) {
        super(message);
    }

    public DeleteOccupiedLocationException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeleteOccupiedLocationException(Throwable cause) {
        super(cause);
    }
}