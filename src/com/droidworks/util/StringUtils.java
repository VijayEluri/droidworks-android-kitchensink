package com.droidworks.util;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Iterator;

public class StringUtils {

	/**
	 * Given a string, returns the number of occurances of a char.
	 *
	 * @param subject
	 * @param match
	 * @return
	 */
	public static int charCount(String subject, char match) {
		int count = 0;
		for (char c : subject.toCharArray()) {
			if (c == match)
				count++;
		}

		return count;
	}

	// converts an InputStream into a stream
	public static String slurp (InputStream in) throws IOException {
	    StringBuffer out = new StringBuffer();
	    byte[] b = new byte[4096];
	    for (int n; (n = in.read(b)) != -1;) {
	        out.append(new String(b, 0, n));
	    }
	    return out.toString();
	}

	// reads 1 line from an input stream
	public static String readLine(InputStream in) throws IOException {
		StringBuffer out = new StringBuffer();
		char c;

		while ( (c = (char)in.read()) != -1 ) {
			if (c == '\n')
				return out.toString();
			else
				out.append(c);
		}

		return out.toString();
	}

	// simple test to make sure a string is not null and
	// is not zero length
	public static boolean hasChars(String s) {
		if (s == null)
			return false;

		if (s.length() <= 0)
			return false;

		return true;
	}

	// implode operation
	public static String implode(Collection<String> c, String seperator) {
		StringBuffer sb = new StringBuffer();
		Iterator<String> it = c.iterator();
		while (it.hasNext()) {
			sb.append(it.next());
			if (it.hasNext())
				sb.append(seperator);
		}
		return sb.toString();
	}

	  /**
	   * Method to return the md5 of a supplied string.
	   *
	   * @param text the string you want to make a digest for.
	   * @return the generated md5
	   * @throws NoSuchAlgorithmException
	   */
	  public static String md5String(String text) throws NoSuchAlgorithmException{

	    MessageDigest algorithm = MessageDigest.getInstance("MD5");
	    algorithm.update(text.getBytes());

	    byte[] digest = algorithm.digest();

	    StringBuilder hexString = new StringBuilder();
	    for (int i=0; i < digest.length; i++) {
	      hexString.append(Integer.toHexString(0xFF & digest[i]));
	    }

	    return hexString.toString();
	  }


		/**
		 * Formats a duration in miliseconds to M:S format. For example,
		 * 2000 milis would return "0:02"
		 *
		 * @param milis
		 * @return
		 */
		public static String getFormattedTime(long milis) {
			String minutes = String.valueOf( (int) (milis / 60000));
			String seconds = String.valueOf( (int)(milis % 60000) / 1000);

			while (seconds.length() < 2)
				seconds = "0" + seconds;

			return minutes + ":" + seconds;
		}

}
