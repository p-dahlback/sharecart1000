package io.itch.frogcheese.sharecart;

import java.io.File;

/**
 * Created by Peter on 15-05-23.
 */
class SharecartFile {

    private File file;
    private boolean autoCreated = false;

    public static SharecartFile fromFile(File file) {
        return new SharecartFile(file);
    }

    public static SharecartFile fromAutoCreatedFile(File file) {
        return new SharecartFile(file, true);
    }

    private SharecartFile(File file) {
        this.file = file;
    }

    private SharecartFile(File file, boolean autoCreated) {
        this.file = file;
        this.autoCreated = autoCreated;
    }

    public File getFile() {
        return this.file;
    }

    public void setIsAutoCreated(boolean autoCreated) {
        this.autoCreated = autoCreated;
    }

    public boolean isAutoCreated() {
        return this.autoCreated;
    }
}
