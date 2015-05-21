package io.itch.frogcheese.sharecart;

import io.itch.frogcheese.sharecart.error.SharecartFormatException;
import org.apache.commons.lang3.StringUtils;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * Handles reading from a sharecart file.
 */
class SharecartFileReader implements Closeable {

    private Scanner scanner;

    private boolean mStrict;

    /**
     * Constructor.
     *
     * @param path path to the file that should be written to.
     * @throws FileNotFoundException
     */
    public SharecartFileReader(String path) throws FileNotFoundException {
        this(new File(path));
    }

    /**
     * Constructor.
     *
     * @param file the file that should be written to.
     * @throws FileNotFoundException
     */
    public SharecartFileReader(File file) throws FileNotFoundException {
        this.scanner = new Scanner(file)
                .useDelimiter(SharecartFileConstants.DELIMITER_PARAMETER_PATTERN);
        this.scanner.next();
    }

    /**
     * Set whether or not the reader should fail when encountering an error in the file.
     *
     * @param strict If true, the reader will fail with an exception for a poorly formatted or erroneous file. If false,
     *               the reader will silently ignore any faulty parameters and instead assign a default value.
     */
    public void setIsStrict(boolean strict) {
        mStrict = strict;
    }

    /**
     * @return Whether or not the reader will fail with an exception when encountering an error in the file's format.
     * If false, any faulty parameters will be substituted with default values.
     */
    public boolean isStrict() {
        return mStrict;
    }

    /**
     * Reads the contents of the file into a Sharecart object.
     *
     * @return A new Sharecart containing the parameters of the file.
     */
    public Sharecart read() {
        Sharecart ret = new Sharecart();

        ret.x(readShort());
        ret.y(readShort());
        for (int i = 0; i < SharecartFileConstants.PARAMETER_MISC.length; i++) {
            ret.misc(i, readInt());
        }
        ret.name(readString());
        for (int i = 0; i < SharecartFileConstants.PARAMETER_SWITCH.length; i++) {
            ret.switchValue(i, readBoolean());
        }

        checkConstraints(ret);

        return ret;
    }

    private void checkConstraints(Sharecart sharecart) {
        short x = sharecart.x();
        if (!Constraints.validX(x)) {
            if (isStrict())
                throwConstraintException(SharecartFileConstants.PARAMETER_X, x);
            sharecart.x(Constraints.clampX(x));
        }

        short y = sharecart.y();
        if (!Constraints.validY(y)) {
            if (isStrict())
                throwConstraintException(SharecartFileConstants.PARAMETER_Y, y);
            sharecart.y(Constraints.clampY(y));
        }

        for (int i = 0; i < SharecartFileConstants.PARAMETER_MISC.length; i++) {
            int misc = sharecart.misc(i);
            if (!Constraints.validMisc(misc)) {
                if (isStrict())
                    throwConstraintException(SharecartFileConstants.PARAMETER_MISC[i], misc);
                sharecart.misc(i, Constraints.clampMisc(misc));
            }
        }

        String name = sharecart.name();
        if (!Constraints.validName(name)) {
            if (isStrict())
                throwConstraintException(SharecartFileConstants.PARAMETER_NAME, name);
            sharecart.name(Constraints.clampName(name));
        }
    }

    private short readShort() {
        return (short) readInt();
    }

    private int readInt() {
        String str = readString();
        if (str.length() == 0 || !StringUtils.isNumeric(str)) {
            if (isStrict()) {
                throw new SharecartFormatException("The string '%s' is not a valid integer", str);
            }
            return 0;
        }

        return Integer.valueOf(str);
    }

    private String readString() {
        String token = this.scanner.next();
        int firstIndex = token.indexOf(SharecartFileConstants.DELIMITER_VALUE);
        if (firstIndex < token.length() - 1) {
            String valueString = token.substring(firstIndex + 1).replaceAll("\r", "");
            if (valueString.startsWith("\"") && valueString.length() > 1) {
                valueString = valueString.substring(1);
            }
            if (valueString.endsWith("\"") && valueString.length() > 1) {
                valueString = valueString.substring(0, valueString.length() - 1);
            }
            return valueString;
        } else if (isStrict()) {
            throw new SharecartFormatException("'%s' is not a valid parameter definition", token);
        }
        return "";
    }

    private boolean readBoolean() {
        String value = readString();
        if (value.length() == 0) {
            if (isStrict()) {
                throw new SharecartFormatException("The string '%s' is not a valid boolean", value);
            }
            return false;
        }
        if (StringUtils.isNumeric(value)) {
            if (isStrict()) {
                throw new SharecartFormatException("The string '%s' is not a valid boolean", value);
            }
            return Integer.valueOf(value) != 0;
        }
        if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
            return Boolean.valueOf(value);
        } else if (isStrict()) {
            throw new SharecartFormatException("The string '%s' is not a valid boolean", value);
        }
        return false;
    }

    private void throwConstraintException(String parameterName, Object value) throws SharecartFormatException {
        throw new SharecartFormatException("The %s value '%s' does not fulfill the constraints " +
                "for the parameter", parameterName, value.toString());
    }

    @Override
    public void close() throws IOException {
        this.scanner.close();
    }

}
