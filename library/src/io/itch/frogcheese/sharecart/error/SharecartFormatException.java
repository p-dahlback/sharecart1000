package io.itch.frogcheese.sharecart.error;

import java.util.Locale;

/**
 * Created by Peter on 15-05-21.
 */
public class SharecartFormatException extends SharecartException {

    public SharecartFormatException(String message, Object... params) {
        super(String.format(Locale.US, message, params));
    }

}
