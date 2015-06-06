package io.itch.frogcheese.sharecart;

/**
 * Utility that handles constraints for the sharecart parameters.
 */
public class Constraints {

    /**
     * Constant: maximum size for the X parameter.
     */
    public static final int MAX_SIZE_X = 1023;
    /**
     * Constant: maximum size for the y parameter.
     */
    public static final int MAX_SIZE_Y = 1023;
    /**
     * Constant: maximum size for the misc parameters.
     */
    public static final int MAX_SIZE_MISC = 65535;
    /**
     * Constant: maximum length for the name parameter.
     */
    public static final int MAX_SIZE_NAME_LENGTH = 1023;
    /**
     * Constant: amount of misc parameters.
     */
    public static final int MISC_ITEMS_LENGTH = 4;
    /**
     * Constant: amount of switch parameters.
     */
    public static final int SWITCH_ITEMS_LENGTH = 8;

    /**
     * @param x the value to check.
     *
     * @return Whether or not the given value conforms to the constraints for the x parameter.
     */
    public static boolean validX(int x) {
        return Constraints.validInteger(x, MAX_SIZE_X);
    }

    /**
     * Clamps the given value to within the constraints for the x parameter.
     *
     * @param x the value to check.
     *
     * @return the provided value, clamped to be within the constraints for x.
     */
    public static short clampX(int x) {
        return (short) (x < 0 ? 0 : Math.min(x, MAX_SIZE_X));
    }

    /**
     * @param y the value to check.
     *
     * @return Whether or not the given value conforms to the constraints for the y parameter.
     */
    public static boolean validY(int y) {
        return Constraints.validInteger(y, MAX_SIZE_Y);
    }

    /**
     * Clamps the given value to within the constraints for the y parameter.
     *
     * @param y the value to check.
     *
     * @return the provided value, clamped to be within the constraints for y.
     */
    public static short clampY(int y) {
        return (short) (y < 0 ? 0 : Math.min(y, MAX_SIZE_Y));
    }

    /**
     * @param misc the value to check.
     *
     * @return Whether or not the given value conforms to the constraints for the misc parameters.
     */
    public static boolean validMisc(int misc) {
        return Constraints.validInteger(misc, MAX_SIZE_MISC);
    }

    /**
     * @param index the index to check.
     *
     * @return Whether or not the given index is a valid misc parameter.
     */
    public static boolean validMiscIndex(int index) {
        return Constraints.validInteger(index, MISC_ITEMS_LENGTH - 1);
    }

    /**
     * Clamps the given value to within the constraints for the misc parameter.
     *
     * @param misc the value to check.
     *
     * @return the provided value, clamped to be within the constraints for misc.
     */
    public static int clampMisc(int misc) {
        return misc < 0 ? 0 : Math.min(misc, MAX_SIZE_MISC);
    }

    /**
     * @param name the value to check.
     *
     * @return Whether or not the given value conforms to the constraints for the name parameter.
     */
    public static boolean validName(String name) {
        return Constraints.validString(name, MAX_SIZE_NAME_LENGTH);
    }

    /**
     * Clamps the given value to within the constraints for the name parameter.
     *
     * @param name the value to check.
     *
     * @return the provided value. If the value is null, an empty string is returned. If the value is too long, a substring
     * of the maximum length will be returned.
     */
    public static String clampName(String name) {
        if (name == null)
            return "";
        if (name.length() > MAX_SIZE_NAME_LENGTH)
            return name.substring(0, MAX_SIZE_NAME_LENGTH);
        return name;
    }

    /**
     * @param index the index to check.
     *
     * @return Whether or not the given index is a valid switch parameter.
     */
    public static boolean validSwitchIndex(int index) {
        return Constraints.validInteger(index, SWITCH_ITEMS_LENGTH - 1);
    }

    private static boolean validInteger(int integer, int maxSize) {
        return integer >= 0 && integer <= maxSize;
    }

    private static boolean validString(String string, int maxLength) {
        return string != null && string.length() <= maxLength;
    }
}
