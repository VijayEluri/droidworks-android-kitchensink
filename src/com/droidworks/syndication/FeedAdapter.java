package com.droidworks.syndication;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

public abstract class FeedAdapter extends BaseAdapter {

	private final LayoutInflater mLayoutInflater;

	private final Feed mFeed;

	public FeedAdapter(Context context, Feed feed) {
		super();
		mLayoutInflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mFeed = feed;
	}

	// class for holding image information regarding this feed.
	public static class Image implements Parcelable {

		public String url;
		public String title;
		public String link;
		public int width;
		public int height;

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel out, int flags) {
            out.writeString(url);
            out.writeString(title);
            out.writeString(link);
            out.writeInt(width);
            out.writeInt(height);
        }

        public static final Parcelable.Creator<Image> CREATOR
                = new Parcelable.Creator<Image>() {
            public Image createFromParcel(Parcel in) {
                return new Image(in);
            }

            public Image[] newArray(int size) {
                return new Image[size];
            }
        };

        private Image(Parcel in) {
            url = in.readString();
            title = in.readString();
            link = in.readString();
            width = in.readInt();
            height = in.readInt();
        }

		public Image(String url, String title, String link, int width,
				int height) {

			this.url = url;
			this.title = title;
			this.link = link;
			this.width = width;
			this.height = height;
		}
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

	public Feed getFeed() {
		return mFeed;
	}

}
