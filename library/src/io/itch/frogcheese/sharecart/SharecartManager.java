package io.itch.frogcheese.sharecart;

import io.itch.frogcheese.sharecart.error.InvalidParameterException;
import io.itch.frogcheese.sharecart.error.ParameterNotAccessibleException;
import io.itch.frogcheese.sharecart.error.ShareCartException;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Manager for the shareCart file. Handles read/write operations and shareCart changes.
 */
public class ShareCartManager {

    private static ShareCartManager INSTANCE;

    // Resources
    private ShareCartFileInterface fileInterface;
    
    // File
    private ShareCartFile shareCartFile;
    private ShareCart shareCart;

    // State
    private ShareCartConfig config;

    private boolean searchedForFile = false;
    private boolean valid = false;
    private boolean loaded = false;
    private boolean saved = true;

    /**
     * Initializes the manager with the given configuration.
     *
     * @param config configuration for how the shareCart file should be handled.
     */
    public static void initialize(ShareCartConfig config) {
        INSTANCE = new ShareCartManager(config);
    }

    /**
     * @return The singleton instance. If {@link #initialize(ShareCartConfig)} has not been called, an instance will be
     * created with a default configuration.
     */
    public static ShareCartManager get() {
        if (INSTANCE == null) {
            initialize(new ShareCartConfig.Builder().build());
        }
        return INSTANCE;
    }

    private ShareCartManager(ShareCartConfig config) {
        this.config = config;
        fileInterface = ShareCartFileInterface.get();
    }

    /**
     * Checks to see if there is a valid shareCart file. If there is not, it may create a shareCart file if configured
     * to in {@link ShareCartConfig}.
     *
     * @return {@code true} if there was a valid file, or if the file was successfully created. {@code false} otherwise.
     */
    public boolean findSharecartFile() {
        if (config.willAutoCreateFile()) {
            shareCartFile = fileInterface.findOrCreateIniFile(config.getDirectoryLevelsToCheck(),
                    config.getApplicationPath());
        } else {
            shareCartFile = fileInterface.findIniFile(config.getDirectoryLevelsToCheck(), config.getApplicationPath());
        }

        valid = shareCartFile != null;
        searchedForFile = true;
        return valid;
    }

    /**
     * Loads the contents of a valid shareCart file. This will perform IO operations on the current thread. This method
     * cannot be called before a successful call to {@link #findSharecartFile()}.
     *
     * @return {@code true} if the contents of the file was successfully loaded. {@code false} otherwise.
     *
     * @throws IllegalStateException if {@link #findSharecartFile()} hasn't been successfully called
     * @throws ShareCartException    if an unhandled error occurs when reading the shareCart. This will only be thrown
     *                               if {@link ShareCartConfig#isStrictFileReadMode()} is false.
     */
    public boolean load() {
        if (!valid)
            throw new IllegalStateException("Cannot load file before findSharecartFile() has been called.");

        if (shareCartFile.isAutoCreated()) {
            loaded = true;
            shareCart = ShareCart.withDefaults();
            save();

            // Clear auto created flag since the file has been properly initialized now
            shareCartFile.setIsAutoCreated(false);
            return true;
        }

        try {
            ShareCartFileReader reader = fileInterface.getNewSharecartFileReader(shareCartFile);
            reader.setIsStrict(config.isStrictFileReadMode());
            shareCart = reader.read();
            reader.close();

            saved = true;
            return loaded = true;

        } catch (IOException e) {
            e.printStackTrace();
            valid = false;
            loaded = false;
            shareCart = null;
            if (config.isStrictFileReadMode())
                throw new ShareCartException(e);
            return false;

        } catch (ShareCartException e) {
            if (config.isStrictFileReadMode()) {
                throw e;
            } else {
                e.printStackTrace();
                loaded = true;
                shareCart = ShareCart.withDefaults();
                save();
            }
            return true;
        }
    }

    /**
     * Saves changes to the shareCart file. This will perform IO operations on the current thread.
     *
     * @return {@code true} if the changes could be saved.
     *
     * @throws IllegalStateException if {@link #findSharecartFile()} or {@link #load()} have not been called.
     */
    public boolean save() {
        if (!valid)
            throw new IllegalStateException(
                    "Cannot load file before findSharecartFile() has been called.");
        if (!loaded || shareCart == null)
            throw new IllegalStateException(
                    "Cannot save file before it has been loaded at least once.");

        try {
            ShareCartFileWriter writer = fileInterface.getNewSharecartFileWriter(shareCartFile);
            writer.write(shareCart);
            writer.close();

            return saved = true;
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
     *
     * @throws ParameterNotAccessibleException if the shareCart wasn't initialized.
     */
    public int x() {
        if (!isReadable())
            throw new ParameterNotAccessibleException("x");

        return shareCart.x();
    }

    /**
     * @return the current value of the Y parameter.
     *
     * @throws ParameterNotAccessibleException if the shareCart wasn't initialized.
     */
    public int y() {
        if (!isReadable())
            throw new ParameterNotAccessibleException("y");

        return shareCart.y();
    }

    /**
     * Sets the x parameter to the given value.
     *
     * @param value x value. Must be between zero and {@link Constraints#MAX_SIZE_X}.
     *
     * @throws ParameterNotAccessibleException if the shareCart wasn't initialized.
     * @throws InvalidParameterException       if the value did not fit the constraints.
     */
    public void x(int value) {
        if (!isWritable())
            throw new ParameterNotAccessibleException("x");

        if (config.willClampToConstraints()) {
            value = Constraints.clampX(value);
        } else if (!Constraints.validX(value)) {
            throw new InvalidParameterException("x", value);
        }

        saved = false;
        shareCart.x(value);
    }

    /**
     * Sets the y parameter to the given value.
     *
     * @param value y value. Must be between zero and {@link Constraints#MAX_SIZE_Y}.
     *
     * @throws ParameterNotAccessibleException if the shareCart wasn't initialized.
     * @throws InvalidParameterException       if the value did not fit the constraints.
     */
    public void y(int value) {
        if (!isWritable())
            throw new ParameterNotAccessibleException("y");

        if (config.willClampToConstraints()) {
            value = Constraints.clampY(value);
        } else if (!Constraints.validY(value)) {
            throw new InvalidParameterException("y", value);
        }

        saved = false;
        shareCart.y(value);
    }

    /**
     * @param index the index of the misc value. Must be at least zero and less than {@link
     *              Constraints#MISC_ITEMS_LENGTH}
     *
     * @return the current value for the misc parameter with the given index.
     *
     * @throws ParameterNotAccessibleException if the shareCart wasn't initialized.
     * @throws IndexOutOfBoundsException       if the index is less than zero or greater or equal to {@link
     *                                         Constraints#MISC_ITEMS_LENGTH}.
     */
    public int misc(int index) {
        if (!isReadable())
            throw new ParameterNotAccessibleException("misc" + index);
        if (!Constraints.validMiscIndex(index))
            throw new IndexOutOfBoundsException("misc" + index);

        return shareCart.misc(index);
    }

    /**
     * Sets the misc parameter with the given index to the provided value.
     *
     * @param index the index of the misc value. Must be at least zero and less than {@link
     *              Constraints#MISC_ITEMS_LENGTH}
     * @param value misc value. Must be between zero and {@link Constraints#MAX_SIZE_MISC}.
     *
     * @throws ParameterNotAccessibleException if the shareCart wasn't initialized.
     * @throws InvalidParameterException       if the value did not fit the constraints.
     * @throws IndexOutOfBoundsException       if the index is less than zero or greater or equal to {@link
     *                                         Constraints#MISC_ITEMS_LENGTH}.
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

        saved = false;
        shareCart.misc(index, value);
    }

    /**
     * @return the current value of the name parameter.
     *
     * @throws ParameterNotAccessibleException if the shareCart wasn't initialized.
     */
    public String name() {
        if (!isReadable())
            throw new ParameterNotAccessibleException("name");
        return shareCart.name();
    }

    /**
     * Sets the name parameter to the provided value.
     *
     * @param value name value. Must be shorter than {@link Constraints#MAX_SIZE_NAME_LENGTH}.
     *
     * @throws ParameterNotAccessibleException if the shareCart wasn't initialized.
     */
    public void name(String value) {
        if (!isWritable())
            throw new ParameterNotAccessibleException("name");

        if (config.willClampToConstraints()) {
            value = Constraints.clampName(value);
        } else if (!Constraints.validName(value)) {
            throw new InvalidParameterException("name", value);
        }

        saved = false;
        shareCart.name(value);
    }

    /**
     * @param index the index of the switch value. Must be at least zero and less than {@link
     *              Constraints#SWITCH_ITEMS_LENGTH}
     *
     * @return the current value for the switch parameter with the given index.
     *
     * @throws ParameterNotAccessibleException if the shareCart wasn't initialized.
     * @throws IndexOutOfBoundsException       if the index is less than zero or greater or equal to {@link
     *                                         Constraints#SWITCH_ITEMS_LENGTH}.
     */
    public boolean switchValue(int index) {
        if (!isReadable())
            throw new ParameterNotAccessibleException("name");
        if (!Constraints.validSwitchIndex(index))
            throw new IndexOutOfBoundsException("switch" + index);
        return shareCart.switchValue(index);
    }

    /**
     * Sets the switch parameter with the given index to the provided value.
     *
     * @param index the index of the switch value. Must be at least zero and less than {@link
     *              Constraints#SWITCH_ITEMS_LENGTH}
     * @param value switch value.
     *
     * @throws ParameterNotAccessibleException if the shareCart wasn't initialized.
     * @throws IndexOutOfBoundsException       if the index is less than zero or greater or equal to {@link
     *                                         Constraints#SWITCH_ITEMS_LENGTH}.
     */
    public void switchValue(int index, boolean value) {
        if (!isWritable())
            throw new ParameterNotAccessibleException("switch" + index);
        if (!Constraints.validSwitchIndex(index))
            throw new IndexOutOfBoundsException("switch" + index);

        saved = false;
        shareCart.switchValue(index, value);
    }

    /**
     * @return Whether or not a valid shareCart file has been found. This will call {@link #findSharecartFile()} if it
     * hasn't been called yet.
     */
    public boolean isValidSharecartFile() {
        if (!searchedForFile) {
            findSharecartFile();
        }
        return valid;
    }

    /**
     * @return Whether or not the shareCart has been successfully loaded.
     */
    public boolean isLoaded() {
        return loaded;
    }

    /**
     * @return Whether or not changes have been made to the shareCart that haven't been saved yet.
     */
    public boolean hasUnsavedChanges() {
        return !saved;
    }

    ShareCartConfig getConfig() {
        return config;
    }

    private boolean isReadable() {
        return valid && loaded;
    }

    private boolean isWritable() {
        return valid && loaded;
    }
}
