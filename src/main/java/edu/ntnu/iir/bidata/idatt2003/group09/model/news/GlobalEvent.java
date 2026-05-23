package edu.ntnu.iir.bidata.idatt2003.group09.model.news;

import java.util.HashMap;
import java.util.Locale;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Global nyhetshendelse med påvirkning per sektor.
 */
public class GlobalEvent extends Event {
  
  private HashMap<String, BigDecimal> eventData;

  /**
   * Oppretter en global hendelse.
   *
   * @param headline overskrift for hendelsen
   * @param description beskrivelse av hendelsen
   */
  public GlobalEvent(String headline, String description) {
    super(headline, description);
    eventData = new HashMap<>();
  }

  /**
   * Legger til sektorpåvirkning for hendelsen.
   *
   * @param key sektornavn
   * @param value påvirkning for sektoren
   */
  public void addEventData(String key, BigDecimal value){
    eventData.put(key, value);
  }

  /**
   * Henter alle sektorpåvirkninger.
   *
   * @return map med sektornavn og påvirkning
   */
  public HashMap<String, BigDecimal> getEventData() {
    return eventData;
  }

  /**
   * Henter påvirkning for en sektor, med enkel tekstmatching.
   *
   * @param sector sektoren som skal sjekkes
   * @return påvirkning, eller {@code BigDecimal.ZERO} hvis ingen treff
   */
  public BigDecimal getImpactForSector(String sector) {
    if (sector == null || sector.isBlank()) {
      return BigDecimal.ZERO;
    }

    String normalizedSector = sector.toLowerCase(Locale.ROOT);

    for (var entry : eventData.entrySet()) {
      String key = entry.getKey().toLowerCase(Locale.ROOT);
      if (normalizedSector.equals(key)
          || normalizedSector.contains(key)
          || key.contains(normalizedSector)) {
        return entry.getValue();
      }
    }

    return BigDecimal.ZERO;
  }

  /**
   * Beregner gjennomsnittlig påvirkning på tvers av alle sektorer.
   *
   * @return gjennomsnittlig påvirkning, eller {@code BigDecimal.ZERO} uten data
   */
  public BigDecimal getAverageImpact() {
    if (eventData.isEmpty()) {
      return BigDecimal.ZERO;
    }

    BigDecimal total = BigDecimal.ZERO;
    for (BigDecimal value : eventData.values()) {
      total = total.add(value);
    }

    return total.divide(BigDecimal.valueOf(eventData.size()), 6, RoundingMode.HALF_UP);
  }
}