package com.droidworks.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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

	/**
	 * Reads an InputStream into an OutputStream
	 *
	 * @param is
	 * @param os
	 * @throws IOException
	 */
    public static void InputStreamToOutputStream(InputStream is, OutputStream os) throws IOException {
        int b;
        while ((b = is.read()) != -1) {
            os.write(b);
        }
    }
}
