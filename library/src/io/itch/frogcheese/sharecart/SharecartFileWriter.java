package io.itch.frogcheese.sharecart;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import static io.itch.frogcheese.sharecart.SharecartFileConstants.*;

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

        StringBuilder file = new StringBuilder(TITLE);

        file.append(DELIMITER_PARAMETER);

        file.append(PARAMETER_X)
                .append(DELIMITER_VALUE)
                .append(intString(sharecart.x()));

        file.append(DELIMITER_PARAMETER);

        file.append(PARAMETER_Y)
                .append(DELIMITER_VALUE)
                .append(intString(sharecart.y()));

        file.append(DELIMITER_PARAMETER);

        for (int i = 0; i < PARAMETER_MISC.length; i++) {
            file.append(PARAMETER_MISC[i])
                    .append(DELIMITER_VALUE)
                    .append(intString(sharecart.misc(i)));

            file.append(DELIMITER_PARAMETER);
        }
        file.append(PARAMETER_NAME)
                .append(DELIMITER_VALUE)
                .append(sharecart.name());

        file.append(DELIMITER_PARAMETER);

        for (int i = 0; i < PARAMETER_SWITCH.length; i++) {
            file.append(PARAMETER_SWITCH[i])
                    .append(DELIMITER_VALUE)
                    .append(booleanString(sharecart.switchValue(i)));

            file.append(DELIMITER_PARAMETER);
        }


        this.writer.write(file.toString());
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
