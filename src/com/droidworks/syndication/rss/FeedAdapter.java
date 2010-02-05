package com.droidworks.syndication.rss;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

public abstract class FeedAdapter extends BaseAdapter {

	private final LayoutInflater mLayoutInflater;

	private Feed mFeed = new Feed();

	public FeedAdapter(Context context) {
		super();
		mLayoutInflater = (LayoutInflater) context
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
		mFeed.getItems().add(item);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mFeed.getItems().size();
	}

	@Override
	public Object getItem(int position) {
		return mFeed.getItems().get(position);
	}

	@Override
	public long getItemId(int position) {
		return mFeed.getItems().get(position).getGuid().hashCode();
	}

	public LayoutInflater getLayoutInflater() {
		return mLayoutInflater;
	}

}
