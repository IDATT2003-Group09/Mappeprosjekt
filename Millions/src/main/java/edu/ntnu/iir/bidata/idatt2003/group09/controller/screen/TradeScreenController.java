package edu.ntnu.iir.bidata.idatt2003.group09.controller.screen;

import edu.ntnu.iir.bidata.idatt2003.group09.controller.GameController;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Share;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Stock;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import java.math.BigDecimal;
import java.util.List;

public class TradeScreenController {
	private final GameController controller;

	public TradeScreenController(GameController controller) {
		this.controller = controller;
	}

	public void handleBuy(ListView<Stock> stockList, TextField quantityField, Label statusLabel, Runnable onSuccess, Runnable refreshInfo) {
		Stock selectedStock = stockList.getSelectionModel().getSelectedItem();
		if (selectedStock == null) {
			statusLabel.setText("Please select a stock first.");
			return;
		}
		try {
			BigDecimal quantity = parseQuantity(quantityField);
			// Calculate breakdown before buying
			// price can be used for overlays if needed
			// commissionRate and tempShare can be used for overlays if needed
			// PurchaseCalculator calc = new PurchaseCalculator(tempShare, commissionRate); // For overlays if needed
			// Calculation for commission, tax, and total can be used for overlays if needed

			// Perform buy
			controller.getExchange().buy(selectedStock.getSymbol(), controller.getPlayer(), quantity);
			statusLabel.setText("Bought " + quantity + " of " + selectedStock.getSymbol());
			onSuccess.run();
			stockList.refresh();
			refreshInfo.run();
		} catch (Exception e) {
			statusLabel.setText("Buy failed: " + e.getMessage());
		}
	}

	public void handleSell(ListView<Stock> stockList, TextField quantityField, Label statusLabel, Runnable onSuccess, Runnable refreshInfo) {
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
			// Calculate breakdown before selling
			// price can be used for overlays if needed
			// commissionRate and tempShare can be used for overlays if needed
			// PurchaseCalculator calc = new PurchaseCalculator(tempShare, commissionRate); // For overlays if needed
			// Calculation for commission, tax, and total can be used for overlays if needed

			// Perform sell
			controller.getExchange().sell(selectedStock.getSymbol(), controller.getPlayer(), quantity);
			statusLabel.setText("Sold " + quantity.stripTrailingZeros().toPlainString()
				+ " of " + selectedStock.getSymbol());
			onSuccess.run();
			stockList.refresh();
			refreshInfo.run();
		} catch (Exception e) {
			statusLabel.setText("Sell failed: " + e.getMessage());
		}
	}

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
}
