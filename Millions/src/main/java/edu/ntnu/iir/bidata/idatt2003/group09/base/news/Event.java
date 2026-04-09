package edu.ntnu.iir.bidata.idatt2003.group09.base.news;

public abstract class Event {

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
