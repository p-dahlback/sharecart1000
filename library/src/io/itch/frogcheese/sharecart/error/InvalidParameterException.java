package io.itch.frogcheese.sharecart.error;

import java.util.Locale;

/**
 * Describes when a parameter could not be modified because the new value was invalid.
 */
public class InvalidParameterException extends ShareCartException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new exception for the given parameter.
     *
     * @param parameter the name of the parameter.
     * @param value     the invalid value that caused the exception.
     */
    public InvalidParameterException(String parameter, int value) {
        this(parameter, String.valueOf(value));
    }

    /**
     * Creates a new exception for the given parameter.
     *
     * @param parameter the name of the parameter.
     * @param value     the invalid value that caused the exception.
     */
    public InvalidParameterException(String parameter, String value) {
        super(String.format(Locale.US, "'%s' is not a valid value for parameter '%s'", value, parameter));
    }

}
