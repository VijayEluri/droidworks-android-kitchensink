package com.droidworks.syndication.atom.gdata.youtube;

import java.util.ArrayList;

import android.widget.BaseAdapter;


public abstract class YouTubeVideoAdapter extends BaseAdapter {

	private final ArrayList<YouTubeItem> mItems = new ArrayList<YouTubeItem>();

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
		return mItems.get(position).youtubeId.hashCode();
	}

}
