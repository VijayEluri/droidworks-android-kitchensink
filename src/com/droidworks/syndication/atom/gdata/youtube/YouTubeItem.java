package com.droidworks.syndication.atom.gdata.youtube;

import java.util.Date;

public class YouTubeItem {

	public final Date datePublished;
	public final String title;
	public final String description;
	public final String youtubeUrl;
	public final long duration;
	public final String thumbnailUrl;
	public final String youtubeId;

	public YouTubeItem(Date datePublished, String title, String description,
			String youtubeUrl, long duration, String thumbnailUrl,
			String youtubeId) {

		super();

		this.datePublished = datePublished;
		this.title = title;
		this.description = description;
		this.youtubeUrl = youtubeUrl;
		this.duration = duration;
		this.thumbnailUrl = thumbnailUrl;
		this.youtubeId = youtubeId;
	}


}
