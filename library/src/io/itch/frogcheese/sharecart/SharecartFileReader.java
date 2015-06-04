package io.itch.frogcheese.sharecart;

import io.itch.frogcheese.sharecart.error.ShareCartFormatException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Handles reading from a sharecart file.
 */
class ShareCartFileReader implements Closeable {

    private static final String VALUE_TYPE_BOOLEAN = "boolean";
    private static final String VALUE_TYPE_INTEGER = "integer";

    private Scanner scanner;

    private String path;

    private int currentLine = 0;

    private boolean isStrict;

    private String latestToken;

    /**
     * Constructor.
     *
     * @param file the file that should be written to.
     * @throws FileNotFoundException
     */
    public ShareCartFileReader(File file) throws FileNotFoundException {
        if (file == null)
            throw new IllegalArgumentException("File cannot be null");

        path = file.getPath();
        scanner = new Scanner(file)
                .useDelimiter(ShareCartFileConstants.DELIMITER_PARAMETER_PATTERN);
    }

    /**
     * Set whether or not the reader should fail when encountering an error in the file.
     *
     * @param strict If true, the reader will fail with an exception for a poorly formatted or erroneous file. If false,
     *               the reader will silently ignore any faulty parameters and instead assign a default value.
     */
    public void setIsStrict(boolean strict) {
        isStrict = strict;
    }

    /**
     * @return Whether or not the reader will fail with an exception when encountering an error in the file's format.
     * If false, any faulty parameters will be substituted with default values.
     */
    public boolean isStrict() {
        return isStrict;
    }

    /**
     * Reads the contents of the file into a ShareCart object.
     *
     * @return A new ShareCart containing the parameters of the file.
     */
    public ShareCart read() {
        ShareCart ret = ShareCart.withDefaults();

        try {
            // Skip title
            scanner.next();
            currentLine++;

            int x = readInt(ShareCartFileConstants.PARAMETER_X);
            ret.x(validateX(x));
            currentLine++;

            int y = readInt(ShareCartFileConstants.PARAMETER_Y);
            ret.y(validateY(y));
            currentLine++;

            for (int i = 0; i < ShareCartFileConstants.PARAMETER_MISC.length; i++) {
                int misc = readInt(ShareCartFileConstants.PARAMETER_MISC[i]);
                ret.misc(i, validateMisc(i, misc));
                currentLine++;
            }

            String name = readString(ShareCartFileConstants.PARAMETER_NAME);
            ret.name(validateName(name));
            currentLine++;

            for (int i = 0; i < ShareCartFileConstants.PARAMETER_SWITCH.length; i++) {
                ret.switchValue(i, readBoolean(ShareCartFileConstants.PARAMETER_SWITCH[i]));
                currentLine++;
            }
            checkConstraints(ret);

        } catch (NoSuchElementException e) {
            if (isStrict()) {
                throw new ShareCartFormatException("File '%s', line %d: Encountered end of file prematurely",
                        path, currentLine);
            }
        }
        return ret;
    }

    private int validateX(int x) {
        if (!Constraints.validX(x)) {
            if (isStrict())
                throwConstraintException(ShareCartFileConstants.PARAMETER_X, x);
            return Constraints.clampX(x);
        }
        return x;
    }

    private int validateY(int y) {
        if (!Constraints.validY(y)) {
            if (isStrict())
                throwConstraintException(ShareCartFileConstants.PARAMETER_Y, y);
            return Constraints.clampY(y);
        }
        return y;
    }

    private int validateMisc(int index, int misc) {
        if (!Constraints.validMisc(misc)) {
            if (isStrict())
                throwConstraintException(ShareCartFileConstants.PARAMETER_MISC[index], misc);
            return Constraints.clampMisc(misc);
        }
        return misc;
    }

    private String validateName(String name) {
        if (!Constraints.validName(name)) {
            if (isStrict())
                throwConstraintException(ShareCartFileConstants.PARAMETER_NAME, name);
            return Constraints.clampName(name);
        }
        return name;
    }

    private void checkConstraints(ShareCart shareCart) {

        int y = shareCart.y();


        for (int i = 0; i < ShareCartFileConstants.PARAMETER_MISC.length; i++) {
            int misc = shareCart.misc(i);
            if (!Constraints.validMisc(misc)) {
                if (isStrict())
                    throwConstraintException(ShareCartFileConstants.PARAMETER_MISC[i], misc);
                shareCart.misc(i, Constraints.clampMisc(misc));
            }
        }

        String name = shareCart.name();
        if (!Constraints.validName(name)) {
            if (isStrict())
                throwConstraintException(ShareCartFileConstants.PARAMETER_NAME, name);
            shareCart.name(Constraints.clampName(name));
        }
    }

    private int readInt(String key) {
        String value = readString(key);
        if (value.length() == 0 || !StringUtils.isNumeric(value)) {
            if (isStrict()) {
                throwInvalidValueFormatException(value, VALUE_TYPE_INTEGER);
            }
            return 0;
        }

        return Integer.valueOf(value);
    }

    private String readString(String key) {
        String token = getNextToken();
        Pair<String, String> keyValue = getKeyValuePair(token);
        if (keyValue == null)
            return "";

        if (!hasKey(keyValue, key)) {
            if (isStrict())
                throw new ShareCartFormatException("File '%s', line %d: Found '%s' where parameter '%s' was expected",
                        path, currentLine, keyValue.getKey(), key);

            // If this is the second failure for this token, then the token is likely faulty.
            // In that case, we should just skip to the next token.
            if (latestToken != null) {
                latestToken = null;
                return readString(key);
            }
            latestToken = token;
            return "";
        }

        String valueString = keyValue.getValue();
        if (valueString.startsWith("\"") && valueString.length() > 1) {
            valueString = valueString.substring(1);
        }
        if (valueString.endsWith("\"") && valueString.length() > 1) {
            valueString = valueString.substring(0, valueString.length() - 1);
        }
        latestToken = null;
        return valueString;
    }

    private boolean readBoolean(String key) {
        String value = readString(key);
        if (value.length() == 0) {
            if (isStrict()) {
                throwInvalidValueFormatException(value, VALUE_TYPE_BOOLEAN);
            }
            return false;
        }
        if (StringUtils.isNumeric(value)) {
            if (isStrict()) {
                throwInvalidValueFormatException(value, VALUE_TYPE_BOOLEAN);
            }
            return Integer.valueOf(value) != 0;
        }
        if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
            return Boolean.valueOf(value);
        } else if (isStrict()) {
            throwInvalidValueFormatException(value, VALUE_TYPE_BOOLEAN);
        }
        return false;
    }

    private String getNextToken() {
        return latestToken != null ? latestToken : scanner.next().replaceAll("\r", "");
    }

    private Pair<String, String> getKeyValuePair(String token) {
        int firstIndex = token.indexOf(ShareCartFileConstants.DELIMITER_VALUE);
        if (firstIndex < token.length() - 1 && firstIndex > 0) {
            String left = token.substring(0, firstIndex);
            String right = token.substring(firstIndex + 1);
            return Pair.of(left, right);
        } else if (isStrict()) {
            throw new ShareCartFormatException("File '%s', line %d: '%s' is not a valid parameter definition",
                    path, currentLine, token);
        }
        return null;
    }

    private boolean hasKey(Pair<String, String> keyValue, String key) {
        if (isStrict())
            return key.equals(keyValue.getKey());
        return key.equalsIgnoreCase(keyValue.getKey());
    }

    private void throwInvalidValueFormatException(String value, String valueType) throws ShareCartFormatException {
        throw new ShareCartFormatException("File '%s', line %d: The string '%s' is not a valid %s",
                path, currentLine, value, valueType);
    }

    private void throwConstraintException(String parameterName, Object value) throws ShareCartFormatException {
        throw new ShareCartFormatException("File '%s', line %d: The %s value '%s' does not fulfill " +
                "the constraints of the parameter",
                path, currentLine, parameterName, value.toString());
    }

    @Override
    public void close() throws IOException {
        this.scanner.close();
    }

}
