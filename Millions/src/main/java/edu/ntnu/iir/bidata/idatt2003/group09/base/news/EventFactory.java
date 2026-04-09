package edu.ntnu.iir.bidata.idatt2003.group09.base.news;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class EventFactory {

  public EventFactory() {

  }

  public List<Event> generateEvents(){
    List<Event> events = new ArrayList<>();

    GlobalEvent event1 = new GlobalEvent("Tech Boom", "The technology sector is experiencing unprecedented growth, with new innovations driving market expansion.");
    event1.addEventData("Tech", BigDecimal.valueOf(0.8));
    event1.addEventData("Energy", BigDecimal.valueOf(0.4));
    events.add(event1);

    GlobalEvent event2 = new GlobalEvent("Financial Crisis", "A major financial institution has collapsed, sending shockwaves through global markets and causing widespread panic.");
    event2.addEventData("Finance", BigDecimal.valueOf(0.9));
    event2.addEventData("Real Estate", BigDecimal.valueOf(0.6));
    events.add(event2);

    StockSpecificEvent event3 = new StockSpecificEvent(
            "{stock} announces major partnership",
            "{stock} has signed a strategic partnership expected to improve growth outlook."
    );
    event3.addStockImpact("AAPL", BigDecimal.valueOf(0.12));
    event3.addStockImpact("MSFT", BigDecimal.valueOf(0.08));
    events.add(event3);

    return events;

  }
}
