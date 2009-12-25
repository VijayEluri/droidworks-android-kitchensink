package com.droidworks.syndication.rss;

import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

public abstract class FeedAdapter extends BaseAdapter {

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

	protected final ArrayList<FeedItem> mItems = new ArrayList<FeedItem>();

	protected final LayoutInflater pLayoutInflater;

	public FeedAdapter(Context context) {
		super();

		pLayoutInflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	// class for holding image information regarding this feed.
	public static class Image {
		public final String url;
		public final String title;
		public final String link;
		public final int width;
		public final int height;

		public Image(String url, String title, String link, int width,
				int height) {

			this.url = url;
			this.title = title;
			this.link = link;
			this.width = width;
			this.height = height;
		}
	}

	public void addItem(FeedItem item) {
		mItems.add(item);
	}

	@Override
	public int getCount() {
		return mItems.size();
	}

	@Override
	public Object getItem(int position) {
		return mItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mItems.get(position).getGuid().hashCode();
	}

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

}
