package io.itch.frogcheese.sharecart;

import java.io.*;

import static io.itch.frogcheese.sharecart.ShareCartFileConstants.*;

/**
 * Handles writing to a sharecart file.
 */
class ShareCartFileWriter implements Closeable {

    private PrintWriter writer;

    /**
     * Constructor.
     *
     * @param file the file that should be written to.
     * @throws FileNotFoundException
     */
    public ShareCartFileWriter(File file) throws FileNotFoundException {
        if (file == null)
            throw new IllegalArgumentException("File cannot be null");
        if (!file.exists() || !file.isFile())
            // This manual FileNotFoundException is here because
            // PrintWriter creates the file instead of throwing an exception.
            throw new FileNotFoundException(file.getAbsolutePath() + " could not be found");

        this.writer = new PrintWriter(file);
    }

    /**
     * Writes the contents of the ShareCart to file.
     *
     * @param shareCart container for the ShareCart file contents. All of the parameters in this object will be
     *                  committed to the file.
     */
    public void write(ShareCart shareCart) {

        StringBuilder file = new StringBuilder(TITLE);

        file.append(DELIMITER_PARAMETER);

        file.append(PARAMETER_X)
                .append(DELIMITER_VALUE)
                .append(intString(shareCart.x()));

        file.append(DELIMITER_PARAMETER);

        file.append(PARAMETER_Y)
                .append(DELIMITER_VALUE)
                .append(intString(shareCart.y()));

        file.append(DELIMITER_PARAMETER);

        for (int i = 0; i < PARAMETER_MISC.length; i++) {
            file.append(PARAMETER_MISC[i])
                    .append(DELIMITER_VALUE)
                    .append(intString(shareCart.misc(i)));

            file.append(DELIMITER_PARAMETER);
        }
        file.append(PARAMETER_NAME)
                .append(DELIMITER_VALUE)
                .append(shareCart.name());

        file.append(DELIMITER_PARAMETER);

        for (int i = 0; i < PARAMETER_SWITCH.length; i++) {
            file.append(PARAMETER_SWITCH[i])
                    .append(DELIMITER_VALUE)
                    .append(booleanString(shareCart.switchValue(i)));

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
