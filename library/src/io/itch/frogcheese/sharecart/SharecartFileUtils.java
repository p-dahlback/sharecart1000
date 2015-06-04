package io.itch.frogcheese.sharecart;

import java.io.File;

/**
 * Some utilities for handling sharecart files.
 */
class ShareCartFileUtils {

	static String RUNNING_DIR = null;
	static String APP_DIR = null;

	/**
	 * @return The absolute path to the running directory.
	 */
	public static String getRunningDirectory() {
		if(RUNNING_DIR == null) {
			RUNNING_DIR = new File("").getAbsolutePath();
		}
		return RUNNING_DIR;
	}

	/**
	 * @return The absolute path to the application package.
	 */
	public static String getApplicationPath() {
		if(APP_DIR == null) {
			String dir = null;
			try {
				dir = System.getProperty("application.dir");
			} catch(SecurityException e) {
				e.printStackTrace();
			}

			if(dir == null) {
				APP_DIR = ShareCartFileUtils.getRunningDirectory();
			} else {
				APP_DIR = dir;
			}
		}
		return APP_DIR;
	}

	/**
	 * Gets a file relative to the running directory
	 * @param filePath the relative path to the file
	 * @return A new File instance for the provided path. Check {@link File#exists()} to see if a file
	 * actually exists at that location.
	 */
	public static File getFileFromRunningDirectory(String filePath) {
		return new File(ShareCartFileUtils.getRunningDirectory(), filePath);
	}

	/**
	 * Gets a file relative to the running directory, traversing the file tree upwards the given number of levels.
	 * @param levels the number of levels to traverse upwards before appending the file path
	 * @param filePath the relative path to the file
	 * @return A new File instance for the provided path. Check {@link File#exists()} to see if a file
	 * actually exists at that location.
	 * @see #getRunningDirectory()
	 * @see #getFileAboveDirectory(int, String, String)
	 */
	public static File getFileAboveRunningDirectory(int levels, String filePath) {
		return ShareCartFileUtils.getFileAboveDirectory(levels, ShareCartFileUtils.getRunningDirectory(), filePath);
	}

	/**
	 * Gets a file relative to the given location, traversing the file tree upwards the given number of levels.
	 * @param levels the number of levels to traverse upwards before appending the file path
	 * @param directory absolute path to the directory to use as a relative point
	 * @param filePath the relative path to the file
	 * @return A new File instance for the provided path. Check {@link File#exists()} to see if a file
	 * actually exists at that location.
	 * @see #getFileAboveRunningDirectory(int, String)
	 */
	public static File getFileAboveDirectory(int levels, String directory, String filePath) {
		StringBuilder file = new StringBuilder(filePath);
		for(int i = 0; i < levels; i++) {
			file.insert(0, "../");
		}
		return new File(directory, file.toString());
	}
}
