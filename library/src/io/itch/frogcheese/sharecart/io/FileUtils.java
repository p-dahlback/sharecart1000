package io.itch.frogcheese.sharecart.io;

import java.io.File;

public class FileUtils {

	private static String RUNNING_DIR = null;
	private static String APP_DIR = null;

	public static String getRunningLocation() {
		if(RUNNING_DIR == null) {
			RUNNING_DIR = new File("").getAbsolutePath();
		}
		return RUNNING_DIR;
	}

	public static String getAppLocation() {
		if(APP_DIR == null) {
			String dir = null;
			try {
				dir = System.getProperty("sharecart.dir");
			} catch(SecurityException e) {
				e.printStackTrace();
			}

			if(dir == null) {
				APP_DIR = FileUtils.getRunningLocation();
			} else {
				APP_DIR = dir;
			}
		}
		return APP_DIR;
	}

	public static File fileFromRunningLocation(String filePath) {
		return new File(FileUtils.getRunningLocation(), filePath);
	}

	public static File fileAboveRunningLocation(int levels, String filePath) {
		return FileUtils.fileAboveLocation(levels, FileUtils.getRunningLocation(), filePath);
	}

	public static File fileAboveLocation(int levels, String location, String filePath) {
		StringBuilder file = new StringBuilder(filePath);
		for(int i = 0; i < levels; i++) {
			file.insert(0, "../");
		}
		return new File(location, file.toString());
	}
}
