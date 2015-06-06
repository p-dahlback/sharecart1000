package io.itch.frogcheese.sharecart;

/**
 * Constants for the sharecart file.
 */
class ShareCartFileConstants {

    /**
     * Title string found at the top of a sharecart file.
     */
    public static final String TITLE = "[Main]";

    /**
     * Key for the X parameter.
     */
    public static final String PARAMETER_X = "MapX";

    /**
     * Key for the X parameter.
     */
    public static final String PARAMETER_Y = "MapY";

    /**
     * Keys for the Misc parameters.
     */
    public static final String[] PARAMETER_MISC = new String[] {
            "Misc0", "Misc1", "Misc2", "Misc3"
    };

    /**
     * Ket for the Name parameter.
     */
    public static final String PARAMETER_NAME = "PlayerName";

    /**
     * Keys for the Switch parameters.
     */
    public static final String[] PARAMETER_SWITCH = new String[] {
            "Switch0", "Switch1", "Switch2", "Switch3", "Switch4", "Switch5", "Switch6", "Switch7"
    };

    /**
     * Delimiter between parameters. A CRLF line break.
     */
    public static final String DELIMITER_PARAMETER = "\r\n";

    /**
     * Pattern used to split up lines in the reader. Though files should be formatted with CRLF breaks,
     * this pattern assumes only LF so that we can read files with faulty line formatting gracefully.
     */
    public static final String DELIMITER_PARAMETER_PATTERN = "\n";

    /**
     * Delimiter between key and value.
     */
    public static final String DELIMITER_VALUE = "=";

}
