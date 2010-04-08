package com.droidworks.http.download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.os.Environment;
import android.util.Log;

import com.droidworks.util.AndroidUtils;

/**
 * DownloadTask implementation that writes to writable storage.
 *
 * @author Jason Hudgins <jason@droidworks.com>
 *
 */
public class WritableStorageDownloadTask extends DownloadTask<String> {

	public WritableStorageDownloadTask(String url) {
		super(url);
	}

	private String mOutputFilePath =  null;

	@Override
	public String getOutput() {
		return mOutputFilePath;
	}

	@Override
	public void processStream(InputStream is) {

		if (!AndroidUtils.hasStorage(true)) {
			setStatus(STATUS_NO_STORAGE);
			return;
		}

        String directoryName = Environment
    		.	getExternalStorageDirectory().toString() + "/tmp";

        File directory = new File(directoryName);

        if (!directory.isDirectory()) {
            if (!directory.mkdirs()) {
            	Log.e(getClass().getCanonicalName(),
            			"can't create directory: " + directoryName);
            	setStatus(STATUS_FILE_WRITE_ERROR);
				return;
            }
        }
        try {
			File tmpFile = File.createTempFile("ad_", null, directory);
			FileOutputStream os = new FileOutputStream(tmpFile);
			byte[] data = new byte[512];
			int bytesRead = 0;
			while ((bytesRead = is.read(data)) != -1) {
				os.write(data, 0, bytesRead);

				if (Thread.interrupted()) {
					setStatus(STATUS_CANCELLED);
					return;
				}
			}

			// if we get here we suceeeded
			mOutputFilePath = tmpFile.getAbsolutePath();
			is.close();
			setStatus(STATUS_OK);
			return;
		}
        catch (IOException e) {
			Log.e(getClass().getCanonicalName(),
					"Exception creating temp file", e);
			setStatus(STATUS_FILE_WRITE_ERROR);
			return;
        }
	}

}
