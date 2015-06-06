package io.itch.frogcheese.sharecart;

/**
 * Configuration for the {@link ShareCartManager}.
 */
public class ShareCartConfig {

    static final int DEFAULT_LEVELS_TO_CHECK = 4;

    private boolean createSharecartIfNotExists = false;
    private boolean clampToConstraints = false;
    private boolean strictFileMode = false;

    private int directoryLevelsToCheck = DEFAULT_LEVELS_TO_CHECK;
    private String applicationPath;

    /**
     * Utility class for creating a ShareCartConfig instance.
     */
    public static class Builder {
        final ShareCartConfig config;

        public Builder() {
            config = new ShareCartConfig();
        }

        /**
         * Sets whether or not the sharecart file will be created if it is missing.
         *
         * @param create if true, a new sharecart file with the correct structure will be created if one isn't found.
         *
         * @return This Builder instance.
         */
        public Builder setAutoCreateFile(boolean create) {
            config.createSharecartIfNotExists = create;
            return this;
        }

        /**
         * Sets whether to assign default values or throw an exception when encountering errors
         * in the sharecart file.
         *
         * @param strict if {@code true}, parameters will get default values if there are errors in the file.
         *               If {@code false}, a ShareCartFormatException will be thrown when encountering an error.
         *
         * @return This Builder instance.
         *
         * @see io.itch.frogcheese.sharecart.error.ShareCartFormatException
         */
        public Builder setStrictFileReadMode(boolean strict) {
            config.strictFileMode = strict;
            return this;
        }

        /**
         * Sets the amount of directories above the application path to check for the existence of a sharecart file.
         *
         * @param levels the amount of levels to traverse upward when searching for the sharecart file.
         *
         * @return This Builder instance.
         */
        public Builder setDirectoryLevelsToCheck(int levels) {
            if (levels < 0) {
                throw new IllegalArgumentException("Levels to check cannot be a negative number. Number was " + levels);
            }
            config.directoryLevelsToCheck = levels;
            return this;
        }

        /**
         * Sets whether or not the sharecart parameters will automatically be clamped to within constraints.
         *
         * @param clampToConstraints if {@code true}, any values that are not within the minimum and maximum value
         *                           for that parameter will be automatically set to either the maximum or the minimum
         *                           - whichever is the closest.
         *                           <br/><br/>
         *                           if {@code false}, trying to set a value that does not fulfill these constraints causes
         *                           an InvalidParameterException to be thrown.
         *
         * @return This Builder instance.
         *
         * @see Constraints
         * @see io.itch.frogcheese.sharecart.error.InvalidParameterException
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
         *
         * @return This Builder instance.
         */
        public Builder setApplicationPath(String path) {
            config.applicationPath = path;
            return this;
        }


        /**
         * Builds the ShareCartConfig instance.
         *
         * @return A new ShareCart configuration instance with the assigned parameters.
         */
        public ShareCartConfig build() {
            return new ShareCartConfig(config);
        }

    }

    private ShareCartConfig() {

    }

    private ShareCartConfig(ShareCartConfig other) {
        createSharecartIfNotExists = other.createSharecartIfNotExists;
        directoryLevelsToCheck = other.directoryLevelsToCheck;
        clampToConstraints = other.clampToConstraints;
        strictFileMode = other.strictFileMode;
        applicationPath = other.applicationPath;

        if (applicationPath == null) {
            applicationPath = ShareCartFileUtils.getApplicationPath();
        }
    }

    /**
     * @return Whether or not the sharecart file will be created if it is missing.
     */
    public boolean willAutoCreateFile() {
        return createSharecartIfNotExists;
    }

    /**
     * @return The amount of directories above the running path to check for the existence of a sharecart file.
     */
    public int getDirectoryLevelsToCheck() {
        return directoryLevelsToCheck;
    }

    /**
     * Gets whether parameters will automatically be clamped to be within constraints.
     *
     * @return The value of the flag.
     * <br/><br/>
     * If {@code true}, any values that are not within the minimum and maximum value
     * for that parameter will be automatically set to either the maximum or the minimum
     * - whichever is the closest.
     * <br/><br/>
     * If {@code false}, trying to set a value that does not fulfill these constraints causes
     * an InvalidParameterException to be thrown.
     *
     * @see Constraints
     * @see io.itch.frogcheese.sharecart.error.InvalidParameterException
     */
    public boolean willClampToConstraints() {
        return clampToConstraints;
    }

    /**
     * Gets whether to assign default values or throw an exception when encountering errors
     * when reading the sharecart file.
     *
     * @return The value of the flag.
     * <br/><br/>
     * If {@code true}, parameters will get default values if there are errors in the file.
     * <br/><br/>
     * If {@code false}, a ShareCartFormatException will be thrown when encountering an error.
     *
     * @see io.itch.frogcheese.sharecart.error.ShareCartFormatException
     */
    public boolean isStrictFileReadMode() {
        return strictFileMode;
    }

    /**
     * @return The absolute path of the running application.
     */
    public String getApplicationPath() {
        return applicationPath;
    }

}
