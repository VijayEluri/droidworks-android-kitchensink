package com.droidworks.parsers.rss.itunes;

import com.droidworks.util.StringUtils;

public class DurationParser {

	public long parse(String duration) {
		int colonCount = StringUtils.charCount(duration, ':');
		long seconds = 0;

		// handle seconds
		if (colonCount == 0) {
			seconds = Long.parseLong(duration);
		}
		else if (colonCount == 1) {
			String[] parts = duration.split(":");
			seconds = Integer.parseInt(parts[0]) * 60;
			seconds += Integer.parseInt(parts[1]);
		}
		else if (colonCount == 2) {
			// handle hh:mm:ss
			String[] parts = duration.split(":");
			seconds = Integer.parseInt(parts[0]) * 3600;
			seconds += Integer.parseInt(parts[1]) * 60;
			seconds += Integer.parseInt(parts[2]);
		}
		else {
			throw new RuntimeException("Failure parsing duration");
		}

		return seconds;
	}


}
