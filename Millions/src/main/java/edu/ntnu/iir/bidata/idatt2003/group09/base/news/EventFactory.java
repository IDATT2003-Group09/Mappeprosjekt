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
    event1.addEventData("Tech", BigDecimal.valueOf(0.06));
    event1.addEventData("Energy", BigDecimal.valueOf(0.03));
    events.add(event1);

    GlobalEvent event2 = new GlobalEvent("Financial Crisis", "A major financial institution has collapsed, sending shockwaves through global markets and causing widespread panic.");
    event2.addEventData("Financial", BigDecimal.valueOf(-0.05));
    event2.addEventData("Real Estate", BigDecimal.valueOf(0.05));
    events.add(event2);

      GlobalEvent e3 = new GlobalEvent("Interest Rate Cuts", "Central banks lower interest rates to stimulate economic growth.");
      e3.addEventData("Financial", BigDecimal.valueOf(0.04));
      e3.addEventData("Technology", BigDecimal.valueOf(0.03));
      events.add(e3);

      GlobalEvent e4 = new GlobalEvent("Interest Rate Hike", "Central banks increase interest rates to combat inflation.");
      e4.addEventData("Financial", BigDecimal.valueOf(-0.02));
      e4.addEventData("Real Estate", BigDecimal.valueOf(-0.03));
      events.add(e4);

      GlobalEvent e5 = new GlobalEvent("Oil Price Surge", "Oil prices spike due to geopolitical tensions.");
      e5.addEventData("Energy", BigDecimal.valueOf(0.05));
      e5.addEventData("Consumer", BigDecimal.valueOf(-0.02));
      events.add(e5);

      GlobalEvent e6 = new GlobalEvent("Global Supply Chain Recovery", "Supply chains stabilize, improving production and delivery worldwide.");
      e6.addEventData("Industrial", BigDecimal.valueOf(0.05));
      e6.addEventData("Retail", BigDecimal.valueOf(0.02));
      events.add(e6);

      GlobalEvent e7 = new GlobalEvent("Recession Fears", "Economists warn of a potential global recession.");
      e7.addEventData("Financial", BigDecimal.valueOf(-0.03));
      e7.addEventData("Consumer", BigDecimal.valueOf(-0.02));
      events.add(e7);

      GlobalEvent e8 = new GlobalEvent("AI Breakthrough", "Major advancements in artificial intelligence drive optimism in tech markets.");
      e8.addEventData("Technology", BigDecimal.valueOf(0.07));
      events.add(e8);

      GlobalEvent e9 = new GlobalEvent("Cybersecurity Threat", "Large-scale cyber attacks raise concerns across industries.");
      e9.addEventData("Technology", BigDecimal.valueOf(-0.03));
      events.add(e9);

      GlobalEvent e10 = new GlobalEvent("Green Energy Push", "Governments increase investments in renewable energy.");
      e10.addEventData("Energy", BigDecimal.valueOf(0.07));
      events.add(e10);

      GlobalEvent e11 = new GlobalEvent("Housing Market Slowdown", "Rising interest rates slow down the housing market.");
      e11.addEventData("Real Estate", BigDecimal.valueOf(-0.03));
      events.add(e11);

      GlobalEvent e12 = new GlobalEvent("Consumer Spending Boom", "Consumers increase spending, boosting retail and services.");
      e12.addEventData("Retail", BigDecimal.valueOf(0.05));
      events.add(e12);

      GlobalEvent e13 = new GlobalEvent("Trade War Escalation", "New tariffs disrupt global trade.");
      e13.addEventData("Industrial", BigDecimal.valueOf(-0.03));
      events.add(e13);

      GlobalEvent e14 = new GlobalEvent("Healthcare Innovation", "New medical breakthroughs improve industry outlook.");
      e14.addEventData("Healthcare", BigDecimal.valueOf(0.05));
      events.add(e14);

      GlobalEvent e15 = new GlobalEvent("Banking Stability Concerns", "Concerns over banking stability spread uncertainty.");
      e15.addEventData("Financial", BigDecimal.valueOf(-0.03));
      events.add(e15);

      GlobalEvent e16 = new GlobalEvent("Strong Job Market", "Employment rates improve, boosting economic confidence.");
      e16.addEventData("Consumer", BigDecimal.valueOf(0.06));
      events.add(e16);

      GlobalEvent e17 = new GlobalEvent("Inflation Spike", "Inflation rises faster than expected.");
      e17.addEventData("Consumer", BigDecimal.valueOf(-0.02));
      e17.addEventData("Financial", BigDecimal.valueOf(-0.02));
      events.add(e17);

    return events;

  }

  public List<Event> generateStockSpecificEvents(){
    List<Event> events = new ArrayList<>();

    StockSpecificEvent event1 = new StockSpecificEvent(
            "{stock} to the moon",
            "{stock} has reported record-breaking earnings for the quarter, exceeding analyst expectations.",
            BigDecimal.valueOf(0.015)
    );
    events.add(event1);

    StockSpecificEvent event2 = new StockSpecificEvent(
            "{stock} faces regulatory investigation",
            "{stock} is under investigation by regulatory authorities, raising concerns about potential legal issues.",
            BigDecimal.valueOf(-0.025)
    );
    events.add(event2);

        StockSpecificEvent event3 = new StockSpecificEvent(
          "{stock} secures major long-term contract",
          "{stock} has secured a long-term contract that is expected to stabilize revenue for years.",
          BigDecimal.valueOf(0.01)
        );
        events.add(event3);

      events.add(new StockSpecificEvent(
              "{stock} beats earnings expectations",
              "{stock} reported earnings above expectations, boosting investor confidence.",
              BigDecimal.valueOf(0.02)
      ));

      events.add(new StockSpecificEvent(
              "{stock} misses earnings",
              "{stock} reported disappointing earnings results.",
              BigDecimal.valueOf(-0.02)
      ));

      events.add(new StockSpecificEvent(
              "{stock} announces layoffs",
              "{stock} announces workforce reductions to cut costs.",
              BigDecimal.valueOf(-0.015)
      ));

      events.add(new StockSpecificEvent(
              "{stock} launches new product",
              "{stock} unveils a new product expected to drive revenue growth.",
              BigDecimal.valueOf(0.015)
      ));

      events.add(new StockSpecificEvent(
              "{stock} CEO resigns",
              "{stock}'s CEO steps down unexpectedly.",
              BigDecimal.valueOf(-0.02)
      ));

      events.add(new StockSpecificEvent(
              "{stock} expands internationally",
              "{stock} expands into new international markets.",
              BigDecimal.valueOf(0.02)
      ));

      events.add(new StockSpecificEvent(
              "{stock} faces lawsuit",
              "{stock} is hit with a major lawsuit.",
              BigDecimal.valueOf(-0.025)
      ));

      events.add(new StockSpecificEvent(
              "{stock} upgrades outlook",
              "{stock} raises its future guidance.",
              BigDecimal.valueOf(0.02)
      ));

      events.add(new StockSpecificEvent(
              "{stock} downgraded by analysts",
              "{stock} receives a downgrade from major analysts.",
              BigDecimal.valueOf(-0.02)
      ));

      events.add(new StockSpecificEvent(
              "{stock} announces stock buyback",
              "{stock} announces a share buyback program.",
              BigDecimal.valueOf(0.015)
      ));

      events.add(new StockSpecificEvent(
              "{stock} supply issues",
              "{stock} faces supply chain disruptions.",
              BigDecimal.valueOf(-0.015)
      ));

      events.add(new StockSpecificEvent(
              "{stock} strategic partnership",
              "{stock} enters a strategic partnership.",
              BigDecimal.valueOf(0.015)
      ));

      events.add(new StockSpecificEvent(
              "{stock} data breach",
              "{stock} suffers a major data breach.",
              BigDecimal.valueOf(-0.03)
      ));

      events.add(new StockSpecificEvent(
              "{stock} strong quarterly growth",
              "{stock} reports strong quarterly growth.",
              BigDecimal.valueOf(0.02)
      ));

      events.add(new StockSpecificEvent(
              "{stock} regulatory approval",
              "{stock} receives regulatory approval for a key product.",
              BigDecimal.valueOf(0.02)
      ));

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
