package com.droidworks.syndication.rss;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;

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
public class Rss2Parser extends Parser<FeedItem> {

	private RootElement mRootElement;

	private String mImageUrl;
	private String mImageTitle;
	private String mImageLink;
	private int mImageWidth;
	private int mImageHeight;
	private final DurationParser mDurationParser = new DurationParser();

	private FeedItem mFeedItem;
	private final Feed mFeed = new Feed();
	private FeedItemFactory mFeedItemFactory;

	private static final String DEFAULT_NAMESPACE = "";
	private static final String NS_ITUNES = "http://www.itunes.com/dtds/podcast-1.0.dtd";
	private static final String NS_MEDIA = "http://search.yahoo.com/mrss/";

	public Rss2Parser() {
		super(DEFAULT_NAMESPACE);
	}

	public Rss2Parser(FeedItemFactory feedItemFactory) {
		super(DEFAULT_NAMESPACE);
		mFeedItemFactory = feedItemFactory;
	}

	@Override
	protected ContentHandler getContentHandler() {
		return mRootElement.getContentHandler();
	}

	@Override
	protected void setupNodes() {

	    final SimpleDateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");

		mRootElement = new RootElement("rss");

		Element channelNode = mRootElement.getChild("channel");

		// channel sub nodes
		Element titleNode = channelNode.getChild("title");
		Element linkNode = channelNode.getChild("link");
		Element descriptionNode = channelNode.getChild("description");
		Element pubDateNode = channelNode.getChild("pubDate");
		Element languageNode = channelNode.getChild("language");
		Element generatorNode = channelNode.getChild("generator");
		Element copyrightNode = channelNode.getChild("copyright");
		Element managingEditorNode = channelNode.getChild("managingEditor");
		Element categoryNode = channelNode.getChild("category");
		Element ttlNode = channelNode.getChild("TTL");

		Element imageNode = channelNode.getChild("image");
		Element imageHeightNode = imageNode.getChild("height");
		Element imageWidthNode = imageNode.getChild("width");
		Element imageUrlNode = imageNode.getChild("url");
		Element imageTitleNode = imageNode.getChild("title");
		Element imageLinkNode = imageNode.getChild("link");

		Element feedItemNode = channelNode.getChild("item");
		Element feedItemTitleNode = feedItemNode.getChild("title");
		Element feedItemLinkNode = feedItemNode.getChild("link");
		Element feedItemCommentsNode = feedItemNode.getChild("comments");
		Element feedItemPubDateNode = feedItemNode.getChild("pubDate");
		Element feedItemGuidNode = feedItemNode.getChild("guid");
		Element feedItemDescriptionNode = feedItemNode.getChild("description");
		Element feedItemMediaNode = feedItemNode.getChild(NS_MEDIA, "content");
		Element feedItemItunesSummary = feedItemNode.getChild(NS_ITUNES, "summary");
		Element feedItemItunesDuration = feedItemNode.getChild(NS_ITUNES,"duration");
		Element feedItemCategoryNode = feedItemNode.getChild("category");

		feedItemCategoryNode.setEndTextElementListener(new EndTextElementListener() {
			public void end(String text) {
				mFeedItem.setCategory(text);
			}
		});

		feedItemItunesDuration.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				mFeedItem.setItunesDuration(mDurationParser.parse(body) * 1000);
			}
 		});

		feedItemItunesSummary.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
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
				if (mFeedItemFactory != null)
					mFeedItem = mFeedItemFactory.create();
				else
					mFeedItem = new FeedItem();
			}
		});

		feedItemNode.setEndElementListener(new EndElementListener() {
			public void end() {

				final FeedItem tmpItem = mFeedItem;
				mFeedItem = null;

				// always add items to the feed
				mFeed.addItem(tmpItem);

				// notify a listener if present
				if (getListener() != null) {
					getListener().onItemParsed(tmpItem);
				}
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
				mFeed.setFeedImage(image);
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
				mFeed.setTTL(Integer.parseInt(body));
			}
		});

		categoryNode.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				mFeed.setCategory(body);
			}
		});

		managingEditorNode.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				mFeed.setManagingEditor(body);
			}
		});

		copyrightNode.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				mFeed.setCopyright(body);
			}
		});

		languageNode.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				mFeed.setLanguage(body);
			}
		});

		generatorNode.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				mFeed.setGenerator(body);
			}
		});

		pubDateNode.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				try {
					mFeed.setPubDate(df.parse(body));
				}
				catch (ParseException e) {
					Log.e(getClass().getCanonicalName(), "Error parsing pubDate");
				}
			}
		});

		descriptionNode.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				mFeed.setDescription(body);
			}
		});

		linkNode.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				mFeed.setLink(body);
			}
		});

		titleNode.setEndTextElementListener(new EndTextElementListener() {
			public void end(String body) {
				mFeed.setTitle(body);
			}
		});
	}

	public Feed getFeed() {
		return mFeed;
	}

}
