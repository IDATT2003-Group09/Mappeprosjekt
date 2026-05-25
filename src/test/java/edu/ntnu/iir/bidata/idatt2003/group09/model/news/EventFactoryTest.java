package edu.ntnu.iir.bidata.idatt2003.group09.model.news;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

public class EventFactoryTest {

  @Test
  void generateGlobalEventsShouldReturnGlobalEventList() {
    EventFactory factory = new EventFactory();

    List<GlobalEvent> globalEvents = factory.generateGlobalEvents();

    assertFalse(globalEvents.isEmpty());
    assertTrue(globalEvents.stream().allMatch(GlobalEvent.class::isInstance));
  }

  @Test
  void generateStockSpecificEventsShouldReturnTemplateList() {
    EventFactory factory = new EventFactory();

    List<StockSpecificEvent> stockSpecificEvents = factory.generateStockSpecificEvents();

    assertFalse(stockSpecificEvents.isEmpty());
    assertTrue(stockSpecificEvents.stream().allMatch(StockSpecificEvent.class::isInstance));
  }
}
