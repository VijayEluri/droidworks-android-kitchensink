package com.droidworks.syndication.atom.gdata.youtube;

import java.util.Date;

public class YouTubeItem {

	public final Date datePublished;
	public final String title;
	public final String description;
	public final String author;
	public final String authorUrl;
	public final String youtubeUrl;
	public final String duration;
	public final String rstpLink;
	public final String thumbnailUrl;
	public final String youtubeId;

	public YouTubeItem(Date datePublished, String title, String description,
			String author, String authorUrl, String youtubeUrl,
			String duration, String rstpLink, String thumbnailUrl,
			String youtubeId) {

		super();

		this.datePublished = datePublished;
		this.title = title;
		this.description = description;
		this.author = author;
		this.authorUrl = authorUrl;
		this.youtubeUrl = youtubeUrl;
		this.duration = duration;
		this.rstpLink = rstpLink;
		this.thumbnailUrl = thumbnailUrl;
		this.youtubeId = youtubeId;
	}


}
