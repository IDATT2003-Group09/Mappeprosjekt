package edu.ntnu.iir.bidata.idatt2003.group09.base.news;

import java.io.Serializable;

public abstract class Event implements Serializable {

	private final String headline;
	private final String description;

	protected Event(String headline, String description) {
		if (headline == null || headline.isBlank()) {
			throw new IllegalArgumentException("Headline cannot be null or empty");
		}
		if (description == null || description.isBlank()) {
			throw new IllegalArgumentException("Description cannot be null or empty");
		}
		this.headline = headline;
		this.description = description;
	}

	public String getHeadline() {
		return headline;
	}

	public String getDescription() {
		return description;
	}
}
