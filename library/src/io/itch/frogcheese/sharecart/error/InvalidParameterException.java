package io.itch.frogcheese.sharecart.error;

import java.util.Locale;

/**
 * Describes when a parameter could not be modified because the new value
 * was invalid.
 */
public class InvalidParameterException extends SharecartException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public InvalidParameterException(String parameter, int value) {
        this(parameter, String.valueOf(value));
    }

    public InvalidParameterException(String parameter, boolean value) {
        this(parameter, String.valueOf(value));
    }

    public InvalidParameterException(String parameter, short value) {
        this(parameter, String.valueOf(value));
    }

    public InvalidParameterException(String parameter, String value) {
        super(String.format(Locale.US, "'%s' is not a valid value for parameter '%s'", value, parameter));
    }

}
