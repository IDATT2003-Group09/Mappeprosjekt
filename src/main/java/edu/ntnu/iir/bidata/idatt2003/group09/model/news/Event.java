package edu.ntnu.iir.bidata.idatt2003.group09.model.news;

import java.io.Serializable;

/**
 * Abstrakt basisklasse for nyhetshendelser som vises i spillet.
 */
public abstract class Event implements Serializable {

	private final String headline;
	private final String description;

	/**
	 * Oppretter en hendelse med overskrift og beskrivelse.
	 *
	 * @param headline kort tittel på hendelsen
	 * @param description utfyllende beskrivelse av hendelsen
	 */
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

	/**
	 * Henter overskriften for hendelsen.
	 *
	 * @return overskrift
	 */
	public String getHeadline() {
		return headline;
	}

	/**
	 * Henter beskrivelsen for hendelsen.
	 *
	 * @return beskrivelse
	 */
	public String getDescription() {
		return description;
	}
}
