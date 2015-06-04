package io.itch.frogcheese.sharecart.error;

/**
 * A general exception for the sharecart library. All thrown exceptions by the library extend
 * this class.
 */
public class ShareCartException extends RuntimeException {

    public ShareCartException() {
    }

    public ShareCartException(String s) {
        super(s);
    }

    public ShareCartException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ShareCartException(Throwable throwable) {
        super(throwable);
    }

}
