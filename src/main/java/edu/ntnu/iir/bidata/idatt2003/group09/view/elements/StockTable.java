package edu.ntnu.iir.bidata.idatt2003.group09.view.elements;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

import edu.ntnu.iir.bidata.idatt2003.group09.model.Player;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Share;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Stock;
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
        symbolColumn.getStyleClass().add("stock-symbol-column");
        symbolColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String symbol, boolean empty) {
                super.updateItem(symbol, empty);

                if (!getStyleClass().contains("stock-symbol-cell")) {
                    getStyleClass().add("stock-symbol-cell");
                }

                setText(empty || symbol == null ? null : symbol);
            }
        });

		TableColumn<Stock, String> companyColumn = new TableColumn<>("Company");
		companyColumn.setCellValueFactory(data ->
				new SimpleStringProperty(data.getValue().getCompany()));
        companyColumn.getStyleClass().add("stock-company-column");
        companyColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String company, boolean empty) {
                super.updateItem(company, empty);

                if (!getStyleClass().contains("stock-company-cell")) {
                    getStyleClass().add("stock-company-cell");
                }

                setText(empty || company == null ? null : company);
            }
        });

        TableColumn<Stock, BigDecimal> priceColumn = new TableColumn<>("Price");
        priceColumn.getStyleClass().add("stock-price-column");

        priceColumn.setCellValueFactory(data ->
                new SimpleObjectProperty<>(data.getValue().getSalesPrice()));

        priceColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(BigDecimal price, boolean empty) {
                super.updateItem(price, empty);

                if (!getStyleClass().contains("stock-price-cell")) {
                    getStyleClass().add("stock-price-cell");
                }

                getStyleClass().removeAll("stock-change-up", "stock-change-down", "stock-change-neutral");

                if (empty || price == null) {
                    setText(null);
                    return;
                }

                Stock stock = getTableView().getItems().get(getIndex());
                BigDecimal change = stock.getLatestPriceChange();
                BigDecimal previousPrice = price.subtract(change);

                BigDecimal percentChange = BigDecimal.ZERO;
                if (previousPrice.compareTo(BigDecimal.ZERO) > 0) {
                    percentChange = change
                            .divide(previousPrice, 4, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(100));
                }

                String sign = change.compareTo(BigDecimal.ZERO) > 0 ? "+" : "";
                String percentSign = percentChange.compareTo(BigDecimal.ZERO) > 0 ? "+" : "";

                String text = currencyFormat.format(price) +
                        String.format("\n%s%s (%s%s%%)",
                                sign,
                                currencyFormat.format(change),
                                percentSign,
                                percentChange.setScale(2, RoundingMode.HALF_UP).toPlainString());

                setText(text);

                if (change.compareTo(BigDecimal.ZERO) > 0) {
                    getStyleClass().add("stock-change-up");
                } else if (change.compareTo(BigDecimal.ZERO) < 0) {
                    getStyleClass().add("stock-change-down");
                } else {
                    getStyleClass().add("stock-change-neutral");
                }
            }
        });

		TableColumn<Stock, String> ownedColumn = new TableColumn<>("Owned");
        ownedColumn.getStyleClass().add("stock-owned-column");
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
        ownedColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String owned, boolean empty) {
                super.updateItem(owned, empty);

                if (!getStyleClass().contains("stock-owned-cell")) {
                    getStyleClass().add("stock-owned-cell");
                }

                setText(empty || owned == null ? null : owned);
            }
        });

		symbolColumn.setPrefWidth(120);
		companyColumn.setPrefWidth(350);
		priceColumn.setPrefWidth(250);
		ownedColumn.setPrefWidth(120);

		TableView<Stock> stockTable = new TableView<>();
        stockTable.getStyleClass().add("stock-table");
		stockTable.getColumns().add(symbolColumn);
		stockTable.getColumns().add(companyColumn);
		stockTable.getColumns().add(priceColumn);
		stockTable.getColumns().add(ownedColumn);
		stockTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

		return stockTable;
	}
}
