package com.droidworks.syndication.atom.gdata.youtube;

import java.util.ArrayList;

import android.widget.BaseAdapter;


public abstract class YouTubeVideoAdapter extends BaseAdapter {

	private final ArrayList<YouTubeItem> mItems = new ArrayList<YouTubeItem>();

	private String mAuthor;

	public void addItem(YouTubeItem item) {
		mItems.add(item);
		notifyDataSetChanged();
	}

	public String getAuthor() {
		return mAuthor;
	}

	public void setAuthor(String author) {
		mAuthor = author;
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
		return mItems.get(position).getYoutubeId().hashCode();
	}

}
