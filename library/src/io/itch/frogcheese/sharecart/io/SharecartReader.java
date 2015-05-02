package io.itch.frogcheese.sharecart.io;

import io.itch.frogcheese.sharecart.Constraints;
import io.itch.frogcheese.sharecart.Sharecart;
import org.apache.commons.lang3.StringUtils;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class SharecartReader implements Closeable {
    Scanner scanner;

    public SharecartReader(String path) throws FileNotFoundException {
        this(new File(path));
    }

    public SharecartReader(File sharecart) throws FileNotFoundException {
        this.scanner = new Scanner(sharecart)
                .useDelimiter(Constants.DELIMITER_PARAMETER_PATTERN);
        this.scanner.next();
    }

    public Sharecart read() {
        Sharecart ret = new Sharecart();

        ret.x(readShort());
        ret.y(readShort());
        for (int i = 0; i < Constants.PARAMETER_MISC.length; i++) {
            ret.misc(i, Constraints.clampMisc(readInt()));
        }
        ret.name(readString());
        for (int i = 0; i < Constants.PARAMETER_SWITCH.length; i++) {
            ret.switchValue(i, readBoolean());
        }

        return ret;
    }

    private short readShort() {
        return (short) readInt();
    }

    private int readInt() {
        String str = readString();
        if (str.length() == 0 || !StringUtils.isNumeric(str))
            return 0;
        return Integer.valueOf(str);
    }

    private String readString() {
        String token = this.scanner.next();
        int firstIndex = token.indexOf(Constants.DELIMITER_VALUE);
        if (firstIndex < token.length() - 1) {
            String valueString = token.substring(firstIndex + 1).replaceAll("\r", "");
            if (valueString.startsWith("\"") && valueString.length() > 1) {
                valueString = valueString.substring(1);
            }
            if (valueString.endsWith("\"") && valueString.length() > 1) {
                valueString = valueString.substring(0, valueString.length() - 1);
            }
            return valueString;
        }
        return "";
    }

    private boolean readBoolean() {
        String value = readString();
        if (value.length() == 0)
            return false;
        if (StringUtils.isNumeric(value))
            return Integer.valueOf(value) != 0;
        if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false"))
            return Boolean.valueOf(value);
        return false;
    }

    @Override
    public void close() throws IOException {
        this.scanner.close();
    }

}
