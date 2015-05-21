package io.itch.frogcheese.sharecart;

import io.itch.frogcheese.sharecart.error.InvalidParameterException;
import io.itch.frogcheese.sharecart.error.ParameterNotAccessibleException;
import io.itch.frogcheese.sharecart.error.SharecartException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Manager for the sharecart file. Handles read/write operations and sharecart changes.
 */
public class SharecartManager {

    private static final String DAT_DIRECTORY = "dat";
    private static final String SHARECART_FILE = "o_o.ini";

    private static SharecartManager INSTANCE;

    public File datDirectory;
    public File shareCartFile;

    private SharecartConfig config;

    private Sharecart sharecart;
    private boolean valid = false;
    private boolean loaded = false;
    private boolean saved = false;

    /**
     * Initializes the manager with the given configuration.
     *
     * @param config configuration for how the sharecart file should be handled.
     */
    public static void initialize(SharecartConfig config) {
        INSTANCE = new SharecartManager(config);
    }

    /**
     * @return The singleton instance.
     * @throws IllegalStateException if a call to {@link #initialize(SharecartConfig)} has not been made.
     */
    public static SharecartManager get() {
        if (INSTANCE == null) {
            throw new IllegalStateException("SharecartManager uninitialized. " +
                    "Please make a call to 'initialize' before using the SharecartManager.");
        }
        return INSTANCE;
    }

    private SharecartManager(SharecartConfig config) {
        this.config = config;
    }

    /**
     * Checks to see if there is a valid sharecart file. If there is not,
     * it will create a sharecart file if configured in {@link SharecartConfig}.
     *
     * @return {@code true} if there was a valid file, or if the file was successfully created. {@code false} otherwise.
     */
    public boolean isValidCart() {
        for (int i = 0; i <= config.getDirectoryLevelsToCheck(); i++) {
            File file = SharecartFileUtils.fileAboveRunningLocation(i, DAT_DIRECTORY);
            if (file.exists()) {
                this.datDirectory = file;
                file = new File(this.datDirectory, SHARECART_FILE);
                if (file.exists()) {
                    this.shareCartFile = file;
                    return this.valid = true;
                } else if (createIniFile())
                    return this.valid = true;
            }
        }

        return config.willCreateIfNotExists() && (this.valid = createDatDirectory());
    }

    private boolean createDatDirectory() {
        for (int i = 0; i <= config.getDirectoryLevelsToCheck(); i++) {
            File dir = SharecartFileUtils.fileAboveLocation(i, config.getApplicationPath(), "../");
            if (dir.exists() && dir.isDirectory()) {
                File dat = new File(dir, DAT_DIRECTORY);
                if (dat.mkdirs()) {
                    this.datDirectory = dat;
                    return createIniFile();
                }
            }
        }
        return false;
    }

    private boolean createIniFile() {
        File file = new File(this.datDirectory, SHARECART_FILE);
        if (file.exists()) {
            this.shareCartFile = file;
            return this.valid = true;
        } else {
            try {
                if (file.createNewFile()) {
                    this.valid = true;
                    this.loaded = true;
                    this.shareCartFile = file;
                    this.sharecart = Sharecart.withDefaults();
                    save();
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return this.valid = false;
        }
    }

    /**
     * Loads the contents of a valid sharecart file. This will perform IO operations on the current thread.
     * This method cannot be called before a successful call to {@link #isValidCart()}.
     *
     * @return {@code true} if the contents of the file was successfully loaded. {@code false} otherwise.
     * @throws IllegalStateException if {@link #isValidCart()} hasn't been successfully called
     * @throws SharecartException    if an unhandled error occurs when reading the sharecart. This will only be thrown if
     *                               {@link SharecartConfig#isStrictFileMode()} is false.
     */
    public boolean load() {
        if (!this.valid)
            throw new IllegalStateException("Cannot load file before isValidCart() has been called.");

        try {
            SharecartFileReader reader = new SharecartFileReader(this.shareCartFile);
            reader.setIsStrict(config.isStrictFileMode());
            this.sharecart = reader.read();
            reader.close();

            return this.loaded = true;

        } catch (IOException e) {
            e.printStackTrace();
            this.valid = false;
            this.loaded = false;
            this.sharecart = null;
            if (config.isStrictFileMode())
                throw new SharecartException(e);
            return false;

        } catch (SharecartException e) {
            if (config.isStrictFileMode()) {
                throw e;
            } else {
                e.printStackTrace();
                this.loaded = true;
                this.sharecart = Sharecart.withDefaults();
                save();
            }
            return true;
        }
    }

    /**
     * Saves changes to the sharecart file. This will perform IO operations on the current thread.
     *
     * @return {@code true} if the changes could be saved.
     * @throws IllegalStateException if {@link #isValidCart()} or {@link #load()} have not been called.
     */
    public boolean save() {
        if (!this.valid)
            throw new IllegalStateException(
                    "Cannot load file before isValidCart() has been called.");
        if (!this.loaded || this.sharecart == null)
            throw new IllegalStateException(
                    "Cannot save file before it has been loaded at least once.");

        try {
            SharecartFileWriter writer = new SharecartFileWriter(this.shareCartFile);
            writer.write(this.sharecart);
            writer.close();

            return this.saved = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();

            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * @return the current value of the X parameter.
     * @throws ParameterNotAccessibleException if the sharecart wasn't initialized.
     */
    public short x() {
        if (!isReadable())
            throw new ParameterNotAccessibleException("x");

        return this.sharecart.x();
    }

    /**
     * @return the current value of the Y parameter.
     * @throws ParameterNotAccessibleException if the sharecart wasn't initialized.
     */
    public short y() {
        if (!isReadable())
            throw new ParameterNotAccessibleException("y");

        return this.sharecart.y();
    }

    /**
     * Sets the x parameter to the given value.
     *
     * @param value x value. Must be between zero and {@link Constraints#MAX_SIZE_X}.
     * @throws ParameterNotAccessibleException if the sharecart wasn't initialized.
     * @throws InvalidParameterException       if the value did not fit the constraints.
     */
    public void x(short value) {
        if (!isWritable())
            throw new ParameterNotAccessibleException("x");

        if (config.willClampToConstraints()) {
            value = Constraints.clampX(value);
        } else if (!Constraints.validX(value)) {
            throw new InvalidParameterException("x", value);
        }

        this.saved = false;
        this.sharecart.x(value);
    }

    /**
     * Sets the y parameter to the given value.
     *
     * @param value y value. Must be between zero and {@link Constraints#MAX_SIZE_Y}.
     * @throws ParameterNotAccessibleException if the sharecart wasn't initialized.
     * @throws InvalidParameterException       if the value did not fit the constraints.
     */
    public void y(short value) {
        if (!isWritable())
            throw new ParameterNotAccessibleException("y");

        if (config.willClampToConstraints()) {
            value = Constraints.clampY(value);
        } else if (!Constraints.validY(value)) {
            throw new InvalidParameterException("y", value);
        }

        this.saved = false;
        this.sharecart.y(value);
    }

    /**
     * @param index the index of the misc value. Must be at least zero and less than {@link Constraints#MISC_ITEMS_LENGTH}
     * @return the current value for the misc parameter with the given index.
     * @throws ParameterNotAccessibleException if the sharecart wasn't initialized.
     * @throws IndexOutOfBoundsException       if the index is less than zero or greater or equal to {@link Constraints#MISC_ITEMS_LENGTH}.
     */
    public int misc(int index) {
        if (!isReadable())
            throw new ParameterNotAccessibleException("misc" + index);
        if (!Constraints.validMiscIndex(index))
            throw new IndexOutOfBoundsException("misc" + index);

        return this.sharecart.misc(index);
    }

    /**
     * Sets the misc parameter with the given index to the provided value.
     *
     * @param index the index of the misc value. Must be at least zero and less than {@link Constraints#MISC_ITEMS_LENGTH}
     * @param value misc value. Must be between zero and {@link Constraints#MAX_SIZE_MISC}.
     * @throws ParameterNotAccessibleException if the sharecart wasn't initialized.
     * @throws InvalidParameterException       if the value did not fit the constraints.
     * @throws IndexOutOfBoundsException       if the index is less than zero or greater or equal to {@link Constraints#MISC_ITEMS_LENGTH}.
     */
    public void misc(int index, int value) {
        if (!isWritable())
            throw new ParameterNotAccessibleException("misc" + index);

        if (config.willClampToConstraints()) {
            value = Constraints.clampMisc(value);
        } else if (!Constraints.validMisc(value)) {
            throw new InvalidParameterException("misc" + index, value);
        }

        if (!Constraints.validMiscIndex(index))
            throw new IndexOutOfBoundsException("misc" + index);

        this.saved = false;
        this.sharecart.misc(index, value);
    }

    /**
     * @return the current value of the name parameter.
     * @throws ParameterNotAccessibleException if the sharecart wasn't initialized.
     */
    public String name() {
        if (!isReadable())
            throw new ParameterNotAccessibleException("name");
        return this.sharecart.name();
    }

    /**
     * Sets the name parameter to the provided value.
     *
     * @param value name value. Must be shorter than {@link Constraints#MAX_SIZE_NAME_LENGTH}.
     * @throws ParameterNotAccessibleException if the sharecart wasn't initialized.
     */
    public void name(String value) {
        if (!isWritable())
            throw new ParameterNotAccessibleException("name");

        if (config.willClampToConstraints()) {
            value = Constraints.clampName(value);
        } else if (!Constraints.validName(value)) {
            throw new InvalidParameterException("name", value);
        }

        this.saved = false;
        this.sharecart.name(value);
    }

    /**
     * @param index the index of the switch value. Must be at least zero and less than {@link Constraints#SWITCH_ITEMS_LENGTH}
     * @return the current value for the switch parameter with the given index.
     * @throws ParameterNotAccessibleException if the sharecart wasn't initialized.
     * @throws IndexOutOfBoundsException       if the index is less than zero or greater or equal to {@link Constraints#SWITCH_ITEMS_LENGTH}.
     */
    public boolean switchValue(int index) {
        if (!isReadable())
            throw new ParameterNotAccessibleException("name");
        if (!Constraints.validSwitchIndex(index))
            throw new IndexOutOfBoundsException("switch" + index);
        return this.sharecart.switchValue(index);
    }

    /**
     * Sets the switch parameter with the given index to the provided value.
     *
     * @param index the index of the switch value. Must be at least zero and less than {@link Constraints#SWITCH_ITEMS_LENGTH}
     * @param value switch value.
     * @throws ParameterNotAccessibleException if the sharecart wasn't initialized.
     * @throws IndexOutOfBoundsException       if the index is less than zero or greater or equal to {@link Constraints#SWITCH_ITEMS_LENGTH}.
     */
    public void switchValue(int index, boolean value) {
        if (!isWritable())
            throw new ParameterNotAccessibleException("switch" + index);
        if (!Constraints.validSwitchIndex(index))
            throw new IndexOutOfBoundsException("switch" + index);

        this.saved = false;
        this.sharecart.switchValue(index, value);
    }

    /**
     * @return Whether or not the sharecart has been successfully loaded.
     */
    public boolean isLoaded() {
        return this.loaded;
    }

    /**
     * @return Whether or not changes have been made to the sharecart that haven't been saved yet.
     */
    public boolean hasUnsavedChanges() {
        return !this.saved;
    }

    private boolean isReadable() {
        return this.valid && this.loaded;
    }

    private boolean isWritable() {
        return this.valid && this.loaded;
    }
}
