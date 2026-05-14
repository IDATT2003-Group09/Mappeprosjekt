
package edu.ntnu.iir.bidata.idatt2003.group09.model.screen;

import edu.ntnu.iir.bidata.idatt2003.group09.model.Stock;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Share;
import edu.ntnu.iir.bidata.idatt2003.group09.model.calculator.PurchaseCalculator;
import java.util.*;
import java.util.stream.Collectors;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Model for the Trade Screen (MVC).
 * Holds trade-related data and business logic for the trade screen.
 */
public class TradeScreenModel {
	private List<Stock> allStocks;
	private Set<String> selectedSectors;

	public TradeScreenModel(List<Stock> stocks) {
		this.allStocks = new ArrayList<>(stocks);
		this.selectedSectors = new HashSet<>();
	}

	public void setStocks(List<Stock> stocks) {
		this.allStocks = new ArrayList<>(stocks);
	}

	public List<Stock> getAllStocks() {
		return allStocks;
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
	 * Applies all trade-screen filters in one place.
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

}
