package com.droidworks.util;

import android.test.InstrumentationTestCase;

public class StringUtilsTest extends InstrumentationTestCase {

	public void testCharCount() {
		assertEquals(3, StringUtils.charCount("xcxcxc", 'x'));
	}

	public void testFormatHMS() {
		String result = StringUtils.formatHMS(186);
		assertEquals("3:06", result);

		result = StringUtils.formatHMS(15);
		assertEquals("15", result);

		result = StringUtils.formatHMS(7268);
		assertEquals("2:01:08", result);
	}

	public void testAddLeadingZero() {
		assertEquals("04", StringUtils.addLeadingZero(4));
		assertEquals("14", StringUtils.addLeadingZero(14));
	}

}
