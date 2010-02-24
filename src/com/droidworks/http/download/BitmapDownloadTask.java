package com.droidworks.http.download;

import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * DownloadTask implementation that spits out bitmaps
 *
 * @author Jason Hudgins <jasonleehudgins@gmail.com>
 */
public class BitmapDownloadTask extends DownloadTask<Bitmap>{

	private Bitmap mBitmap;

	public BitmapDownloadTask(String url) {
		super(url);
	}

	@Override
	public Bitmap getOutput() {
		return mBitmap;
	}

	@Override
	public void processStream(InputStream stream) {

		if (Thread.interrupted()) {
			setStatus(DownloadTask.STATUS_CANCELLED);
			return;
		}

		try {
			mBitmap = BitmapFactory.decodeStream(stream);
			setStatus(DownloadTask.STATUS_OK);
			return;
		}
		catch (Exception e) {
			Log.e(getClass().getCanonicalName(),
					"Exception occurred downloading bitmap", e);
		}

		setStatus(DownloadTask.STATUS_GENERAL_ERROR);
	}

	public static abstract class BitmapDownloadCompletedListener
		implements DownloadCompletedListener<Bitmap> { }

}
