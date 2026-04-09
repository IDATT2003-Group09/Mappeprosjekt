package edu.ntnu.iir.bidata.idatt2003.group09.base.news;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class EventFactory {

  public EventFactory() {

  }

  public List<Event> generateGlobalEvents(){
    List<Event> events = new ArrayList<>();

    GlobalEvent event1 = new GlobalEvent("Tech Boom", "The technology sector is experiencing unprecedented growth, with new innovations driving market expansion.");
    event1.addEventData("Tech", BigDecimal.valueOf(0.8));
    event1.addEventData("Energy", BigDecimal.valueOf(0.4));
    events.add(event1);

    GlobalEvent event2 = new GlobalEvent("Financial Crisis", "A major financial institution has collapsed, sending shockwaves through global markets and causing widespread panic.");
    event2.addEventData("Finance", BigDecimal.valueOf(-0.9));
    event2.addEventData("Real Estate", BigDecimal.valueOf(0.6));
    events.add(event2);

    return events;

  }

  public List<Event> generateStockSpecificEvents(){
    List<Event> events = new ArrayList<>();

    StockSpecificEvent event1 = new StockSpecificEvent(
            "{stock} reports record earnings",
            "{stock} has reported record-breaking earnings for the quarter, exceeding analyst expectations.",
            BigDecimal.valueOf(0.15)
    );
    event1.addStockImpact("AAPL", BigDecimal.valueOf(0.20));
    events.add(event1);

    StockSpecificEvent event2 = new StockSpecificEvent(
            "{stock} faces regulatory investigation",
            "{stock} is under investigation by regulatory authorities, raising concerns about potential legal issues.",
            BigDecimal.valueOf(-0.25)
    );
    event2.addStockImpact("GOOGL", BigDecimal.valueOf(-0.30));
    events.add(event2);

    return events;
  }
}
