package edu.ntnu.iir.bidata.idatt2003.group09.base.news;

import java.util.HashMap;
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
}