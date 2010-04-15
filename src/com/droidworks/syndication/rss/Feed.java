package com.droidworks.syndication.rss;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import com.droidworks.syndication.rss.FeedAdapter.Image;
import com.droidworks.util.StringUtils;

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
		// add and sort
		mItems.add(tmpItem);
	}

	private final Comparator<FeedItem> dateComparator = new Comparator<FeedItem>() {

		@Override
		public int compare(FeedItem item1, FeedItem item2) {

			if (item1.getPubDate().after(item2.getPubDate())) {
				return -1;
			}
			else if (item1.getPubDate().before(item2.getPubDate())) {
				return 1;
			}

			return 0;
		}
	};

	/**
	 * Tests to see if the given item already is present within the feed by
	 * comparing guid's.
	 *
	 * @param item
	 * @return
	 */
	public boolean containsItem(FeedItem item) {

		for (FeedItem innerItem : mItems) {
			if (innerItem.getGuid().equals(item.getGuid())) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Merge another feed into this one, will not overwrite unset/empty
	 * values.
	 *
	 * @param feed
	 */
	public void mergeFeed(Feed feed) {
		if (!StringUtils.hasChars(mTitle)) {
			mTitle = feed.getTitle();
		}
		if (!StringUtils.hasChars(mLink)) {
			mLink = feed.getLink();
		}
		if (!StringUtils.hasChars(mDescription)) {
			mDescription = feed.getDescription();
		}
		if (mPubDate == null) {
			mPubDate = feed.getPubDate();
		}
		if (!StringUtils.hasChars(mLanguage)) {
			mLanguage = feed.getLanguage();
		}
		if (!StringUtils.hasChars(mGenerator)) {
			mGenerator = feed.getGenerator();
		}
		if (!StringUtils.hasChars(mCopyright)) {
			mCopyright = feed.getCopyright();
		}
		if (!StringUtils.hasChars(mManagingEditor)) {
			mManagingEditor = feed.getManagingEditor();
		}
		if (!StringUtils.hasChars(mCategory)) {
			mCategory = feed.getCategory();
		}
		if (mTTL == 0) {
			mTTL = feed.getTTL();
		}
		if (mFeedImage == null) {
			mFeedImage = feed.getFeedImage();
		}

		// merge feed items
		for (FeedItem item : feed.getItems()) {
			if (!containsItem(item))
				addItem(item);
		}

		// always sort after merging
		sort();
	}

	public void sort() {
		Collections.sort(mItems, dateComparator);
	}

}
