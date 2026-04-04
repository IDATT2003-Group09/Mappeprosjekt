package edu.ntnu.iir.bidata.idatt2003.group09.ui;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

import edu.ntnu.iir.bidata.idatt2003.group09.base.Player;
import edu.ntnu.iir.bidata.idatt2003.group09.base.Share;
import edu.ntnu.iir.bidata.idatt2003.group09.base.Stock;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class StockTable {

  /**
   * empty constructor
   */
  public StockTable() {

  }

  /**
   * creates the stock table and returns it as a tableview that can be displayed
   * @return a TableView of Stock with columns for symbol, company, price and owned quantity
   */
  public TableView<Stock> createStockTable() {
		return createStockTable(null);
	}

  /**
   * create the tableview
   * @param player
   * @return returns a visual
   */
	public TableView<Stock> createStockTable(Player player) {
		NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);

		TableColumn<Stock, String> symbolColumn = new TableColumn<>("Symbol");
		symbolColumn.setCellValueFactory(data ->
				new SimpleStringProperty(data.getValue().getSymbol()));

		TableColumn<Stock, String> companyColumn = new TableColumn<>("Company");
		companyColumn.setCellValueFactory(data ->
				new SimpleStringProperty(data.getValue().getCompany()));

        TableColumn<Stock, BigDecimal> priceColumn = new TableColumn<>("Price");

        priceColumn.setCellValueFactory(data ->
                new SimpleObjectProperty<>(data.getValue().getSalesPrice()));

        priceColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(BigDecimal price, boolean empty) {
                super.updateItem(price, empty);

                if (empty || price == null) {
                    setText(null);
                    setStyle("");
                    return;
                }

                Stock stock = getTableView().getItems().get(getIndex());
                BigDecimal change = stock.getLatestPriceChange();

                String sign = change.compareTo(BigDecimal.ZERO) > 0 ? "+" : "";

                String text = currencyFormat.format(price) +
                        String.format(" (%s%.2f)", sign, change);

                setText(text);

                // farge
                if (change.compareTo(BigDecimal.ZERO) > 0) {
                    setStyle("-fx-text-fill: green;");
                } else if (change.compareTo(BigDecimal.ZERO) < 0) {
                    setStyle("-fx-text-fill: red;");
                } else {
                    setStyle("");
                }
            }
        });

		TableColumn<Stock, String> ownedColumn = new TableColumn<>("Owned");
		ownedColumn.setCellValueFactory(data -> {
			if (player == null) {
				return new SimpleStringProperty("-");
			}

			BigDecimal totalOwned = BigDecimal.ZERO;
			for (Share share : player.getPortfolio().getShares(data.getValue().getSymbol())) {
				totalOwned = totalOwned.add(share.getQuantity());
			}

			String display = totalOwned.stripTrailingZeros().toPlainString();
			return new SimpleStringProperty(display);
		});

		symbolColumn.setPrefWidth(120);
		companyColumn.setPrefWidth(350);
		priceColumn.setPrefWidth(250);
		ownedColumn.setPrefWidth(120);

		TableView<Stock> stockTable = new TableView<>();
		stockTable.getColumns().add(symbolColumn);
		stockTable.getColumns().add(companyColumn);
		stockTable.getColumns().add(priceColumn);
		stockTable.getColumns().add(ownedColumn);
		stockTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

		return stockTable;
	}
}
