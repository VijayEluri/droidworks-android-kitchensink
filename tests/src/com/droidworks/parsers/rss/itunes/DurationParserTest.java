package com.droidworks.parsers.rss.itunes;

import android.test.InstrumentationTestCase;

public class DurationParserTest extends InstrumentationTestCase {

	private DurationParser parser;

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		parser = new DurationParser();
	}

	public void testParse() {
		long result = parser.parse("10");
		assertEquals(10, result);
	}

}
