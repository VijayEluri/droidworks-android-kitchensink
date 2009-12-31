package com.droidworks.xml;

import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.os.Handler;
import android.util.Log;
import android.widget.ListAdapter;


public abstract class Parser<T extends ListAdapter> {

	private final T mAdapter;
	// need a uiHandler
	private final Handler mUiHandler;
	private final String mNamespace;

	public Parser(Handler uiHandler, T adapter, String namespace) {
		mAdapter = adapter;
		mUiHandler = uiHandler;
		mNamespace = namespace;

		setupNodes();
	}

	protected abstract void setupNodes();

	protected abstract ContentHandler getContentHandler();

	public void parse(InputStream stream) {
		try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();

            /* Get the XMLReader of the SAXParser we created. */
            XMLReader xr = sp.getXMLReader();


			xr.setContentHandler(getContentHandler());
			xr.parse(new InputSource(stream));
			stream.close();
		}
		catch (Exception e) {
			Log.e(getClass().getCanonicalName(),
					"Failure parsing document", e);
		}
	}

	public T getAdapter() {
		return mAdapter;
	}

	public Handler getUiHandler() {
		return mUiHandler;
	}

	public String getNamespace() {
		return mNamespace;
	}

}
