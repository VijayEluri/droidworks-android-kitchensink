package com.droidworks.syndication;

import java.io.Serializable;
import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Model for an "item" parsed from a syndication feed.
 *
 * @author jasonleehudgins@gmail.com
 */
public class FeedItem implements Parcelable, Serializable {

	private static final long serialVersionUID = 6360228609918230909L;
	
	private String mAuthorNode;
	private String mTitle;
	private String mLink;
	private String mComments;
	private long mPubDate;
	private String mGuid;
	private String mDescription;
	private String mMediaUrl;
	private int mMediaSize;
	private String mMediaType;
	private String mItunesSummary;
	private String mCategory;
	private long mItunesDuration;  // duration in seconds

	public FeedItem() {
		
	}
	
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
		return new Date(mPubDate);
	}

	public void setPubDate(Date pubDate) {
		mPubDate = pubDate.getTime();
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

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<FeedItem> CREATOR = new Creator<FeedItem>() {
		public FeedItem createFromParcel(Parcel in) {
			return new FeedItem(in);
		}

		public FeedItem[] newArray(int size) {
			return new FeedItem[size];
		}
	};
	
	@Override
	public void writeToParcel(Parcel out, int arg1) {
		out.writeString(mAuthorNode);
		out.writeString(mTitle);
		out.writeString(mLink);
		out.writeString(mComments);
		out.writeLong(mPubDate);
		out.writeString(mGuid);
		out.writeString(mDescription);
		out.writeString(mMediaUrl);
		out.writeInt(mMediaSize);
		out.writeString(mMediaType);
		out.writeString(mItunesSummary);
		out.writeString(mCategory);
		out.writeLong(mItunesDuration);  // duration in seconds
	}

	public FeedItem(Parcel in) {
		mAuthorNode = in.readString();
		mTitle = in.readString();
		mLink = in.readString();
		mComments = in.readString();
		mPubDate = in.readLong();
		mGuid = in.readString();
		mDescription = in.readString();
		mMediaUrl = in.readString();
		mMediaSize = in.readInt();
		mMediaType = in.readString();
		mItunesSummary = in.readString();
		mCategory = in.readString();
		mItunesDuration = in.readLong();
	}
}


