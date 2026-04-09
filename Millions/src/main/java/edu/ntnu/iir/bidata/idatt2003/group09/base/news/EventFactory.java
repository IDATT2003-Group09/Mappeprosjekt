package edu.ntnu.iir.bidata.idatt2003.group09.base.news;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class EventFactory {

  private List<String> tags = List.of("Tech", "Finance", "Health", "Energy", "Consumer", "Industrial", "Utilities", "Real Estate", "Materials", "Telecom");

  public EventFactory() {

  }

  public List<Event> generateEvents(){
    List<Event> events = new ArrayList<>();

    Event event1 = new Event("Tech Boom", "The technology sector is experiencing unprecedented growth, with new innovations driving market expansion.");
    event1.addEventData("Tech", BigDecimal.valueOf(0.8));
    event1.addEventData("Energy", BigDecimal.valueOf(0.4));
    events.add(event1);

    Event event2 = new Event("Financial Crisis", "A major financial institution has collapsed, sending shockwaves through global markets and causing widespread panic.");
    event2.addEventData("Finance", BigDecimal.valueOf(0.9));
    event2.addEventData("Real Estate", BigDecimal.valueOf(0.6));
    events.add(event2);

    return events;

  }
}
