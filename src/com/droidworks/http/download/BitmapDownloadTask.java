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

	private boolean mIsCancelled = false;
	private Bitmap mBitmap;

	public BitmapDownloadTask(String url) {
		super(url);
	}

	@Override
	public void cancel() {
		mIsCancelled = true;
	}

	@Override
	public Bitmap getOutput() {
		return mBitmap;
	}

	@Override
	public int processStream(InputStream stream) {

		if (mIsCancelled)
			return DownloadTask.STATUS_CANCELLED;

		try {
			mBitmap = BitmapFactory.decodeStream(stream);
			return DownloadTask.STATUS_OK;
		}
		catch (Exception e) {
			Log.e(getClass().getCanonicalName(),
					"Exception occurred downloading bitmap", e);
		}

		return DownloadTask.STATUS_GENERAL_ERROR;
	}

	public static abstract class BitmapDownloadCompletedListener implements DownloadCompletedListener<Bitmap> {

	}
}
