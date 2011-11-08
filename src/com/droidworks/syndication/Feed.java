package com.droidworks.syndication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import android.text.TextUtils;

import com.droidworks.syndication.FeedAdapter.Image;
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
		if (mPubDate != null)
			return (Date) mPubDate.clone();

		return null;
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

	public void replaceItem(FeedItem item) {
		for (int i = 0; i < mItems.size(); i++) {
			if (mItems.get(i).getGuid().equals(item.getGuid())) {
				mItems.set(i, item);
			}
		}
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
	public void mergeIntoMe(Feed feed) {
		if (TextUtils.isEmpty(mTitle)) {
			mTitle = feed.getTitle();
		}
		if (TextUtils.isEmpty(mLink)) {
			mLink = feed.getLink();
		}
		if (TextUtils.isEmpty(mDescription)) {
			mDescription = feed.getDescription();
		}
		if (mPubDate == null) {
			mPubDate = feed.getPubDate();
		}
		if (TextUtils.isEmpty(mLanguage)) {
			mLanguage = feed.getLanguage();
		}
		if (TextUtils.isEmpty(mGenerator)) {
			mGenerator = feed.getGenerator();
		}
		if (TextUtils.isEmpty(mCopyright)) {
			mCopyright = feed.getCopyright();
		}
		if (TextUtils.isEmpty(mManagingEditor)) {
			mManagingEditor = feed.getManagingEditor();
		}
		if (TextUtils.isEmpty(mCategory)) {
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
