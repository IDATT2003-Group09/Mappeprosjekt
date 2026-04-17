package edu.ntnu.iir.bidata.idatt2003.group09.model.screen;

import edu.ntnu.iir.bidata.idatt2003.group09.model.Stock;
import java.util.*;
import java.util.stream.Collectors;

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

	public List<Stock> filterStocks(String searchText) {
		// First filter by sectors if any are selected
		List<Stock> filteredBySector = allStocks.stream()
			.filter(stock -> {
				if (selectedSectors == null || selectedSectors.isEmpty()) {
					return true;
				}
				String stockSector = stock.getSector();
				return stockSector != null && selectedSectors.contains(stockSector);
			})
			.collect(Collectors.toList());

		// Then filter by search text
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

}
