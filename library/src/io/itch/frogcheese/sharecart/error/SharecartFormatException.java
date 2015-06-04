package io.itch.frogcheese.sharecart.error;

import java.util.Locale;

/**
 * Describes when a sharecart file cannot be parsed because of format issues.
 */
public class ShareCartFormatException extends ShareCartException {

    public ShareCartFormatException(String message, Object... params) {
        super(String.format(Locale.US, message, params));
    }

}
