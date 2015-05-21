package io.itch.frogcheese.sharecart.error;

/**
 * Describes when a parameter could not be accessed because of an invalid
 * sharecart state.
 */
public class ParameterNotAccessibleException extends SharecartException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public ParameterNotAccessibleException(String parameter) {
		super("Can't access parameter " + parameter + " without valid sharecart file.");
	}
}
