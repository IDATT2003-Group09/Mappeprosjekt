package edu.ntnu.iir.bidata.idatt2003.group09.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Representerer en enkelt markedsnyhet med sektor og prispåvirkning.
 */
public class MarketNews implements Serializable {

  private final String headline;
  private final String description;
  private final String affectedSector;
  private final BigDecimal impact;

  /**
   * Oppretter en ny markedsnyhet.
   *
   * @param headline overskrift for nyheten
   * @param description beskrivende tekst
   * @param affectedSector sektoren som påvirkes
   * @param impact estimert prosentvis påvirkning som desimaltall
   */
  public MarketNews(String headline, String description, String affectedSector, BigDecimal impact) {
    this.headline = headline;
    this.description = description;
    this.affectedSector = affectedSector;
    this.impact = impact;
  }

  /**
   * Henter overskriften til nyheten.
   *
   * @return nyhetsoverskrift
   */
  public String getHeadline() {
    return headline;
  }

  /**
   * Henter utfyllende beskrivelse.
   *
   * @return beskrivelsestekst
   */
  public String getDescription() {
    return description;
  }

  /**
   * Henter navnet på påvirket sektor.
   *
   * @return sektornavn
   */
  public String getAffectedSector() {
    return affectedSector;
  }

  /**
   * Henter påvirkningsverdi for nyheten.
   *
   * @return påvirkning som desimaltall
   */
  public BigDecimal getImpact() {
    return impact;
  }
}
