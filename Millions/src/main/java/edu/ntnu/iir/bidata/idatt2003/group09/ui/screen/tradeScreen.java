package edu.ntnu.iir.bidata.idatt2003.group09.ui.screen;

import edu.ntnu.iir.bidata.idatt2003.group09.base.Exchange;
import edu.ntnu.iir.bidata.idatt2003.group09.base.Player;
import edu.ntnu.iir.bidata.idatt2003.group09.base.Share;
import edu.ntnu.iir.bidata.idatt2003.group09.base.Stock;
import edu.ntnu.iir.bidata.idatt2003.group09.ui.StockTable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class tradeScreen extends BorderPane {

	private final Exchange exchange;
	private final Player player;
	private final TableView<Stock> stockTable;
	private final Label statusLabel;
	private final Label cashLabel;
	private final Label holdingsLabel;
	private final TextField quantityField;
	private final NumberFormat currencyFormat;

  /**
   * Constructor for the trade screen. requires a player an exchange and a list of stock.
   * @param exchange
   * @param player
   * @param stocks
   */
	public tradeScreen(Exchange exchange, Player player, List<Stock> stocks) {
		this.exchange = exchange;
		this.player = player;
		this.currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);

		if (this.exchange.getWeek() == 0) {
			this.exchange.advance();
		}

		stockTable = new StockTable().createStockTable(this.player);
		stockTable.setItems(FXCollections.observableArrayList(stocks));

		quantityField = new TextField("1");
		quantityField.setPrefWidth(100);

		statusLabel = new Label("Select a stock, then buy or sell.");
		cashLabel = new Label();
		holdingsLabel = new Label();

		buildLayout();
		refreshInfo();
	}

  /**
   * build the layout. Makes labels and buttons, and create the stocktable.
   */
	private void buildLayout() {
		Label titleLabel = new Label("Trade Stocks");
		titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

		Label quantityLabel = new Label("Quantity:");

		Button buyButton = new Button("Buy selected stock");
		Button sellButton = new Button("Sell selected stock");

		buyButton.setOnAction(event -> buySelectedStock());
		sellButton.setOnAction(event -> sellSelectedStock());

		HBox controls = new HBox(10, quantityLabel, quantityField, buyButton, sellButton);
		controls.setPadding(new Insets(0, 0, 10, 0));

		VBox headerBox = new VBox(8, titleLabel, cashLabel, holdingsLabel, controls, statusLabel);
		headerBox.setPadding(new Insets(10));

		setTop(headerBox);
		setCenter(stockTable);
		setPadding(new Insets(10));
	}

  /**
   * method to buy stock intuitivly connected to the ui.
   */
	private void buySelectedStock() {
		Stock selectedStock = stockTable.getSelectionModel().getSelectedItem();
		if (selectedStock == null) {
			statusLabel.setText("Please select a stock first.");
			return;
		}

		BigDecimal quantity;
		try {
			quantity = parseQuantity();
		} catch (IllegalArgumentException exception) {
			statusLabel.setText(exception.getMessage());
			return;
		}

		try {
			exchange.buy(selectedStock.getSymbol(), player, quantity);
			statusLabel.setText("Bought " + quantity.toPlainString() + " of " + selectedStock.getSymbol());
			stockTable.refresh();
			refreshInfo();
		} catch (Exception exception) {
			statusLabel.setText("Buy failed: " + exception.getMessage());
		}
	}

  /**
   * method to sell stock intuitivly connected to the ui.
   */
	private void sellSelectedStock() {
		Stock selectedStock = stockTable.getSelectionModel().getSelectedItem();
		if (selectedStock == null) {
			statusLabel.setText("Please select a stock first.");
			return;
		}

		List<Share> ownedShares = player.getPortfolio().getShares(selectedStock.getSymbol());
		if (ownedShares.isEmpty()) {
			statusLabel.setText("You do not own any " + selectedStock.getSymbol() + " shares to sell.");
			return;
		}

		Share shareToSell = ownedShares.getFirst();
		try {
			exchange.sell(shareToSell, player);
			statusLabel.setText("Sold " + shareToSell.getQuantity().toPlainString()
					+ " of " + selectedStock.getSymbol());
			stockTable.refresh();
			refreshInfo();
		} catch (Exception exception) {
			statusLabel.setText("Sell failed: " + exception.getMessage());
		}
	}

  /**
   * parses the quantity from the text field, and validates it. throws an exception if invalid.
   * @return the quantity as a BigDecimal
   */
	private BigDecimal parseQuantity() {
		try {
			BigDecimal quantity = new BigDecimal(quantityField.getText().trim());
			if (quantity.compareTo(BigDecimal.ZERO) <= 0) {
				throw new IllegalArgumentException("Quantity must be greater than 0.");
			}
			return quantity;
		} catch (NumberFormatException exception) {
			throw new IllegalArgumentException("Quantity must be a valid number.");
		}
	}

  /**
   * refreshes the cash and holdings labels to show the current state of the player.
   */
	private void refreshInfo() {
		cashLabel.setText("Cash: " + currencyFormat.format(player.getMoney()));
		holdingsLabel.setText("Open positions: " + player.getPortfolio().getShares().size());
	}
}
