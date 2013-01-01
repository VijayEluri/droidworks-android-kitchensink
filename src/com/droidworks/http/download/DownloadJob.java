package com.droidworks.http.download;

import java.io.Serializable;

import android.R;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class DownloadJob implements Serializable, Parcelable {

	private static final long serialVersionUID = -26957191258042933L;

	public static class State {
		// an error occured
		public static final int ERROR = -1;
		// the default state
		public static final int INITIALIZED = 0;
		// duh
		public static final int COMPLETED = 1;
		// currently being downloaded
		public static final int ACTIVE = 2;
		// job in queue, waiting to be downloaded
		public static final int QUEUED = 3;
		// job has been cancelled
		public static final int CANCELLED = 5;
		// job is blocked for some reason
		public static final int BLOCKED = 6;
	}

	public static class BlockedState {
		public static final int NOT_BLOCKED = 0;
		public static final int WAITING_WIFI = 1;
	}

	private final String mJobId;
	private final String mDownloadUrl;
	private final long mTimeOutSeconds;
	private final String mOutputDir;
	private final String mOutputFileName;
	private final String mDescription; // for use in notification manager

	private int mState;
	private int mBlockedState;
	private long mContentLength = 0;
	private int mBytesRead = 0;

	public DownloadJob(String url, String description, String dir,
			String filename, int seconds, String jobId) {

		mDownloadUrl = url;
		mOutputDir = dir;
		mOutputFileName = filename;
		mTimeOutSeconds = seconds;
		mDescription = description;
		mJobId = jobId;
	}

	private DownloadJob(Parcel in) {
		mJobId = in.readString();
		mDownloadUrl = in.readString();
		mState = in.readInt();
		mBlockedState = in.readInt();
		mTimeOutSeconds = in.readLong();
		mContentLength = in.readLong();
		mBytesRead = in.readInt();
		mOutputDir = in.readString();
		mOutputFileName = in.readString();
		mDescription = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mJobId);
		dest.writeString(mDownloadUrl);
		dest.writeInt(mState);
		dest.writeInt(mBlockedState);
		dest.writeLong(mTimeOutSeconds);
		dest.writeLong(mContentLength);
		dest.writeInt(mBytesRead);
		dest.writeString(mOutputDir);
		dest.writeString(mOutputFileName);
		dest.writeString(mDescription);
	}

	// returns the filename if it's null, then default to returning
	// the last segment of the URL, which hopefully makes sense
	public String getFileName() {
		return mOutputFileName;
	}

	/**
	 * Returns the path of the directory where the file will be
	 * written.
	 *
	 * @return destination directory to write to.
	 */
	public String getOutputDir() {
		return mOutputDir;
	}

	public static final Parcelable.Creator<DownloadJob> CREATOR =
		new Parcelable.Creator<DownloadJob>() {

		public DownloadJob createFromParcel(Parcel source) {
			return new DownloadJob(source);
		}

		public DownloadJob[] newArray(int size) {
			return new DownloadJob[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	public int getPercentageCompleted() {

        if (mContentLength == 0)
            return 0;

		return Math.round(( ((float) mBytesRead
				/ (float) mContentLength)) * 100f);
	}

	public int getState() {
		return mState;
	}

	public void setState(int state, int blockedState) {
		
		mState = state;

		// if the job is not blocked, then clear wifi flags
		if (!isBlocked()) {
			mBlockedState = BlockedState.NOT_BLOCKED;
		}
		else {
			mBlockedState = blockedState;
		}
	}

	public long getContentLength() {
		return mContentLength;
	}

	public void setContentLength(long contentLength) {
		mContentLength = contentLength;
	}

	public int getBytesRead() {
		return mBytesRead;
	}

	/**
	 * Indicates if the job has been cancelled by the user or due to an error.
	 * 
	 * @return
	 */
	public boolean isCancelled() {
		return (mState == State.CANCELLED) || (mState == State.ERROR);
	}
	
	public String getJobId() {
		return mJobId;
	}

	public String getDownloadUrl() {
		return mDownloadUrl;
	}

	public void incrementBytesRead(int bytesRead) {
		mBytesRead += bytesRead;
	}

	public boolean isCompleted() {
		return (mState == State.COMPLETED);
	}

	public boolean isActive() {
		return (mState == State.ACTIVE);
	}

	public boolean isWaitingForWifi() {
		return (isBlocked() && mBlockedState == BlockedState.WAITING_WIFI);
	}

	public boolean isBlocked() {
		return (mState == State.BLOCKED);
	}

	public boolean isQueued() {
		return (mState == State.QUEUED);
	}

	public boolean isQueuedOrActive() {
		return (isQueued() || isActive());
	}

	public long getTimeOutSeconds() {
		return mTimeOutSeconds;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof DownloadJob)) {
			return false;
		}

		if (((DownloadJob) obj).getJobId() == getJobId()) {
			return true;
		}

		return false;
	}

	public String getDescription() {
		return mDescription;
	}

}
