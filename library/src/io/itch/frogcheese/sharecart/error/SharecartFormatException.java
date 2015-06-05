package io.itch.frogcheese.sharecart.error;

import java.util.Locale;

/**
 * Describes when a sharecart file cannot be parsed because of format issues.
 */
public class ShareCartFormatException extends ShareCartException {

    /**
     * Creates a format exception with the given message.
     *
     * @param message the message. May contain parameters according
     *                to {@link String#format(Locale, String, Object...)}.
     * @param params  optional format parameters.
     */
    public ShareCartFormatException(String message, Object... params) {
        super(String.format(Locale.US, message, params));
    }

}
