package edu.ntnu.iir.bidata.idatt2003.group09.model.news;

import java.util.HashMap;
import java.util.Locale;
import java.math.BigDecimal;

public class GlobalEvent extends Event {
  
  private HashMap<String, BigDecimal> eventData;

  public GlobalEvent(String headline, String description) {
    super(headline, description);
    eventData = new HashMap<>();
  }

  public void addEventData(String key, BigDecimal value){
    eventData.put(key, value);
  }

  public HashMap<String, BigDecimal> getEventData() {
    return eventData;
  }

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

  public BigDecimal getAverageImpact() {
    if (eventData.isEmpty()) {
      return BigDecimal.ZERO;
    }

    BigDecimal total = BigDecimal.ZERO;
    for (BigDecimal value : eventData.values()) {
      total = total.add(value);
    }

    return total.divide(BigDecimal.valueOf(eventData.size()), 6, java.math.RoundingMode.HALF_UP);
  }
}