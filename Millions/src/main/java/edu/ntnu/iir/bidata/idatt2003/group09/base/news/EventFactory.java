package edu.ntnu.iir.bidata.idatt2003.group09.base.news;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import edu.ntnu.iir.bidata.idatt2003.group09.base.Stock;

public class EventFactory implements Serializable {

  public EventFactory() {

  }

  public List<Event> generateGlobalEvents(){
    List<Event> events = new ArrayList<>();

    GlobalEvent event1 = new GlobalEvent("Tech Boom", "The technology sector is experiencing unprecedented growth, with new innovations driving market expansion.");
    event1.addEventData("Tech", BigDecimal.valueOf(0.8));
    event1.addEventData("Energy", BigDecimal.valueOf(0.4));
    events.add(event1);

    GlobalEvent event2 = new GlobalEvent("Financial Crisis", "A major financial institution has collapsed, sending shockwaves through global markets and causing widespread panic.");
    event2.addEventData("Financial", BigDecimal.valueOf(-0.9));
    event2.addEventData("Real Estate", BigDecimal.valueOf(0.6));
    events.add(event2);

    return events;

  }

  public List<Event> generateStockSpecificEvents(){
    List<Event> events = new ArrayList<>();

    StockSpecificEvent event1 = new StockSpecificEvent(
            "{stock} to the moon",
            "{stock} has reported record-breaking earnings for the quarter, exceeding analyst expectations.",
            BigDecimal.valueOf(0.15)
    );
    events.add(event1);

    StockSpecificEvent event2 = new StockSpecificEvent(
            "{stock} faces regulatory investigation",
            "{stock} is under investigation by regulatory authorities, raising concerns about potential legal issues.",
            BigDecimal.valueOf(-0.25)
    );
    events.add(event2);

        StockSpecificEvent event3 = new StockSpecificEvent(
          "{stock} secures major long-term contract",
          "{stock} has secured a long-term contract that is expected to stabilize revenue for years.",
          BigDecimal.valueOf(0.10)
        );
        events.add(event3);

    return events;
  }

  public StockSpecificEvent applyRandomStockToEvent(StockSpecificEvent event, List<Stock> stocks){
    if (stocks.isEmpty()) {
      throw new IllegalArgumentException("Stock list cannot be empty");
    }
    int randomIndex = (int) (Math.random() * stocks.size());
    Stock randomStock = stocks.get(randomIndex);
    event.addStock(randomStock);
    return event;
  }
}
