package com.droidworks.syndication.rss;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;

import android.os.Handler;
import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.sax.StartElementListener;
import android.util.Log;

import com.droidworks.parsers.rss.itunes.DurationParser;
import com.droidworks.xml.Parser;

/*
 * Parser for RSS 2.0 syndication feeds.  Supports some itunes tags
 */
public class Rss2Parser extends Parser<FeedAdapter> {

	private RootElement mRootElement;

	private String mImageUrl;
	private String mImageTitle;
	private String mImageLink;
	private int mImageWidth;
	private int mImageHeight;
	private final DurationParser mDurationParser = new DurationParser();

	private FeedItem mFeedItem;

	private static final String LOG_LABEL = "Rss2Parser";

	public Rss2Parser(Handler uiHandler, FeedAdapter adapter) {
		super(uiHandler, adapter, "");
	}

	@Override
	protected ContentHandler getContentHandler() {
		return mRootElement.getContentHandler();
	}

	@Override
	protected void setupNodes() {

	    final SimpleDateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");

		mRootElement = new RootElement(mNamespace, "rss");

		Element channelNode = mRootElement.getChild(mNamespace, "channel");

		// channel sub nodes
		Element titleNode = channelNode.getChild(mNamespace, "title");
		Element linkNode = channelNode.getChild(mNamespace, "link");
		Element descriptionNode = channelNode.getChild(mNamespace, "description");
		Element pubDateNode = channelNode.getChild(mNamespace, "pubDate");
		Element languageNode = channelNode.getChild(mNamespace, "language");
		Element generatorNode = channelNode.getChild(mNamespace, "generator");
		Element copyrightNode = channelNode.getChild(mNamespace, "copyright");
		Element managingEditorNode = channelNode.getChild(mNamespace,
				"managingEditor");
		Element categoryNode = channelNode.getChild(mNamespace, "category");
		Element ttlNode = channelNode.getChild(mNamespace, "TTL");
		Element imageNode = channelNode.getChild(mNamespace, "image");
		Element imageHeightNode = imageNode.getChild(mNamespace, "height");
		Element imageWidthNode = imageNode.getChild(mNamespace, "width");
		Element imageUrlNode = imageNode.getChild(mNamespace, "url");
		Element imageTitleNode = imageNode.getChild(mNamespace, "title");
		Element imageLinkNode = imageNode.getChild(mNamespace, "link");

		Element feedItemNode = channelNode.getChild(mNamespace, "item");
		Element feedItemTitleNode = feedItemNode.getChild(mNamespace, "title");
		Element feedItemLinkNode = feedItemNode.getChild(mNamespace, "link");
		Element feedItemCommentsNode = feedItemNode.getChild(mNamespace, "comments");
		Element feedItemPubDateNode = feedItemNode.getChild(mNamespace, "pubDate");
		Element feedItemGuidNode = feedItemNode.getChild(mNamespace, "guid");
		Element feedItemDescriptionNode = feedItemNode.getChild(mNamespace, "description");
		Element feedItemMediaNode = feedItemNode.getChild("media", "content");
		Element feedItemItunesSummary = feedItemNode.getChild("itunes", "summary");
		Element feedItemItunesDuration = feedItemNode.getChild("itunes", "duration");

		// TODO, the parser is currently broken i think, it's not properly parsing
		// the itunes stuff apparently..need to figure out what's up.
		feedItemItunesDuration.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				Log.d(LOG_LABEL, "parsing duration: " + body);
				mFeedItem.setItunesDuration(mDurationParser.parse(body));
			}
 		});

		feedItemItunesSummary.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				Log.d(LOG_LABEL, "parsing summary: " + body);
				mFeedItem.setItunesSummary(body);
			}
		});

		feedItemMediaNode.setStartElementListener(new StartElementListener() {
			public void start(Attributes attrs) {
				mFeedItem.setMediaUrl(attrs.getValue("url"));
				mFeedItem.setMediaType(attrs.getValue("type"));
				mFeedItem.setMediaSize(Integer.parseInt((attrs.getValue("fileSize"))));
			}
		});

		feedItemDescriptionNode.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				mFeedItem.setDescription(body);
			}
		});

		feedItemGuidNode.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				mFeedItem.setGuid(body);
			}
		});

		feedItemPubDateNode.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				try {
					mFeedItem.setPubDate(df.parse(body));
				}
				catch (ParseException e) {
					Log.e(getClass().getCanonicalName(), "Error parsing pubDate");
				}
			}
		});

		feedItemCommentsNode.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				mFeedItem.setComments(body);
			}
		});

		feedItemLinkNode.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				mFeedItem.setLink(body);
			}
		});

		feedItemTitleNode.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				mFeedItem.setTitle(body);
			}
		});

		feedItemNode.setStartElementListener(new StartElementListener() {
			public void start(Attributes attributes) {
				Log.d(LOG_LABEL, "parsing a feed item");
				mFeedItem = new FeedItem();
			}
		});

		feedItemNode.setEndElementListener(new EndElementListener() {
			public void end() {
				Log.d("Rss2Parser", "trying to add a feed item");

				if (mUiHandler == null) {
					mAdapter.addItem(mFeedItem);
					mAdapter.notifyDataSetChanged();
					return;
				}

				mUiHandler.post(new Runnable() {
					public void run() {
						mAdapter.addItem(mFeedItem);
						mAdapter.notifyDataSetChanged();
					}
				});
			}
		});

		imageLinkNode.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				mImageLink = body;
			}
		});

		imageTitleNode.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				mImageTitle = body;
			}
		});

		imageUrlNode.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				mImageUrl = body;
			}
		});

		imageNode.setEndElementListener(new EndElementListener() {
			public void end() {
				FeedAdapter.Image image = new FeedAdapter.Image(
						mImageUrl, mImageTitle, mImageLink, mImageWidth,
						mImageHeight);
				mAdapter.setFeedImage(image);
			}
		});

		imageWidthNode.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				mImageWidth = Integer.parseInt(body);
			}
		});

		imageHeightNode.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				mImageHeight = Integer.parseInt(body);
			}
		});

		ttlNode.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				mAdapter.setTTL(Integer.parseInt(body));
			}
		});

		categoryNode.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				mAdapter.setCategory(body);
			}
		});

		managingEditorNode.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				mAdapter.setManagingEditor(body);
			}
		});

		copyrightNode.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				mAdapter.setCopyright(body);
			}
		});

		languageNode.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				mAdapter.setLanguage(body);
			}
		});

		generatorNode.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				mAdapter.setGenerator(body);
			}
		});

		pubDateNode.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				try {
					mAdapter.setPubDate(df.parse(body));
				}
				catch (ParseException e) {
					Log.e(getClass().getCanonicalName(), "Error parsing pubDate");
				}
			}
		});

		descriptionNode.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				mAdapter.setDescription(body);
			}
		});

		linkNode.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				mAdapter.setLink(body);
			}
		});

		titleNode.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				mAdapter.setTitle(body);
			}
		});

	}

}
