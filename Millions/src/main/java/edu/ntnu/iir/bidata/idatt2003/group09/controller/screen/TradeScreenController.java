package edu.ntnu.iir.bidata.idatt2003.group09.controller.screen;

import edu.ntnu.iir.bidata.idatt2003.group09.model.calculator.SaleCalculator;
import edu.ntnu.iir.bidata.idatt2003.group09.controller.GameController;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Share;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Stock;
import edu.ntnu.iir.bidata.idatt2003.group09.model.screen.TradeScreenModel;

import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import java.math.BigDecimal;
import java.util.List;
import edu.ntnu.iir.bidata.idatt2003.group09.model.calculator.PurchaseCalculator;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Share;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Stock;

/**
 * This class manages the logic for buying and selling stocks, including validation,
 * calculation of commissions and taxes, and interaction with overlays for user confirmation.
 * It communicates with the GameController to update the player's portfolio.
 */
public class TradeScreenController {
	private final GameController controller;
	private final TradeScreenModel model;

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
	 * @param stockList    the ListView of available stocks
	 * @param quantityField the TextField for user input quantity
	 * @param statusLabel  the Label for status messages
	 * @param showOverlay  the overlay to show transaction confirmation
	* @param onSuccess    callback to run on successful buy
	 */
	public void handleBuy(ListView<Stock> stockList, TextField quantityField, Label statusLabel, ShowTransactionOverlay showOverlay) {
		Stock selectedStock = stockList.getSelectionModel().getSelectedItem();
		if (selectedStock == null) {
			statusLabel.setText("Please select a stock first.");
			return;
		}
		try {
			BigDecimal quantity = parseQuantity(quantityField);
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
					statusLabel.setText("Bought " + quantity + " of " + selectedStock.getSymbol());
					stockList.refresh();
					model.updateFromGameState(controller.getPlayer(), controller.getProgress(), controller.getMoney(), controller.getWeek());
					model.fireTradeEvent(TradeScreenModel.TradeEvent.BUY_SUCCESS);
				} catch (Exception e) {
					statusLabel.setText("Buy failed: " + e.getMessage());
				}
			});
		} catch (Exception e) {
			statusLabel.setText("Buy failed: " + e.getMessage());
		}
	}

	/**
	 * Handles selling a stock. Validates selection and quantity, calculates costs, and shows a confirmation overlay before executing the sell action.
	 *
	 * @param stockList    the ListView of available stocks
	 * @param quantityField the TextField for user input quantity
	 * @param statusLabel  the Label for status messages
	 * @param showOverlay  the overlay to show transaction confirmation
	* @param onSuccess    callback to run on successful sell
	 */
	public void handleSell(ListView<Stock> stockList, TextField quantityField, Label statusLabel, ShowTransactionOverlay showOverlay) {
		Stock selectedStock = stockList.getSelectionModel().getSelectedItem();
		if (selectedStock == null) {
			statusLabel.setText("Please select a stock first.");
			return;
		}
		List<Share> shares = controller.getPortfolio().getShares(selectedStock.getSymbol());
		if (shares.isEmpty()) {
			statusLabel.setText("You do not own this stock.");
			return;
		}
		try {
			BigDecimal quantity = parseQuantity(quantityField);
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
					statusLabel.setText("Sold " + quantity.stripTrailingZeros().toPlainString()
						+ " of " + selectedStock.getSymbol());
					stockList.refresh();
					model.updateFromGameState(controller.getPlayer(), controller.getProgress(), controller.getMoney(), controller.getWeek());
					model.fireTradeEvent(TradeScreenModel.TradeEvent.SELL_SUCCESS);
				} catch (Exception e) {
					statusLabel.setText("Sell failed: " + e.getMessage());
				}
			});
		} catch (Exception e) {
			statusLabel.setText("Sell failed: " + e.getMessage());
		}
	}

	/**
	 * Parses the quantity from the text field.
	 *
	 * @param quantityField the TextField containing the quantity input
	 * @return the parsed quantity as BigDecimal
	 * @throws IllegalArgumentException if the input is invalid (non-numeric, negative, or zero)
	 */
	private BigDecimal parseQuantity(TextField quantityField) {
		try {
			BigDecimal quantity = new BigDecimal(quantityField.getText().trim());
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

}
