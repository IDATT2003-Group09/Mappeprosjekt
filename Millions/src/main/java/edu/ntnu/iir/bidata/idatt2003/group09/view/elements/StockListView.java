package edu.ntnu.iir.bidata.idatt2003.group09.view.elements;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

import edu.ntnu.iir.bidata.idatt2003.group09.model.Player;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Share;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Stock;

public class StockListView {

    public ListView<Stock> createStockList(Player player) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);

        ListView<Stock> stockList = new ListView<>();
        stockList.getStyleClass().add("trade-stock-list");

        stockList.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Stock stock, boolean empty) {
                super.updateItem(stock, empty);

                getStyleClass().remove("selected-cell");

                if (empty || stock == null) {
                    setText(null);
                    setGraphic(null);
                    return;
                }

                Label symbolLabel = new Label(stock.getSymbol());
                symbolLabel.getStyleClass().add("stock-list-symbol");

                Label sectorLabel = new Label(stock.getSector());
                sectorLabel.getStyleClass().add("stock-list-sector");

                HBox symbolLine = new HBox(8, symbolLabel, sectorLabel);
                symbolLine.setAlignment(Pos.CENTER_LEFT);

                if (player != null) {
                    BigDecimal totalOwned = BigDecimal.ZERO;
                    for (Share share : player.getPortfolio().getShares(stock.getSymbol())) {
                        totalOwned = totalOwned.add(share.getQuantity());
                    }

                    if (totalOwned.compareTo(BigDecimal.ZERO) > 0) {
                        Label ownedLabel = new Label(totalOwned.stripTrailingZeros().toPlainString() + " SHARES");
                        ownedLabel.getStyleClass().add("stock-list-owned");
                        symbolLine.getChildren().add(ownedLabel);
                    }
                }

                Label companyLabel = new Label(stock.getCompany());
                companyLabel.getStyleClass().add("stock-list-company");

                VBox leftBox = new VBox(6, symbolLine, companyLabel);
                leftBox.setAlignment(Pos.CENTER_LEFT);

                BigDecimal price = stock.getSalesPrice();
                BigDecimal change = stock.getLatestPriceChange();
                BigDecimal previous = price.subtract(change);

                BigDecimal percentChange = BigDecimal.ZERO;
                if (previous.compareTo(BigDecimal.ZERO) > 0) {
                    percentChange = change
                            .divide(previous, 4, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(100));
                }

                String sign = change.compareTo(BigDecimal.ZERO) > 0 ? "+" : "";
                String percentSign = percentChange.compareTo(BigDecimal.ZERO) > 0 ? "+" : "";

                Label priceLabel = new Label(currencyFormat.format(price));
                priceLabel.getStyleClass().add("stock-list-price");

                Label changeLabel = new Label(sign + currencyFormat.format(change) + " ("
                        + percentSign + percentChange.setScale(2, RoundingMode.HALF_UP).toPlainString() + "%)");
                changeLabel.getStyleClass().add("stock-list-change");

                if (change.compareTo(BigDecimal.ZERO) > 0) {
                    changeLabel.getStyleClass().add("stock-list-change-up");
                } else if (change.compareTo(BigDecimal.ZERO) < 0) {
                    changeLabel.getStyleClass().add("stock-list-change-down");
                } else {
                    changeLabel.getStyleClass().add("stock-list-change-neutral");
                }

                VBox rightBox = new VBox(8, priceLabel, changeLabel);
                rightBox.setAlignment(Pos.CENTER_RIGHT);

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                HBox row = new HBox(12, leftBox, spacer, rightBox);
                row.setAlignment(Pos.CENTER_LEFT);
                row.getStyleClass().add("stock-list-row");

                setText(null);
                setGraphic(row);
            }
        });

        return stockList;
    }
}
