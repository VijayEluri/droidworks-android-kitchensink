package com.droidworks.xml;

import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.droidworks.syndication.FeedItem;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.util.Log;


public abstract class Parser<T> {

	private final String mDefaultNameSpace;
	private boolean mIsFinished = false;
    private String mEncoding;

	// should i support multiple listeners?
	private OnItemParsedListener<T> mListener;

	// interface that gets called when an item is added
	public interface OnItemParsedListener<T> {
		public void onItemParsed(T item);
	}

	public Parser(String defaultNamespace) {
		mDefaultNameSpace = defaultNamespace;
		setupNodes();
	}

	protected abstract void setupNodes();

	protected abstract ContentHandler getContentHandler();

	public void parse(InputStream stream) {

		mIsFinished = false;

		try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();

            /* Get the XMLReader of the SAXParser we created. */
            XMLReader xr = sp.getXMLReader();

            // TODO, just doing some testing
			xr.setContentHandler(getContentHandler());
            InputSource is = new InputSource(stream);

            // set the encoding if requested.
            if (mEncoding != null)  {
                is.setEncoding(mEncoding);
            }

			xr.parse(is);
			stream.close();
		}
		catch (Exception e) {
			Log.e(getClass().getCanonicalName(),
					"Failure parsing document", e);
		}

		mIsFinished = true;
	}

	public String getDefaultNamespace() {
		return mDefaultNameSpace;
	}

	protected OnItemParsedListener<T> getListener() {
		return mListener;
	}

	public void registerListener(OnItemParsedListener<T> listener) {
		mListener = listener;
	}

	public void unregisterListener(OnItemParsedListener<T> listener) {
		mListener = null;
	}

    public void setEncoding(String encoding) {
        mEncoding = encoding;
    }

    /**
	 * Has the parser completed?
     *
	 * @return
	 */
	public boolean isFinished() {
		return mIsFinished;
	}

}
