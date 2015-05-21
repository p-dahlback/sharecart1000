package io.itch.frogcheese.sharecart;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Handles writing to a sharecart file.
 */
class SharecartFileWriter implements Closeable {

    private PrintWriter writer;

    /**
     * Constructor.
     *
     * @param path path to the file that should be written to.
     * @throws FileNotFoundException
     */
    public SharecartFileWriter(String path) throws FileNotFoundException {
        this(new File(path));
    }

    /**
     * Constructor.
     *
     * @param file the file that should be written to.
     * @throws FileNotFoundException
     */
    public SharecartFileWriter(File file) throws FileNotFoundException {
        this.writer = new PrintWriter(file);
    }

    /**
     * Writes the contents of the Sharecart to file.
     *
     * @param sharecart container for the Sharecart file contents. All of the parameters in this object will be
     *                  committed to the file.
     */
    public void write(Sharecart sharecart) {
        StringBuilder file = new StringBuilder(SharecartFileConstants.TITLE);
        file.append(SharecartFileConstants.DELIMITER_PARAMETER);
        file.append(SharecartFileConstants.PARAMETER_X).append(SharecartFileConstants.DELIMITER_VALUE).append(shortString(sharecart.x()));
        file.append(SharecartFileConstants.DELIMITER_PARAMETER);
        file.append(SharecartFileConstants.PARAMETER_Y).append(SharecartFileConstants.DELIMITER_VALUE).append(shortString(sharecart.y()));
        file.append(SharecartFileConstants.DELIMITER_PARAMETER);
        for (int i = 0; i < SharecartFileConstants.PARAMETER_MISC.length; i++) {
            file.append(SharecartFileConstants.PARAMETER_MISC[i]).append(SharecartFileConstants.DELIMITER_VALUE).append(intString(sharecart.misc(i)));
            file.append(SharecartFileConstants.DELIMITER_PARAMETER);
        }
        file.append(SharecartFileConstants.PARAMETER_NAME).append(SharecartFileConstants.DELIMITER_VALUE).append(sharecart.name());
        file.append(SharecartFileConstants.DELIMITER_PARAMETER);
        for (int i = 0; i < SharecartFileConstants.PARAMETER_SWITCH.length; i++) {
            file.append(SharecartFileConstants.PARAMETER_SWITCH[i]).append(SharecartFileConstants.DELIMITER_VALUE).append(booleanString(sharecart.switchValue(i)));
            file.append(SharecartFileConstants.DELIMITER_PARAMETER);
        }


        this.writer.write(file.toString());
    }

    private String shortString(short value) {
        return String.valueOf(value);
    }

    private String intString(int value) {
        return String.valueOf(value);
    }

    private String booleanString(boolean value) {
        return String.valueOf(value).toUpperCase();
    }

    @Override
    public void close() throws IOException {
        this.writer.close();
    }

}
