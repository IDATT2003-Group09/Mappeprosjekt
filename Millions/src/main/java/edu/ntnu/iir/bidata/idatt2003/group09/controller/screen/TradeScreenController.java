package edu.ntnu.iir.bidata.idatt2003.group09.controller.screen;

import edu.ntnu.iir.bidata.idatt2003.group09.controller.GameController;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Share;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Stock;

import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import java.math.BigDecimal;
import java.util.List;
import edu.ntnu.iir.bidata.idatt2003.group09.model.calculator.PurchaseCalculator;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Share;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Stock;

public class TradeScreenController {
	private final GameController controller;

	public TradeScreenController(GameController controller) {
		this.controller = controller;
	}


	public interface ShowTransactionOverlay {
		void show(String action, String stockSymbol, BigDecimal quantity, BigDecimal price, BigDecimal commission, BigDecimal tax, BigDecimal total, Runnable onConfirm);
	}

	public void handleBuy(ListView<Stock> stockList, TextField quantityField, Label statusLabel, ShowTransactionOverlay showOverlay, Runnable onSuccess, Runnable refreshInfo) {
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
					onSuccess.run();
					stockList.refresh();
					refreshInfo.run();
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
   */
	public void handleSell(ListView<Stock> stockList, TextField quantityField, Label statusLabel, ShowTransactionOverlay showOverlay, Runnable onSuccess, Runnable refreshInfo) {
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
			Share tempShare = new Share(selectedStock, quantity, price);
			PurchaseCalculator calc = new PurchaseCalculator(tempShare, commissionRate);
			BigDecimal commission = calc.calculateCommission();
			BigDecimal tax = calc.calculateTax();
			BigDecimal total = calc.calculateTotal();

			showOverlay.show("Sell", selectedStock.getSymbol(), quantity, price, commission, tax, total, () -> {
				try {
					controller.getExchange().sell(selectedStock.getSymbol(), controller.getPlayer(), quantity);
					statusLabel.setText("Sold " + quantity.stripTrailingZeros().toPlainString()
						+ " of " + selectedStock.getSymbol());
					onSuccess.run();
					stockList.refresh();
					refreshInfo.run();
				} catch (Exception e) {
					statusLabel.setText("Sell failed: " + e.getMessage());
				}
			});
		} catch (Exception e) {
			statusLabel.setText("Sell failed: " + e.getMessage());
		}
	}

  /*
   * Parses the quantity from the text field.
    * Throws IllegalArgumentException if the input is invalid (non-numeric, negative, or zero).
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
}
