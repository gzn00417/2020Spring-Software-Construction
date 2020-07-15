package exceptions;

public class EntryInconsistentInfoException extends Exception {
    private static final long serialVersionUID = 1L;

    public EntryInconsistentInfoException() {
        super();
    }

    public EntryInconsistentInfoException(String message) {
        super(message);
    }

    public EntryInconsistentInfoException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntryInconsistentInfoException(Throwable cause) {
        super(cause);
    }
}