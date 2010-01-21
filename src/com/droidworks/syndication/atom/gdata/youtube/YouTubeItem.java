package com.droidworks.syndication.atom.gdata.youtube;

import java.util.Date;

public class YouTubeItem {

	private final Date mDatePublished;
	private final String mTitle;
	private final String mDescription;
	private final String mYouTubeUrl;
	private final long mDuration;
	private final String mThumbnailUrl;
	private final String mYouTubeId;

	public YouTubeItem(Date datePublished, String title, String description,
			String youtubeUrl, long duration, String thumbnailUrl,
			String youtubeId) {

		mDatePublished = datePublished;
		mTitle = title;
		mDescription = description;
		mYouTubeUrl = youtubeUrl;
		mDuration = duration;
		mThumbnailUrl = thumbnailUrl;
		mYouTubeId = youtubeId;
	}

	public Date getDatePublished() {
		return mDatePublished;
	}

	public String getTitle() {
		return mTitle;
	}

	public String getDescription() {
		return mDescription;
	}

	public String getYoutubeUrl() {
		return mYouTubeUrl;
	}

	public long getDuration() {
		return mDuration;
	}

	public String getThumbnailUrl() {
		return mThumbnailUrl;
	}

	public String getYoutubeId() {
		return mYouTubeId;
	}


}
