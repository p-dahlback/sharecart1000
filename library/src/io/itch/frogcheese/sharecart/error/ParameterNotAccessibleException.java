package io.itch.frogcheese.sharecart.error;

import java.util.Locale;

/**
 * Describes when a parameter could not be accessed because of an invalid
 * sharecart state.
 */
public class ParameterNotAccessibleException extends ShareCartException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ParameterNotAccessibleException(String parameter) {
        super(String.format(Locale.US, "Can't access parameter '%s' without valid sharecart file.", parameter));
    }
}
