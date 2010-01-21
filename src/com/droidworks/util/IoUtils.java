package com.droidworks.util;

import java.io.File;

public class IoUtils {

	/**
	 * Utility method that will create a directory (including all necessary)
	 * sub-directories.  It will return on success of if the directory
	 * already exists and false if otherwise.  Take care only to pass in
	 * a directory path and not a file path.
	 *
	 * @param path
	 *
	 * @return directory exists
	 */
	public static boolean ensureDirectoryExists(String path) {
		File outputDir = new File(path);

		if (outputDir.exists())
			return true;

		return outputDir.mkdirs();
	}

}
