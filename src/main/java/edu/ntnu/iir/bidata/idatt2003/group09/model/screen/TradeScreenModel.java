
package edu.ntnu.iir.bidata.idatt2003.group09.model.screen;

import edu.ntnu.iir.bidata.idatt2003.group09.model.Stock;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Share;
import edu.ntnu.iir.bidata.idatt2003.group09.model.calculator.PurchaseCalculator;
import edu.ntnu.iir.bidata.idatt2003.group09.model.game.GameProgress;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Player;
import java.util.*;
import java.util.stream.Collectors;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Consumer;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Model for the Trade Screen (MVC).
 * Holds trade-related data and business logic for the trade screen.
 */
public class TradeScreenModel {
	public enum TradeEvent {
		BUY_SUCCESS,
		SELL_SUCCESS
	}

	private List<Stock> allStocks;
	private Set<String> selectedSectors;

	// Observable UI-backed state
	private final ObjectProperty<BigDecimal> cash = new SimpleObjectProperty<>(BigDecimal.ZERO);
	private final ObjectProperty<BigDecimal> netWorth = new SimpleObjectProperty<>(BigDecimal.ZERO);
	private final IntegerProperty week = new SimpleIntegerProperty(0);
	private final IntegerProperty holdings = new SimpleIntegerProperty(0);
	private final StringProperty quarterLabel = new SimpleStringProperty("");
	private final ObjectProperty<BigDecimal> requirementOverlayValue = new SimpleObjectProperty<>(BigDecimal.ZERO);
	private final ObjectProperty<BigDecimal> netWorthOverlayValue = new SimpleObjectProperty<>(BigDecimal.ZERO);
	private final DoubleProperty progress = new SimpleDoubleProperty(0);
	private final StringProperty deadlineLabel = new SimpleStringProperty("");

	private final ObservableList<Stock> filteredStocks = FXCollections.observableArrayList();
	private final List<Consumer<TradeEvent>> tradeEventListeners = new ArrayList<>();

	public TradeScreenModel(List<Stock> stocks) {
		this.allStocks = new ArrayList<>(stocks);
		this.selectedSectors = new HashSet<>();
		this.filteredStocks.setAll(this.allStocks);
	}

	public void setStocks(List<Stock> stocks) {
		this.allStocks = new ArrayList<>(stocks);
	}

	public List<Stock> getAllStocks() {
		return allStocks;
	}

    public ObservableList<Stock> getFilteredStocks() {
        return filteredStocks;
    }

	public Set<String> getAllSectors() {
		return allStocks.stream()
			.map(Stock::getSector)
			.filter(sector -> sector != null && !sector.isEmpty())
			.collect(Collectors.toSet());
	}

	public Set<String> getSelectedSectors() {
		return selectedSectors;
	}

	public void setSelectedSectors(Set<String> selectedSectors) {
		this.selectedSectors = selectedSectors;
	}

	public void selectSector(String sector) {
		selectedSectors.add(sector);
	}

	public void deselectSector(String sector) {
		selectedSectors.remove(sector);
	}

	public void clearSelectedSectors() {
		selectedSectors.clear();
	}

	public void selectAllSectors() {
		selectedSectors.clear();
		selectedSectors.addAll(getAllSectors());
	}

  /**
   * Filters the list of stocks based on the search text and selected sectors.
   * If search text is empty, it only filters by sector. If no sectors are selected, it only filters by search text.
   * If both are empty, it returns all stocks.
   */
	public List<Stock> filterStocks(String searchText) {
		List<Stock> filteredBySector = allStocks.stream()
			.filter(stock -> {
				if (selectedSectors == null || selectedSectors.isEmpty()) {
					return true;
				}
				String stockSector = stock.getSector();
				return stockSector != null && selectedSectors.contains(stockSector);
			})
			.collect(Collectors.toList());

		if (searchText == null || searchText.trim().isEmpty()) {
			return filteredBySector;
		}

		String lowerCaseSearch = searchText.toLowerCase().trim();
		return filteredBySector.stream()
			.filter(stock ->
				stock.getSymbol().toLowerCase().contains(lowerCaseSearch) ||
				stock.getCompany().toLowerCase().contains(lowerCaseSearch)
			)
			.collect(Collectors.toList());
	}

	/**
	 *  Apply all filters and update the observable filteredStocks list.
	 */
	public void applyFilters(String searchText, boolean ownedOnly, Set<String> ownedSymbols, boolean winnersOnly, boolean losersOnly) {
		List<Stock> result = filterStocksAdvanced(searchText, ownedOnly, ownedSymbols, winnersOnly, losersOnly);
		filteredStocks.setAll(result);
	}

	/**
	 * Calculates the maximum quantity of a stock that can be bought with the given cash, price, and commission rate.
	 */
	public String calculateMaxBuyQuantity(Stock selectedStock, BigDecimal cash, BigDecimal commissionRate) {
		if (selectedStock == null || cash == null || commissionRate == null) return "0";
		BigDecimal price = selectedStock.getSalesPrice();
		if (price.compareTo(BigDecimal.ZERO) <= 0 || cash.compareTo(BigDecimal.ZERO) <= 0) return "0";
		BigDecimal low = BigDecimal.ZERO;
		BigDecimal high = cash.divide(price, 0, RoundingMode.FLOOR).add(BigDecimal.ONE);
		BigDecimal best = BigDecimal.ZERO;
		while (low.compareTo(high) < 0) {
			BigDecimal mid = low.add(high).divide(new BigDecimal("2"), 0, RoundingMode.FLOOR);
			if (mid.compareTo(BigDecimal.ZERO) <= 0) {
				low = mid.add(BigDecimal.ONE);
				continue;
			}
			Share tempShare = new Share(selectedStock, mid, price);
			PurchaseCalculator calc =
					new PurchaseCalculator(tempShare, commissionRate);
			BigDecimal totalCost = calc.calculateTotal();
			if (totalCost.compareTo(cash) <= 0) {
				best = mid;
				low = mid.add(BigDecimal.ONE);
			} else {
				high = mid;
			}
		}
		return best.toPlainString();
	}

	/**
	 * Calculates the total quantity owned for a stock and returns it as plain string.
	 */
	public String calculateMaxSellQuantity(List<Share> ownedShares) {
		if (ownedShares == null || ownedShares.isEmpty()) {
			return "0";
		}

		BigDecimal total = ownedShares.stream()
			.map(Share::getQuantity)
			.reduce(BigDecimal.ZERO, BigDecimal::add);

		return total.stripTrailingZeros().toPlainString();
	}

	/**
	 * Applies all trade-screen filters in one place and returns the list (without mutating filtered list).
	 */
	public List<Stock> filterStocksAdvanced(
		String searchText,
		boolean ownedOnly,
		Set<String> ownedSymbols,
		boolean winnersOnly,
		boolean losersOnly
	) {
		List<Stock> stocks = new ArrayList<>(filterStocks(searchText));

		if (ownedOnly) {
			Set<String> safeOwnedSymbols = ownedSymbols == null ? Set.of() : ownedSymbols;
			stocks = stocks.stream()
				.filter(stock -> safeOwnedSymbols.contains(stock.getSymbol()))
				.collect(Collectors.toList());
		}

		if (winnersOnly) {
			return stocks.stream()
				.filter(stock -> stock.getLatestPriceChangeAsPercentage().signum() > 0)
				.sorted(Comparator.comparing(Stock::getLatestPriceChangeAsPercentage).reversed())
				.collect(Collectors.toList());
		}

		if (losersOnly) {
			return stocks.stream()
				.filter(stock -> stock.getLatestPriceChangeAsPercentage().signum() < 0)
				.sorted(Comparator.comparing(Stock::getLatestPriceChangeAsPercentage))
				.collect(Collectors.toList());
		}

		return stocks;
	}

	/**
	 * Update observable UI state from game state values.
	 */
	public void updateFromGameState(Player player, GameProgress progressObj, BigDecimal cashValue, int weekVal) {
		this.cash.set(cashValue == null ? BigDecimal.ZERO : cashValue);
		this.netWorth.set(player == null ? BigDecimal.ZERO : player.getNetWorth());
		this.week.set(weekVal);
		this.holdings.set(player == null ? 0 : player.getPortfolio().getShares().size());

		int currentQuarter = progressObj == null ? 0 : progressObj.getCheckpointLevel();
		this.quarterLabel.set("Q" + currentQuarter);

		BigDecimal requirement = progressObj == null ? BigDecimal.ZERO : progressObj.getCurrentTarget();
		this.requirementOverlayValue.set(requirement == null ? BigDecimal.ZERO : requirement);

		BigDecimal netWorthVal = player == null ? BigDecimal.ZERO : player.getNetWorth();
		this.netWorthOverlayValue.set(netWorthVal == null ? BigDecimal.ZERO : netWorthVal);

		double progressValue = 0;
		if (requirement != null && requirement.compareTo(BigDecimal.ZERO) > 0) {
			progressValue = netWorthVal.divide(requirement, 4, RoundingMode.HALF_UP).doubleValue();
		}
		this.progress.set(Math.max(0, Math.min(progressValue, 1.0)));

		this.deadlineLabel.set("Deadline in: " + (progressObj == null ? 0 : progressObj.getWeeksUntilDeadline()) + " weeks");
	}

	// Property accessors
	public ObjectProperty<BigDecimal> cashProperty() { return cash; }
	public ObjectProperty<BigDecimal> netWorthProperty() { return netWorth; }
	public IntegerProperty weekProperty() { return week; }
	public IntegerProperty holdingsProperty() { return holdings; }
	public StringProperty quarterLabelProperty() { return quarterLabel; }
	public ObjectProperty<BigDecimal> requirementOverlayValueProperty() { return requirementOverlayValue; }
	public ObjectProperty<BigDecimal> netWorthOverlayValueProperty() { return netWorthOverlayValue; }
	public DoubleProperty progressProperty() { return progress; }
	public StringProperty deadlineLabelProperty() { return deadlineLabel; }

	public void addTradeEventListener(Consumer<TradeEvent> listener) {
		if (listener != null) {
			tradeEventListeners.add(listener);
		}
	}

	public void removeTradeEventListener(Consumer<TradeEvent> listener) {
		tradeEventListeners.remove(listener);
	}

	public void fireTradeEvent(TradeEvent event) {
		for (Consumer<TradeEvent> listener : List.copyOf(tradeEventListeners)) {
			listener.accept(event);
		}
	}

}
