package com.droidworks.http.download;

import java.io.InputStream;
import java.util.ArrayList;

// TODO:
// think and think hard, does a download task and a stream handler need
// to be seperate?  i'm really thinking not now..

public abstract class DownloadTask<T> {

	public static int STATUS_OK = 0;
	public static int STATUS_TIMED_OUT = 1;
	public static int STATUS_NO_STORAGE = 2;
	public static int STATUS_FILE_WRITE_ERROR = 3;
	public static int STATUS_CANCELLED = 4;
	public static int STATUS_GENERAL_ERROR = 5;

	private final String mUrl;
	private int mTimeOut = 10;  // default to a 10 second connection timeout

	private final ArrayList<DownloadCompletedListener<T>> mListeners
		= new ArrayList<DownloadCompletedListener<T>>();

	public void setTimeout(int seconds) {
		mTimeOut = seconds;
	}

	public int getTimeout() {
		return mTimeOut;
	}

	public DownloadTask(String url) {
		mUrl = url;
	}

	public void addListener(DownloadCompletedListener<T> listener) {
		mListeners.add(listener);
	}

	public String getUrl() {
		return mUrl;
	}

	public ArrayList<DownloadCompletedListener<T>> getListeners() {
		return mListeners;
	}

	// TODO: this only works with the sdcard, we need something else
	// that's a bit more flexable.  the name is correct, but the Path
	// thing is kind of lame, can this be genericized?
	public interface DownloadCompletedListener<T> {
		public void onDownloadComplete(T output, int resultCode);
	}

	public void notifyListeners(int resultCode) {
		for (DownloadCompletedListener<T> l : getListeners()) {
			l.onDownloadComplete(getOutput(), resultCode);
		}

	}

	abstract public int processStream(InputStream stream);

	abstract public T getOutput();

	abstract public void cancel();

}
