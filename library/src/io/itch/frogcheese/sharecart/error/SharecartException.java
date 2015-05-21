package io.itch.frogcheese.sharecart.error;

/**
 * Created by Peter on 15-05-21.
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
