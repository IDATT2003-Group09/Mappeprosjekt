package edu.ntnu.iir.bidata.idatt2003.group09.base.news;

import edu.ntnu.iir.bidata.idatt2003.group09.base.Stock;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class NewsPaper implements Serializable {

	private static final int GLOBAL_EVENT_COUNT = 2;
	private static final int MIN_STOCK_SPECIFIC_EVENT_COUNT = 2;
	private static final int MAX_STOCK_SPECIFIC_EVENT_COUNT = 3;

	private final List<GlobalEvent> globalEvents;
	private final List<StockSpecificEvent> stockSpecificEvents;

	public NewsPaper(List<GlobalEvent> globalEvents, List<StockSpecificEvent> stockSpecificEvents) {
		if (globalEvents == null || globalEvents.size() != GLOBAL_EVENT_COUNT || globalEvents.stream().anyMatch(event -> event == null)) {
			throw new IllegalArgumentException("Newspaper must contain exactly 2 global events");
		}
		if (stockSpecificEvents == null
				|| stockSpecificEvents.size() < MIN_STOCK_SPECIFIC_EVENT_COUNT
				|| stockSpecificEvents.size() > MAX_STOCK_SPECIFIC_EVENT_COUNT) {
			throw new IllegalArgumentException("Newspaper must contain between 2 and 3 stock specific events");
		}

		this.globalEvents = new ArrayList<>(globalEvents);
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
		if (globalEvents.size() < GLOBAL_EVENT_COUNT) {
			throw new IllegalStateException("EventFactory must provide at least 2 global events");
		}

		List<Event> stockTemplates = eventFactory.generateStockSpecificEvents();
		if (stockTemplates.isEmpty()) {
			throw new IllegalStateException("EventFactory must provide stock specific event templates");
		}

		List<Event> availableGlobalEvents = new ArrayList<>(globalEvents);
		List<GlobalEvent> selectedGlobals = new ArrayList<>(GLOBAL_EVENT_COUNT);

		for (int index = 0; index < GLOBAL_EVENT_COUNT; index++) {
			int globalIndex = random.nextInt(availableGlobalEvents.size());
			selectedGlobals.add((GlobalEvent) availableGlobalEvents.remove(globalIndex));
		}

		List<StockSpecificEvent> selectedSpecific = new ArrayList<>();
		List<Event> availableTemplates = new ArrayList<>(stockTemplates);
		int stockSpecificEventCount =
				random.nextInt(MAX_STOCK_SPECIFIC_EVENT_COUNT - MIN_STOCK_SPECIFIC_EVENT_COUNT + 1)
						+ MIN_STOCK_SPECIFIC_EVENT_COUNT;

		for (int index = 0; index < stockSpecificEventCount; index++) {
			if (availableTemplates.isEmpty()) {
				availableTemplates = new ArrayList<>(stockTemplates);
			}

			int templateIndex = random.nextInt(availableTemplates.size());
			StockSpecificEvent template = (StockSpecificEvent) availableTemplates.remove(templateIndex);
			Stock stock = stocks.get(random.nextInt(stocks.size()));
			selectedSpecific.add(template.createForStock(stock));
		}

		return new NewsPaper(selectedGlobals, selectedSpecific);
	}

	public List<GlobalEvent> getGlobalEvents() {
		return Collections.unmodifiableList(globalEvents);
	}

	public GlobalEvent getGlobalEvent() {
		return globalEvents.get(0);
	}

	public List<StockSpecificEvent> getStockSpecificEvents() {
		return Collections.unmodifiableList(stockSpecificEvents);
	}

	public BigDecimal getImpactForStock(Stock stock) {
		BigDecimal totalImpact = BigDecimal.ZERO;

		for (GlobalEvent globalEvent : globalEvents) {
			totalImpact = totalImpact.add(globalEvent.getImpactForSector(stock.getSector()));
		}

		for (StockSpecificEvent stockSpecificEvent : stockSpecificEvents) {
			totalImpact = totalImpact.add(stockSpecificEvent.getImpactForStock(stock));
		}

		return totalImpact;
	}
}
