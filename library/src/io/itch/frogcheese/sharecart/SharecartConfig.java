package io.itch.frogcheese.sharecart;

/**
 * Configuration for the {@link SharecartManager}.
 */
public class SharecartConfig {

    private boolean createSharecartIfNotExists = false;
    private int directoryLevelsToCheck = 4;
    private boolean clampToConstraints = false;
    private boolean strictFileMode = false;
    private String applicationPath;

    /**
     * Utility class for creating a SharecartConfig instance.
     */
    public static class Builder {
        SharecartConfig config;

        public Builder() {
            config = new SharecartConfig();
        }

        /**
         * Sets whether or not the sharecart file will be created if it is missing.
         *
         * @param create if true, a new sharecart file with the correct structure will be created if one isn't found.
         * @return This Builder instance.
         */
        public Builder setCreateIfNotExists(boolean create) {
            config.createSharecartIfNotExists = create;
            return this;
        }

        /**
         * Sets whether the sharecart should assign default values or throw an exception when there are errors
         * in the sharecart file.
         *
         * @param strict if true, parameters will get default values if there are errors in the file.
         *               If false, exceptions will be thrown on encountered errors.
         * @return This Builder instance.
         */
        public Builder setStrictFileMode(boolean strict) {
            config.strictFileMode = strict;
            return this;
        }

        /**
         * Sets the amount of directories above the application path to check for the existence of a sharecart file.
         *
         * @param levels the amount of levels to traverse upward when searching for the sharecart file.
         * @return This Builder instance.
         */
        public Builder setDirectoryLevelsToCheck(int levels) {
            config.directoryLevelsToCheck = levels;
            return this;
        }

        /**
         * Sets whether or not the sharecart parameters will automatically be clamped to within constraints.
         *
         * @param clampToConstraints
         * @return This Builder instance.
         */
        public Builder setClampToConstraints(boolean clampToConstraints) {
            config.clampToConstraints = clampToConstraints;
            return this;
        }

        /**
         * Sets the file system path to the running application. This will be used when searching for the sharecart file.
         * This can also be assigned through the System property "application.dir".
         *
         * @param path the path to the application package.
         * @return This Builder instance.
         */
        public Builder setApplicationPath(String path) {
            config.applicationPath = path;
            return this;
        }


        /**
         * Builds the SharecartConfig instance.
         *
         * @return A new Sharecart configuration instance with the assigned parameters.
         */
        public SharecartConfig build() {
            return new SharecartConfig(config);
        }

    }

    private SharecartConfig() {

    }

    private SharecartConfig(SharecartConfig other) {
        createSharecartIfNotExists = other.createSharecartIfNotExists;
        directoryLevelsToCheck = other.directoryLevelsToCheck;
        clampToConstraints = other.clampToConstraints;
        strictFileMode = other.strictFileMode;
        applicationPath = other.applicationPath;

        if (applicationPath == null) {
            applicationPath = SharecartFileUtils.getApplicationLocation();
        }
    }

    /**
     * @return Whether or not the sharecart file will be created if it is missing.
     */
    public boolean willCreateIfNotExists() {
        return createSharecartIfNotExists;
    }

    /**
     * @return The amount of directories above the running path to check for the existence of a sharecart file.
     */
    public int getDirectoryLevelsToCheck() {
        return directoryLevelsToCheck;
    }

    /**
     * @return Whether or not the sharecart parameters will automatically be clamped to be within constraints.
     */
    public boolean willClampToConstraints() {
        return clampToConstraints;
    }

    /**
     * @return Whether default values will be assigned when encountering errors in the sharecart file,
     * or exceptions will be thrown. If true, exceptions will be thrown for faulty files.
     */
    public boolean isStrictFileMode() {
        return strictFileMode;
    }

    /**
     * @return The absolute path of the running application.
     */
    public String getApplicationPath() {
        return applicationPath;
    }

}
