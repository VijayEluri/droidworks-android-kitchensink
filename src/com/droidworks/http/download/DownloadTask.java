package com.droidworks.http.download;

import java.util.ArrayList;

// TODO:
//  the async download listener has a writeFile method, i think an
//  injectable "StreamHandler" would be more flexable.
public class DownloadTask {

	private final String mUrl;
	private int mTimeOut = 10;  // default to a 10 second connection timeout

	private final ArrayList<DownloadCompletedListener> mListeners
		= new ArrayList<DownloadCompletedListener>();

	public void setTimeout(int seconds) {
		mTimeOut = seconds;
	}

	public int getTimeout() {
		return mTimeOut;
	}

	public DownloadTask(String url) {
		mUrl = url;
	}

	public void addListener(DownloadCompletedListener listener) {
		mListeners.add(listener);
	}

	public String getUrl() {
		return mUrl;
	}

	public ArrayList<DownloadCompletedListener> getListeners() {
		return mListeners;
	}

	// TODO: this only works with the sdcard, we need something else
	// that's a bit more flexable.  the name is correct, but the Path
	// thing is kind of lame, can this be genericized?
	public interface DownloadCompletedListener {
		public void onDownloadComplete(String path, int resultCode);
	}

}
