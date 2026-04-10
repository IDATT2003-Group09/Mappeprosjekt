package edu.ntnu.iir.bidata.idatt2003.group09.base.news;

import edu.ntnu.iir.bidata.idatt2003.group09.base.Stock;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class NewsPaper implements Serializable {

	private static final int STOCK_SPECIFIC_EVENT_COUNT = 3;

	private final GlobalEvent globalEvent;
	private final List<StockSpecificEvent> stockSpecificEvents;

	public NewsPaper(GlobalEvent globalEvent, List<StockSpecificEvent> stockSpecificEvents) {
		if (globalEvent == null) {
			throw new IllegalArgumentException("Global event cannot be null");
		}
		if (stockSpecificEvents == null || stockSpecificEvents.size() != STOCK_SPECIFIC_EVENT_COUNT) {
			throw new IllegalArgumentException("Newspaper must contain exactly 3 stock specific events");
		}

		this.globalEvent = globalEvent;
		this.stockSpecificEvents = new ArrayList<>(stockSpecificEvents);
	}

	public static NewsPaper create(EventFactory eventFactory, List<Stock> stocks, Random random) {
		if (eventFactory == null) {
			throw new IllegalArgumentException("EventFactory cannot be null");
		}
		if (stocks == null || stocks.isEmpty()) {
			throw new IllegalArgumentException("Stocks cannot be null or empty");
		}

		List<Event> globalEvents = eventFactory.generateGlobalEvents();
		if (globalEvents.isEmpty()) {
			throw new IllegalStateException("EventFactory must provide at least one global event");
		}

		List<Event> stockTemplates = eventFactory.generateStockSpecificEvents();
		if (stockTemplates.isEmpty()) {
			throw new IllegalStateException("EventFactory must provide stock specific event templates");
		}

		GlobalEvent selectedGlobal = (GlobalEvent) globalEvents.get(random.nextInt(globalEvents.size()));
		List<StockSpecificEvent> selectedSpecific = new ArrayList<>();
		List<Event> availableTemplates = new ArrayList<>(stockTemplates);

		for (int index = 0; index < STOCK_SPECIFIC_EVENT_COUNT; index++) {
			if (availableTemplates.isEmpty()) {
				availableTemplates = new ArrayList<>(stockTemplates);
			}

			int templateIndex = random.nextInt(availableTemplates.size());
			StockSpecificEvent template = (StockSpecificEvent) availableTemplates.remove(templateIndex);
			Stock stock = stocks.get(random.nextInt(stocks.size()));
			selectedSpecific.add(template.createForStock(stock));
		}

		return new NewsPaper(selectedGlobal, selectedSpecific);
	}

	public GlobalEvent getGlobalEvent() {
		return globalEvent;
	}

	public List<StockSpecificEvent> getStockSpecificEvents() {
		return Collections.unmodifiableList(stockSpecificEvents);
	}

	public BigDecimal getImpactForStock(Stock stock) {
		BigDecimal totalImpact = globalEvent.getImpactForSector(stock.getSector());

		for (StockSpecificEvent stockSpecificEvent : stockSpecificEvents) {
			totalImpact = totalImpact.add(stockSpecificEvent.getImpactForStock(stock));
		}

		return totalImpact;
	}
}
