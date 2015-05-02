package io.itch.frogcheese.sharecart;

import io.itch.frogcheese.sharecart.error.InvalidParameterException;
import io.itch.frogcheese.sharecart.error.ParameterNotAcessibleException;
import io.itch.frogcheese.sharecart.io.FileUtils;
import io.itch.frogcheese.sharecart.io.SharecartReader;
import io.itch.frogcheese.sharecart.io.SharecartWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class SharecartManager {

    private static final String TAG = SharecartManager.class.getSimpleName();

    private static final String DAT_DIRECTORY = "dat";
    private static final String SHARECART_FILE = "o_o.ini";
    private static final int DIRECTORY_LEVELS_TO_CHECK = 4;

    private static SharecartManager INSTANCE;

    public File datDirectory;
    public File shareCartFile;
    private Sharecart sharecart;
    private boolean valid = false;
    private boolean loaded = false;
    private boolean saved = false;

    public static SharecartManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SharecartManager();
        }
        return INSTANCE;
    }

    private SharecartManager() {
    }

    public boolean isValidCart() {
        for (int i = 0; i <= DIRECTORY_LEVELS_TO_CHECK; i++) {
            File file = FileUtils.fileAboveRunningLocation(i, DAT_DIRECTORY);
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

        // Couldn't find dat directory - create it
        return this.valid = createDatDirectory();
    }

    private boolean createDatDirectory() {
        for (int i = 0; i <= DIRECTORY_LEVELS_TO_CHECK; i++) {
            File dir = FileUtils.fileAboveLocation(i, FileUtils.getAppLocation(), "../");
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

    public boolean load() {
        if (!this.valid)
            throw new IllegalStateException(
                    "Cannot load file before isValidCart() has been called.");

        try {
            SharecartReader reader = new SharecartReader(this.shareCartFile);
            this.sharecart = reader.read();
            reader.close();

            return this.loaded = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            this.valid = false;
            this.loaded = false;
            this.sharecart = null;
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            this.valid = false;
            this.loaded = false;
            this.sharecart = null;
            return false;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            this.loaded = true;
            this.sharecart = Sharecart.withDefaults();
            save();
            return true;
        }
    }

    public boolean save() {
        if (!this.valid)
            throw new IllegalStateException(
                    "Cannot load file before isValidCart() has been called.");
        if (!this.loaded || this.sharecart == null)
            throw new IllegalStateException(
                    "Cannot save file before it has been loaded at least once.");

        try {
            SharecartWriter writer = new SharecartWriter(this.shareCartFile);
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

    public short x() {
        if (!isReadable())
            throw new ParameterNotAcessibleException("x");

        return this.sharecart.x();
    }

    public short y() {
        if (!isReadable())
            throw new ParameterNotAcessibleException("y");

        return this.sharecart.y();
    }

    public void x(short value) {
        if (!isWritable())
            throw new ParameterNotAcessibleException("x");
        if (!Constraints.validX(value))
            throw new InvalidParameterException("x", value);

        this.saved = false;
        this.sharecart.x(value);
    }

    public void y(short value) {
        if (!isWritable())
            throw new ParameterNotAcessibleException("y");
        if (!Constraints.validY(value))
            throw new InvalidParameterException("y", value);

        this.saved = false;
        this.sharecart.y(value);
    }

    public int misc(int index) {
        if (!isReadable())
            throw new ParameterNotAcessibleException("misc" + index);
        if (!Constraints.validMiscIndex(index))
            throw new IndexOutOfBoundsException("misc" + index);

        return this.sharecart.misc(index);
    }

    public void misc(int index, int value) {
        if (!isWritable())
            throw new ParameterNotAcessibleException("misc" + index);
        if (!Constraints.validMisc(value))
            throw new InvalidParameterException("misc" + index, value);
        if (!Constraints.validMiscIndex(index))
            throw new IndexOutOfBoundsException("misc" + index);

        this.saved = false;
        this.sharecart.misc(index, value);
    }

    public String name() {
        if (!isReadable())
            throw new ParameterNotAcessibleException("name");
        return this.sharecart.name();
    }

    public void name(String value) {
        if (!isWritable())
            throw new ParameterNotAcessibleException("name");
        if (!Constraints.validName(value))
            throw new InvalidParameterException("name", value);

        this.saved = false;
        this.sharecart.name(value);
    }

    public boolean switchValue(int index) {
        if (!isReadable())
            throw new ParameterNotAcessibleException("name");
        if (!Constraints.validSwitchIndex(index))
            throw new IndexOutOfBoundsException("switch" + index);
        return this.sharecart.switchValue(index);
    }

    public void switchValue(int index, boolean value) {
        if (!isWritable())
            throw new ParameterNotAcessibleException("switch" + index);
        if (!Constraints.validSwitchIndex(index))
            throw new IndexOutOfBoundsException("switch" + index);

        this.saved = false;
        this.sharecart.switchValue(index, value);
    }

    public boolean isLoaded() {
        return this.loaded;
    }

    public boolean hasUnsavedChanges() {
        return !this.saved;
    }

    private boolean isReadable() {
        return this.valid && (this.loaded || load());
    }

    private boolean isWritable() {
        return this.valid && (this.loaded || load());
    }
}
