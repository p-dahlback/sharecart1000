package io.itch.frogcheese.sharecart.error;

/**
 * A general exception for the sharecart library. All thrown exceptions by the library extend
 * this class.
 */
public class SharecartException extends RuntimeException {

    public SharecartException() {
    }

    public SharecartException(String s) {
        super(s);
    }

    public SharecartException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public SharecartException(Throwable throwable) {
        super(throwable);
    }

}
