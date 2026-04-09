package edu.ntnu.iir.bidata.idatt2003.group09.base.news;

import java.util.HashMap;
import java.math.BigDecimal;

public class Event {
  
  private HashMap<String, BigDecimal> eventData;
  private String headline;
  private String description;

  public Event(String headline, String description) {
    this.headline = headline;
    this.description = description;
    eventData = new HashMap<>();
  }

  public void addEventData(String key, BigDecimal value){
    eventData.put(key, value);
  }

  public HashMap<String, BigDecimal> getEventData() {
    return eventData;
  }
}