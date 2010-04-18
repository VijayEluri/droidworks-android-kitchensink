package com.droidworks.parsers.twitter;


public class Tweet {

	private final long mTime;
	private final String mContent;

	public Tweet(long time, String content) {
		mTime = time;
		mContent = content;
	}

	public long getTime() {
		return mTime;
	}

	public String getContent() {
		return mContent;
	}

}
