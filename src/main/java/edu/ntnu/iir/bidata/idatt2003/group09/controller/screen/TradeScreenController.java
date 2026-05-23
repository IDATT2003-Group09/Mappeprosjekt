package edu.ntnu.iir.bidata.idatt2003.group09.controller.screen;

import edu.ntnu.iir.bidata.idatt2003.group09.model.calculator.SaleCalculator;
import edu.ntnu.iir.bidata.idatt2003.group09.controller.GameController;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Share;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Stock;
import edu.ntnu.iir.bidata.idatt2003.group09.model.screen.TradeScreenModel;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.logging.Level;
import java.util.logging.Logger;
import edu.ntnu.iir.bidata.idatt2003.group09.model.calculator.PurchaseCalculator;

/**
 * This class manages the logic for buying and selling stocks, including validation,
 * calculation of commissions and taxes, and interaction with overlays for user confirmation.
 * It communicates with the GameController to update the player's portfolio.
 */
public class TradeScreenController {
	private final GameController controller;
	private final TradeScreenModel model;
    private static final Logger LOGGER = Logger.getLogger(TradeScreenController.class.getName());

	/**
	 * Constructs a TradeScreenController with the given GameController.
	 *
	 * @param controller the main game controller
	 */
	public TradeScreenController(GameController controller, TradeScreenModel model) {
		this.controller = controller;
		this.model = model;
		this.model.updateFromGameState(controller.getPlayer(), controller.getProgress(), controller.getMoney(), controller.getWeek());
	}

	/**
	 * Shows the transaction overlay with the provided details.
	 *
	 * @param action      the action type ("Buy" or "Sell")
	 * @param stockSymbol the stock symbol
	 * @param quantity    the quantity to buy or sell
	 * @param price       the price per stock
	 * @param commission  the commission amount
	 * @param tax         the tax amount
	 * @param total       the total transaction amount
	 * @param onConfirm   callback to run if the user confirms
	 */
	public interface ShowTransactionOverlay {
		void show(String action, String stockSymbol, BigDecimal quantity, BigDecimal price, BigDecimal commission, BigDecimal tax, BigDecimal total, Runnable onConfirm);
	}

	/**
	 * Handles buying a stock. Validates selection and quantity, calculates costs, and shows a confirmation overlay before executing the buy action.
	 *
	 * @param selectedStock the selected stock
	 * @param quantityText quantity input from the user
	 * @param showOverlay  the overlay to show transaction confirmation
	 * @param onUiRefresh callback that runs after a successful trade (e.g., refresh list/graph)
	 * @param setStatus callback for status messages
	 */
	public void handleBuy(
		Stock selectedStock,
		String quantityText,
		ShowTransactionOverlay showOverlay,
		Runnable onUiRefresh,
		Consumer<String> setStatus
	) {
		if (selectedStock == null) {
			setStatus.accept("Please select a stock first.");
			return;
		}
		try {
			BigDecimal quantity = parseQuantity(quantityText);
			BigDecimal price = selectedStock.getSalesPrice();
			BigDecimal commissionRate = controller.getExchange().getCommissionRate();
			Share tempShare = new Share(selectedStock, quantity, price);

			PurchaseCalculator calc = new PurchaseCalculator(tempShare, commissionRate);
			BigDecimal commission = calc.calculateCommission();
			BigDecimal tax = calc.calculateTax();
			BigDecimal total = calc.calculateTotal();

			showOverlay.show("Buy", selectedStock.getSymbol(), quantity, price, commission, tax, total, () -> {
				try {
					controller.getExchange().buy(selectedStock.getSymbol(), controller.getPlayer(), quantity);
					setStatus.accept("Bought " + quantity + " of " + selectedStock.getSymbol());
					onUiRefresh.run();
					model.updateFromGameState(controller.getPlayer(), controller.getProgress(), controller.getMoney(), controller.getWeek());
					model.fireTradeEvent(TradeScreenModel.TradeEvent.BUY_SUCCESS);
				} catch (IllegalArgumentException | IllegalStateException e) {
					LOGGER.log(Level.WARNING, "Buy failed for " + selectedStock.getSymbol(), e);
					setStatus.accept("Buy failed: " + e.getMessage());
				} catch (Exception e) {
					LOGGER.log(Level.SEVERE, "Unexpected error during buy", e);
					setStatus.accept("Buy failed: " + e.getMessage());
				}
			});
		} catch (Exception e) {
			if (e instanceof IllegalArgumentException) {
				LOGGER.log(Level.WARNING, "Invalid buy input", e);
				setStatus.accept("Buy failed: " + e.getMessage());
			} else {
				LOGGER.log(Level.SEVERE, "Unexpected error preparing buy", e);
				setStatus.accept("Buy failed: " + e.getMessage());
			}
		}
	}

	/**
	 * Handles selling a stock. Validates selection and quantity, calculates costs, and shows a confirmation overlay before executing the sell action.
	 *
	 * @param selectedStock the selected stock
	 * @param quantityText quantity input from the user
	 * @param showOverlay  the overlay to show transaction confirmation
	 * @param onUiRefresh callback that runs after a successful trade (e.g., refresh list/graph)
	 * @param setStatus callback for status messages
	 */
	public void handleSell(
		Stock selectedStock,
		String quantityText,
		ShowTransactionOverlay showOverlay,
		Runnable onUiRefresh,
		Consumer<String> setStatus
	) {
		if (selectedStock == null) {
			setStatus.accept("Please select a stock first.");
			return;
		}
		List<Share> shares = controller.getPortfolio().getShares(selectedStock.getSymbol());
		if (shares.isEmpty()) {
			setStatus.accept("You do not own this stock.");
			return;
		}
		try {
			BigDecimal quantity = parseQuantity(quantityText);
			BigDecimal price = selectedStock.getSalesPrice();
			BigDecimal commissionRate = controller.getExchange().getCommissionRate();
			BigDecimal avgPurchasePrice = controller.getPortfolio()
				.getAveragePurchasePrice(selectedStock.getSymbol());
			Share tempShare = new Share(selectedStock, quantity, avgPurchasePrice);
			SaleCalculator calc = new SaleCalculator(tempShare);
			BigDecimal commission = calc.calculateCommission();
			BigDecimal tax = calc.calculateTax();
			BigDecimal total = calc.calculateTotal();

			showOverlay.show("Sell", selectedStock.getSymbol(), quantity, price, commission, tax, total, () -> {
				try {
					controller.getExchange().sell(selectedStock.getSymbol(), controller.getPlayer(), quantity);
					setStatus.accept("Sold " + quantity.stripTrailingZeros().toPlainString()
						+ " of " + selectedStock.getSymbol());
					onUiRefresh.run();
					model.updateFromGameState(controller.getPlayer(), controller.getProgress(), controller.getMoney(), controller.getWeek());
					model.fireTradeEvent(TradeScreenModel.TradeEvent.SELL_SUCCESS);
				} catch (IllegalArgumentException | IllegalStateException e) {
					LOGGER.log(Level.WARNING, "Sell failed for " + selectedStock.getSymbol(), e);
					setStatus.accept("Sell failed: " + e.getMessage());
				} catch (Exception e) {
					LOGGER.log(Level.SEVERE, "Unexpected error during sell", e);
					setStatus.accept("Sell failed: " + e.getMessage());
				}
			});
		} catch (Exception e) {
			if (e instanceof IllegalArgumentException) {
				LOGGER.log(Level.WARNING, "Invalid sell input", e);
				setStatus.accept("Sell failed: " + e.getMessage());
			} else {
				LOGGER.log(Level.SEVERE, "Unexpected error preparing sell", e);
				setStatus.accept("Sell failed: " + e.getMessage());
			}
		}
	}

	/**
	 * Parses the quantity from user input text.
	 *
	 * @param quantityText the user input text
	 * @return the parsed quantity as BigDecimal
	 * @throws IllegalArgumentException if the input is invalid (non-numeric, negative, or zero)
	 */
	private BigDecimal parseQuantity(String quantityText) {
		try {
			BigDecimal quantity = new BigDecimal(quantityText.trim());
			if (quantity.compareTo(BigDecimal.ZERO) <= 0) {
				throw new IllegalArgumentException("Quantity must be > 0");
			}
			return quantity;
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Invalid number");
		}
		
	}
	/**
	 * Refreshes the model from the current controller/game state.
	 * Use when external game state changes (e.g., week advance) occur.
	 */
	public void refreshModel() {
		model.updateFromGameState(controller.getPlayer(), controller.getProgress(), controller.getMoney(), controller.getWeek());
	}

	/**
	 * Advances one week and synchronizes model state if the game continues.
	 *
	 * @return the week advancement result from the game controller
	 */
	public GameController.WeekAdvanceResult advanceWeek() {
		GameController.WeekAdvanceResult result = controller.nextWeek();
		if (!result.gameOver()) {
			refreshModel();
		}
		return result;
	}

	/**
	 * Advances one week and invokes callbacks for game-over, normal update and quarter progression.
	 */
	public void handleAdvanceWeek(
		Runnable onGameOver,
		Runnable onWeekAdvanced,
		Consumer<GameController.WeekAdvanceResult> onQuarterAdvanced
	) {
		GameController.WeekAdvanceResult result = advanceWeek();
		if (result.gameOver()) {
			onGameOver.run();
			return;
		}

		onWeekAdvanced.run();
		if (result.quarterAdvanced()) {
			onQuarterAdvanced.accept(result);
		}
	}

	/**
	 * Calculates max buy quantity using current game state.
	 */
	public String calculateMaxBuyQuantity(Stock selectedStock) {
		return model.calculateMaxBuyQuantity(
			selectedStock,
			controller.getMoney(),
			controller.getExchange().getCommissionRate()
		);
	}

	/**
	 * Calculates max sell quantity for the selected symbol from current portfolio.
	 */
	public String calculateMaxSellQuantity(String symbol) {
		List<Share> shares = controller.getPortfolio().getShares(symbol);
		return model.calculateMaxSellQuantity(shares);
	}

	/**
	 * Returns symbols currently owned by the player.
	 */
	public Set<String> getOwnedSymbols() {
		return controller.getPortfolio().getShares().stream()
			.map(share -> share.getStock().getSymbol())
			.collect(Collectors.toSet());
	}

	/**
	 * Applies trade-screen filters using current game state.
	 */
	public void applyFilters(TradeFilterRequest request) {
		String searchText = request.searchText() == null ? "" : request.searchText();
		model.applyFilters(
			searchText,
			request.allSectorsSelected(),
			request.ownedOnly(),
			getOwnedSymbols(),
			request.winnersOnly(),
			request.losersOnly()
		);
	}

	/**
	 * Returns true if all sectors are currently selected.
	 */
	public boolean areAllSectorsSelected() {
		Set<String> allSectors = model.getAllSectors();
		return !allSectors.isEmpty() && model.getSelectedSectors().containsAll(allSectors);
	}

	/**
	 * Select a sector in the model's selected sectors set.
	 */
	public void selectSector(String sector) {
		model.selectSector(sector);
	}

	/**
	 * Deselect a sector in the model's selected sectors set.
	 */
	public void deselectSector(String sector) {
		model.deselectSector(sector);
	}

	/**
	 * Select all available sectors in the model.
	 */
	public void selectAllSectors() {
		model.selectAllSectors();
	}

	/**
	 * Clear all selected sectors in the model.
	 */
	public void clearSelectedSectors() {
		model.clearSelectedSectors();
	}

	/**
	 * Returns the currently selected sectors.
	 */
	public Set<String> getSelectedSectors() {
		return model.getSelectedSectors();
	}

}
