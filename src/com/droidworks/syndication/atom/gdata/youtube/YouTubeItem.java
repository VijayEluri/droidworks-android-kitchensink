package com.droidworks.syndication.atom.gdata.youtube;

import java.util.Date;

public class YouTubeItem {

	private final Date mDatePublished;
	private final String mTitle;
	private final String mDescription;
	private final String mYouTubeUrl;
	private final long mDuration;
	private final String mYouTubeId;
	private final String mSmallThumbUrl;
	private final String mLargeThumbUrl;

	public YouTubeItem(Date datePublished, String title, String description,
			String youtubeUrl, long duration, String smallThumbUrl,
			String largeThumbUrl, String youtubeId) {

		mDatePublished = datePublished;
		mTitle = title;
		mDescription = description;
		mYouTubeUrl = youtubeUrl;
		mDuration = duration;
		mSmallThumbUrl = smallThumbUrl;
		mLargeThumbUrl = largeThumbUrl;
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

	public String getYouTubeUrl() {
		return mYouTubeUrl;
	}

	public long getDuration() {
		return mDuration;
	}

	public String getSmallThumbnailUrl() {
		return mSmallThumbUrl;
	}

	public String getLargeThumbnailUrl() {
		return mLargeThumbUrl;
	}

	public String getYouTubeId() {
		return mYouTubeId;
	}


}
