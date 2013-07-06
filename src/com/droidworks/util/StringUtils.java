package com.droidworks.util;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	@Deprecated
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
	   */
	  public static String md5String(String text) {

	    MessageDigest algorithm;
		try {
			algorithm = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			return null;
		}

	    algorithm.update(text.getBytes());

	    byte[] digest = algorithm.digest();

	    StringBuilder hexString = new StringBuilder();
	    for (int i=0; i < digest.length; i++) {
	      hexString.append(Integer.toHexString(0xFF & digest[i]));
	    }

	    return hexString.toString();
	  }

	  // just a simple method to transform values like 4 to 04, but
	  // values like 14 will come back as 14.
	  private static void appendWithLeadingZero(StringBuilder sb, long value) {
		  if (value < 10) {
			  sb.append("0").append(value);
          }
          else {
              sb.append(value);
          }
	  }


	  public static String formatHMS(StringBuilder duration, long seconds) {

          // delete everyting in the string
		  duration.delete(0, duration.length());

		  // handle hh:mm:ss
		  if (seconds >= 3600) {
			  String hours = Long.toString(seconds / 3600);
			  long leftover = seconds % 3600;

			  duration.append(hours).append(":");
              appendWithLeadingZero(duration, leftover / 60);
              duration.append(":");
              appendWithLeadingZero(duration, (leftover % 60));
		  }
		  // handle mm:ss
		  else if (seconds >= 60) {
			  duration.append(Long.toString(seconds / 60)).append(":");
              appendWithLeadingZero(duration, (seconds % 60));
		  }
		  // handle seconds only
		  else {
			  duration.append("0:");
              appendWithLeadingZero(duration, seconds);
		  }

		  return duration.toString();
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

	/**
	 * Method to santize a path segment for a fat32 filesystem.  Replaces
	 * illegal characters with the supplied replacement character (which
	 * must be safe as well).
	 *
	 * @param segment
	 * @param replace
	 * @return
	 */
	public static String sanitizeFat32(String segment, String replace) {
		String evilChars = "\\\\|/|:|;|\\*|\\?|\"|<|>|\\|";
		Pattern pattern = Pattern.compile(evilChars);
		Matcher matcher = pattern.matcher(segment);
		return matcher.replaceAll(replace);
	}

}
