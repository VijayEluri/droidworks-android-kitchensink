package com.droidworks.util;

import android.test.InstrumentationTestCase;

public class StringUtilsTest extends InstrumentationTestCase {

	public void testCharCount() {
		assertEquals(3, StringUtils.charCount("xcxcxc", 'x'));
	}

}
