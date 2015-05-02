package io.itch.frogcheese.sharecart;

public class Constraints {

	public static short MAX_SIZE_X = 1023;
	public static short MAX_SIZE_Y = 1023;
	public static int MAX_SIZE_MISC = 65535;
	public static int MAX_SIZE_NAME_LENGTH = 1023;
	public static int MISC_ITEMS_LENGTH = 4;
	public static int SWITCH_ITEMS_LENGTH = 8;

	public static boolean validX(short x) {
		return Constraints.validInteger(x, MAX_SIZE_X);
	}

	public static short clampX(short x) {
		return (short) (x < 0 ? 0 : Math.min(x, MAX_SIZE_X));
	}

	public static boolean validY(short y) {
		return Constraints.validInteger(y, MAX_SIZE_Y);
	}

	public static short clampY(short y) {
		return (short) (y < 0 ? 0 : Math.min(y, MAX_SIZE_Y));
	}

	public static boolean validMisc(int misc) {
		return Constraints.validInteger(misc, MAX_SIZE_MISC);
	}

	public static boolean validMiscIndex(int index) {
		return Constraints.validInteger(index, MISC_ITEMS_LENGTH);
	}

	public static int clampMisc(int misc) {
		return misc < 0 ? 0 : Math.min(misc, MAX_SIZE_MISC);
	}

	public static boolean validName(String name) {
		return Constraints.validString(name, MAX_SIZE_NAME_LENGTH);
	}

	public static String clampName(String name) {
		if(name == null)
			return "";
		if(name.length() > MAX_SIZE_NAME_LENGTH)
			return name.substring(0, MAX_SIZE_NAME_LENGTH);
		return name;
	}

	public static boolean validSwitchIndex(int index) {
		return Constraints.validInteger(index, SWITCH_ITEMS_LENGTH);
	}

	private static boolean validInteger(int integer, int maxSize) {
		return integer >= 0 && integer <= maxSize;
	}

	private static boolean validString(String string, int maxLength) {
		return string != null && string.length() <= maxLength;
	}
}
