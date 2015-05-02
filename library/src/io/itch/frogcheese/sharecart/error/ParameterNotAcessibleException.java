package io.itch.frogcheese.sharecart.error;

public class ParameterNotAcessibleException extends IllegalStateException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public ParameterNotAcessibleException(String parameter) {
		super("Can't access parameter " + parameter + " without valid sharecart file.");
	}
}
