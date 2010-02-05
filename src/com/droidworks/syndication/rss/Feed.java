package com.droidworks.syndication.rss;

import java.util.ArrayList;
import java.util.Date;

import com.droidworks.syndication.rss.FeedAdapter.Image;

public class Feed {

	private String mTitle;
	private String mLink;
	private String mDescription;
	private Date mPubDate;
	private String mLanguage;
	private String mGenerator;
	private String mCopyright;
	private String mManagingEditor;
    private String mCategory;
	private int mTTL;
	private Image mFeedImage;

	private ArrayList<FeedItem> mItems = new ArrayList<FeedItem>();

	public String getLanguage() {
		return mLanguage;
	}

	public void setLanguage(String language) {
		this.mLanguage = language;
	}

	public String getGenerator() {
		return mGenerator;
	}

	public void setGenerator(String generator) {
		this.mGenerator = generator;
	}

	public String getLink() {
		return mLink;
	}

	public void setLink(String link) {
		mLink = link;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public void setDescription(String description) {
		mDescription = description;
	}

	public String getDescription() {
		return mDescription;
	}

	public void setPubDate(Date pubDate) {
		mPubDate = pubDate;
	}

	public Date getPubDate() {
		return (Date) mPubDate.clone();
	}

	public String getCopyright() {
		return mCopyright;
	}

	public void setCopyright(String copyright) {
		mCopyright = copyright;
	}

	public String getManagingEditor() {
		return mManagingEditor;
	}

	public void setManagingEditor(String managingEditor) {
		mManagingEditor = managingEditor;
	}

	public String getCategory() {
		return mCategory;
	}

	public void setCategory(String category) {
		mCategory = category;
	}

	public int getTTL() {
		return mTTL;
	}

	public void setTTL(int ttl) {
		mTTL = ttl;
	}

	public Image getFeedImage() {
		return mFeedImage;
	}

	public void setFeedImage(Image feedImage) {
		mFeedImage = feedImage;
	}

	public ArrayList<FeedItem> getItems() {
		return mItems;
	}

	public void setItems(ArrayList<FeedItem> items) {
		mItems = items;
	}

	public FeedItem getItem(int i) {
		return mItems.get(i);
	}

	public void addItem(FeedItem tmpItem) {
		mItems.add(tmpItem);
	}

}
