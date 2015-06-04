package io.itch.frogcheese.sharecart;

import java.io.File;

/**
 * Created by Peter on 15-05-23.
 */
class ShareCartFile {

    private File file;
    private boolean autoCreated = false;

    public static ShareCartFile fromFile(File file) {
        return new ShareCartFile(file);
    }

    public static ShareCartFile fromAutoCreatedFile(File file) {
        return new ShareCartFile(file, true);
    }

    private ShareCartFile(File file) {
        this(file, false);
    }

    private ShareCartFile(File file, boolean autoCreated) {
        if (file == null)
            throw new IllegalArgumentException("Cannot pass null as a file.");

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShareCartFile that = (ShareCartFile) o;

        return file.equals(that.file);

    }

    @Override
    public int hashCode() {
        return file.hashCode();
    }
}
