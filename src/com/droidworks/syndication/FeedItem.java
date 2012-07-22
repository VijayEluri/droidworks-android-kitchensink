package com.droidworks.syndication;

import java.io.Serializable;
import java.util.Date;

/**
 * Model for an "item" parsed from a syndication feed.
 *
 * @author jasonleehudgins@gmail.com
 */
public class FeedItem implements Serializable {

	private static final long serialVersionUID = 6360228609918230909L;
	
	private String mAuthorNode;
	private String mTitle;
	private String mLink;
	private String mComments;
	private Date mPubDate;
	private String mGuid;
	private String mDescription;
	private String mMediaUrl;
	private int mMediaSize;
	private String mMediaType;
	private String mItunesSummary;
	private String mCategory;
	private long mItunesDuration;  // duration in seconds

	/**
	 * Returns media item duration in milliseconds
	 *
	 * @return
	 */
	public long getItunesDuration() {
		return mItunesDuration;
	}

	/**
	 * Set duration in milliseconds
	 *
	 * @param milliseconds
	 */
	public void setItunesDuration(long milliseconds) {
		mItunesDuration =  milliseconds;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public String getLink() {
		return mLink;
	}

	public void setLink(String link) {
		mLink = link;
	}

	public String getComments() {
		return mComments;
	}

	public void setComments(String comments) {
		mComments = comments;
	}

	public Date getPubDate() {
		return mPubDate;
	}

	public void setPubDate(Date pubDate) {
		mPubDate = pubDate;
	}

	public String getGuid() {
		return mGuid;
	}

	public void setGuid(String guid) {
		mGuid = guid;
	}

	public String getDescription() {
		return mDescription;
	}

	public void setDescription(String description) {
		mDescription = description;
	}

	public String getMediaUrl() {
		return mMediaUrl;
	}

	public void setMediaUrl(String mediaUrl) {
		mMediaUrl = mediaUrl;
	}

	public int getMediaSize() {
		return mMediaSize;
	}

	public void setMediaSize(int size) {
		mMediaSize = size;
	}

	public String getMediaType() {
		return mMediaType;
	}

	public void setMediaType(String mediaType) {
		mMediaType = mediaType;
	}

	public String getItunesSummary() {
		return mItunesSummary;
	}

	public void setItunesSummary(String itunesSummary) {
		mItunesSummary = itunesSummary;
	}

	public String getCategory() {
		return mCategory;
	}

	public void setCategory(String category) {
		mCategory = category;
	}

	public String getAuthorNode() {
		return mAuthorNode;
	}

	public void setAuthorNode(String authorNode) {
		mAuthorNode = authorNode;
	}

}


