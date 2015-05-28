package io.itch.frogcheese.sharecart.error;

import java.util.Locale;

/**
 * Describes when a sharecart file cannot be parsed because of format issues.
 */
public class SharecartFormatException extends SharecartException {

    public SharecartFormatException(String message, Object... params) {
        super(String.format(Locale.US, message, params));
    }

}
