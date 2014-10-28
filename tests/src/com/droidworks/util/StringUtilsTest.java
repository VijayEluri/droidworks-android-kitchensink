package com.droidworks.util;

import android.test.InstrumentationTestCase;

public class StringUtilsTest extends InstrumentationTestCase {

	public void testCharCount() {
		assertEquals(3, StringUtils.charCount("xcxcxc", 'x'));
	}

	public void testFormatHMS() {
		String result = StringUtils.formatHMS(new StringBuilder(), 370);
		assertEquals("6:10", result);

		result = StringUtils.formatHMS(new StringBuilder(), 186);
		assertEquals("3:06", result);

		result = StringUtils.formatHMS(new StringBuilder(), 15);
		assertEquals("0:15", result);

		result = StringUtils.formatHMS(new StringBuilder(), 7268);
		assertEquals("2:01:08", result);
	}

	public void testAddLeadingZero() {
        StringBuilder sb = new StringBuilder();
        StringUtils.appendWithLeadingZero(sb, 4);
		assertEquals("04", sb.toString());

        sb = new StringBuilder();
        StringUtils.appendWithLeadingZero(sb, 14);
		assertEquals("14", sb.toString());
	}

}
