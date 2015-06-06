package io.itch.frogcheese.sharecart.error;

/**
 * A general exception for the sharecart library. All thrown exceptions by the library extend this class.
 */
public class ShareCartException extends RuntimeException {

    /**
     * Creates a new sharecart exception with no message.
     */
    public ShareCartException() {
    }

    /**
     * Creates a new sharecart exception with the given message.
     *
     * @param message the exception's message.
     */
    public ShareCartException(String message) {
        super(message);
    }

    /**
     * Creates a new sharecart exception with the given message and cause.
     *
     * @param message the exception's message.
     * @param cause   the original cause of the exception.
     */
    public ShareCartException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new sharecart exception with the given cause.
     *
     * @param cause the original cause of the exception.
     */
    public ShareCartException(Throwable cause) {
        super(cause);
    }

}
