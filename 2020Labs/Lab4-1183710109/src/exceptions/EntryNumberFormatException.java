package exceptions;

public class EntryNumberFormatException extends Exception {
    private static final long serialVersionUID = 1L;

    public EntryNumberFormatException() {
        super();
    }

    public EntryNumberFormatException(String message) {
        super(message);
    }

    public EntryNumberFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntryNumberFormatException(Throwable cause) {
        super(cause);
    }
}