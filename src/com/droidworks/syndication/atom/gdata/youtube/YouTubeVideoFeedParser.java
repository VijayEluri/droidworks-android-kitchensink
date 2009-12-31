package com.droidworks.syndication.atom.gdata.youtube;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;

import android.os.Handler;
import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.sax.StartElementListener;

import com.droidworks.xml.Parser;

public class YouTubeVideoFeedParser extends Parser<YouTubeVideoAdapter> {

	RootElement mRootElement;

	public static final String NS_MEDIA = "http://search.yahoo.com/mrss/";
	public static final String NS_YT = "http://gdata.youtube.com/schemas/2007";

	private final YouTubeVideoAdapter mAdapter;

	@SuppressWarnings("unused")
	private final String LOG_LABEL = "YouTubeVideoFeedParser";

	private Date mDatePublished;
	private String mTitle;
	private String mDescription;
	private String mYouTubeUrl;
	private long mDuration;
	private String mThumbnailUrl;

	public YouTubeVideoFeedParser(Handler uiHandler, YouTubeVideoAdapter adapter,
			String namespace) {

		super(uiHandler, adapter, namespace);
		mAdapter = adapter;
	}

	@Override
	protected ContentHandler getContentHandler() {
		return mRootElement.getContentHandler();
	}

	@Override
	protected void setupNodes() {
		mRootElement = new RootElement(getNamespace(), "feed");

		// 2009-12-04T22:51:39.000-0000
		final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

		Element authorNode = mRootElement.requireChild(getNamespace(), "author");
		Element authorNameNode = authorNode.requireChild(getNamespace(), "name");

		Element entryNode = mRootElement.getChild(getNamespace(), "entry");
		Element pubDateNode = entryNode.getChild(getNamespace(), "published");
		Element titleNode = entryNode.getChild(getNamespace(), "title");

		Element mediaGroupNode = entryNode.requireChild(NS_MEDIA, "group");
		Element descriptionNode = mediaGroupNode.requireChild(NS_MEDIA, "description");
		Element youTubeUrlNode = mediaGroupNode.requireChild(NS_MEDIA, "player");
		Element durationNode = mediaGroupNode.requireChild(NS_YT, "duration");
		Element thumbnailNode = mediaGroupNode.requireChild(NS_MEDIA, "thumbnail");

		thumbnailNode.setStartElementListener(new StartElementListener() {
			public void start(Attributes attrs) {
				if (attrs.getValue("url").contains("0.jpg"))
					mThumbnailUrl = attrs.getValue("url");
			}
		});

		durationNode.setStartElementListener(new StartElementListener() {
			public void start(Attributes attrs) {
				mDuration = Long.parseLong(attrs.getValue("seconds"));
			}
		});

		youTubeUrlNode.setStartElementListener(new StartElementListener() {
			public void start(Attributes attrs) {
				mYouTubeUrl = attrs.getValue("url");
			}
		});

		descriptionNode.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				mDescription = body;
			}
		});

		titleNode.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				mTitle = body;
			}
		});

		authorNameNode.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				mAdapter.setAuthor(body);
			}
		});

		pubDateNode.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				 try {
					 String time = body.replace("Z", "-0000");
					mDatePublished = df.parse(time);
				}
				 catch (ParseException e) {
					e.printStackTrace();
				}
			}
		});

		entryNode.setEndElementListener(new EndElementListener() {
			public void end() {
				final YouTubeItem item = new YouTubeItem(mDatePublished,
						mTitle, mDescription, mYouTubeUrl,
						mDuration, mThumbnailUrl, parseId(mYouTubeUrl));

				if (getUiHandler() == null) {
					mAdapter.addItem(item);
				}
				else {
					getUiHandler().post(new Runnable() {
						public void run() {
							mAdapter.addItem(item);
						}
					});
				}
			}
		});

	}

	private String parseId(String url) {
		Pattern p = Pattern.compile("v=(.+)&");
		Matcher m = p.matcher(url);
		if (m.find())
			return m.group(1);

		return null;
	}

}
