package io.itch.frogcheese.sharecart.error;

public class InvalidParameterException extends IllegalArgumentException {

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
		super(value + " is not a valid value for parameter " + parameter);
	}

}