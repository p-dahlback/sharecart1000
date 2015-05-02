package io.itch.frogcheese.sharecart;

/**
 * Configuration for the {@link SharecartManager}.
 */
public class SharecartConfig {

    private boolean createSharecartIfNotExists = false;
    private int directoryLevelsToCheck = 4;
    private boolean clampToConstraints = false;
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
         * @param create
         * @return
         */
        public Builder setCreateIfNotExists(boolean create) {
            config.createSharecartIfNotExists = create;
            return this;
        }

        /**
         * Sets the amount of directories above the running path to check for the existence of a sharecart file.
         *
         * @param levels
         * @return
         */
        public Builder setDirectoryLevelsToCheck(int levels) {
            config.directoryLevelsToCheck = levels;
            return this;
        }

        /**
         * Sets whether or not the sharecart parameters will automatically be clamped to within constraints.
         *
         * @param clampToConstraints
         * @return
         */
        public Builder setClampToConstraints(boolean clampToConstraints) {
            config.clampToConstraints = clampToConstraints;
            return this;
        }

        /**
         * Sets the file system path to the running application. This will be used when searching for the sharecart file.
         *
         * @param path the path that will represent the location of the application.
         * @return
         */
        public Builder setApplicationPath(String path) {
            config.applicationPath = path;
            return this;
        }

        /**
         * Builds the SharecartConfig instance.
         *
         * @return
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
        applicationPath = other.applicationPath;

        if (applicationPath == null) {
            applicationPath = SharecartFileUtils.getAppLocation();
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
     * @return The absolute path of the running application.
     */
    public String getApplicationPath() {
        return applicationPath;
    }

}
