package com.droidworks.util;

import android.content.Context;

import java.io.*;

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

    /**
     * Simple method to dump a file to the external drive and returns the path.  Purpose of this
     * is to help with debugging stuff.
     *
     * @param context
     * @param input
     * @param filename
     * @return
     */
    public static String dumpToFile(Context context, String input, String filename) {

        File f = context.getExternalFilesDir(null);
        File output = new File(f.getPath() + "/" + filename);

        try {
            FileWriter fw = new FileWriter(output, false);
            fw.append(input);
            fw.flush();
            fw.close();
        } catch (Exception e) {
            android.util.Log.e("DroidCatcher", "Error writing to log", e);
        }

        return output.getAbsolutePath();


    }
}
