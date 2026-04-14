package edu.ntnu.iir.bidata.idatt2003.group09.ui.screen;

import edu.ntnu.iir.bidata.idatt2003.group09.controller.GameController;
import edu.ntnu.iir.bidata.idatt2003.group09.controller.PortfolioRow;
import edu.ntnu.iir.bidata.idatt2003.group09.ui.StockGraph;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class PortfolioScreen extends BorderPane {

    private final GameController controller;
    private final TableView<PortfolioRow> table;

    private final Label totalValueLabel;
    private final Label changeLabel;
    private final Label cashLabel;


        private final NumberFormat currencyFormat =
            NumberFormat.getCurrencyInstance(Locale.US);

        // Graph for portfolio value over time
        private final javafx.scene.chart.LineChart<Number, Number> portfolioChart;


    public PortfolioScreen(GameController controller) {
        this.controller = controller;

        getStylesheets().add(getClass().getResource("/styling/portfolio.css").toExternalForm());
        getStylesheets().add(getClass().getResource("/styling/tradescreen.css").toExternalForm());
        getStyleClass().add("portfolio-screen");

        this.table = new TableView<>();
        this.totalValueLabel = new Label();
        this.changeLabel = new Label();
        this.cashLabel = new Label();

        // Setup portfolio value chart
        javafx.scene.chart.NumberAxis xAxis = new javafx.scene.chart.NumberAxis();
        xAxis.setLabel(""); // Hide x-axis label
        xAxis.setTickLabelsVisible(false); // Hide week numbers
        xAxis.setTickMarkVisible(false);
        javafx.scene.chart.NumberAxis yAxis = new javafx.scene.chart.NumberAxis();
        yAxis.setLabel("Portfolio Value");
        this.portfolioChart = new javafx.scene.chart.LineChart<>(xAxis, yAxis);
        portfolioChart.setAnimated(true);
        portfolioChart.setLegendVisible(false);
        portfolioChart.setMinHeight(250);
        portfolioChart.setMaxHeight(250);
        portfolioChart.getStyleClass().add("trade-graph");
        // Remove grid lines
        portfolioChart.setHorizontalGridLinesVisible(false);
        portfolioChart.setVerticalGridLinesVisible(false);

        buildTable();
        buildLayout();
        refresh();
    }

    private void buildLayout() {
        totalValueLabel.getStyleClass().add("portfolio-total-value");
        changeLabel.getStyleClass().add("portfolio-change");
        cashLabel.getStyleClass().add("portfolio-cash");

        VBox topBox = new VBox(5, totalValueLabel, changeLabel, cashLabel);
        topBox.getStyleClass().add("portfolio-top-box");
        topBox.setPadding(new Insets(10));

        table.getStyleClass().add("portfolio-table");

        VBox centerBox = new VBox(10, portfolioChart, table);
        centerBox.setPadding(new Insets(0, 0, 0, 0));

        setCenter(centerBox);
        setPadding(new Insets(10));
        setTop(topBox);
    }

    private void buildTable() {

        TableColumn<PortfolioRow, String> symbolCol = new TableColumn<>("Symbol");
        symbolCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getSymbol()));

        TableColumn<PortfolioRow, String> quantityCol = new TableColumn<>("Quantity");
        quantityCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getQuantity().stripTrailingZeros().toPlainString()));

        TableColumn<PortfolioRow, String> valueCol = new TableColumn<>("Value");
        valueCol.setCellValueFactory(data ->
                new SimpleStringProperty(format(data.getValue().getValue())));

        TableColumn<PortfolioRow, String> gainCol = new TableColumn<>("Total Gain/Loss");
        gainCol.setCellValueFactory(data ->
                new SimpleStringProperty(formatWithSign(data.getValue().getGain())));
        gainCol.setCellFactory(col -> coloredCell());

        TableColumn<PortfolioRow, String> percentCol = new TableColumn<>("ROI in %");
        percentCol.setCellValueFactory(data ->
                new SimpleStringProperty(formatPercent(data.getValue().getPercentGain())));
        percentCol.setCellFactory(col -> coloredCell());

        TableColumn<PortfolioRow, String> priceChangeCol = new TableColumn<>("Price Change");
        priceChangeCol.setCellValueFactory(data ->
                new SimpleStringProperty(formatWithSign(data.getValue().getWeeklyPriceChange())));
        priceChangeCol.setCellFactory(col -> coloredCell());

        TableColumn<PortfolioRow, String> pricePercentCol = new TableColumn<>("Price Percentage Change");
        pricePercentCol.setCellValueFactory(data ->
                new SimpleStringProperty(formatPercent(data.getValue().getWeeklyPercentChange())));
        pricePercentCol.setCellFactory(col -> coloredCell());

        table.getColumns().addAll(
                symbolCol,
                quantityCol,
                valueCol,
                gainCol,
                percentCol,
                priceChangeCol,
                pricePercentCol
        );

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    public void refresh() {
        List<PortfolioRow> rows = controller.getPortfolio().getShares().stream()
                .map(PortfolioRow::new)
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .toList();

        table.setItems(FXCollections.observableArrayList(rows));

        BigDecimal current = controller.getNetWorth();
        BigDecimal previous = controller.getLastWeekNetWorth();

        BigDecimal change = current.subtract(previous);

        BigDecimal percentChange = BigDecimal.ZERO;
        if (previous.compareTo(BigDecimal.ZERO) != 0) {
            percentChange = change
                    .divide(previous, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        }

        if (change.signum() >= 0) {
            changeLabel.getStyleClass().remove("negative");
            if (!changeLabel.getStyleClass().contains("positive")) {
                changeLabel.getStyleClass().add("positive");
            }
        } else {
            changeLabel.getStyleClass().remove("positive");
            if (!changeLabel.getStyleClass().contains("negative")) {
                changeLabel.getStyleClass().add("negative");
            }
        }

        totalValueLabel.setText("Total: " + format(current));
        changeLabel.setText("Weekly portfolio change: " + formatWithSign(change)
                + " (" + formatPercent(percentChange) + ")");
        cashLabel.setText("Cash: " + currencyFormat.format(controller.getMoney()));

        // Update portfolio value chart
        List<java.math.BigDecimal> values = controller.getPortfolio().getValues();
        javafx.scene.chart.XYChart.Series<Number, Number> series = new javafx.scene.chart.XYChart.Series<>();
        for (int i = 0; i < values.size(); i++) {
            series.getData().add(new javafx.scene.chart.XYChart.Data<>(i + 1, values.get(i)));
        }
        portfolioChart.getData().clear();
        portfolioChart.getData().add(series);
        portfolioChart.setTitle(""); // Remove the header/title
    }


    private String format(BigDecimal value) {
        return currencyFormat.format(value);
    }

    private String formatWithSign(BigDecimal value) {
        String sign = value.compareTo(BigDecimal.ZERO) > 0 ? "+" : "";
        return sign + currencyFormat.format(value);
    }

    private String formatPercent(BigDecimal value) {
        String sign = value.compareTo(BigDecimal.ZERO) > 0 ? "+" : "";
        return sign + value.setScale(2, RoundingMode.HALF_UP)
                .stripTrailingZeros()
                .toPlainString() + "%";
    }

    private TableCell<PortfolioRow, String> coloredCell() {
        return new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (!getStyleClass().contains("portfolio-colored-cell")) {
                    getStyleClass().add("portfolio-colored-cell");
                }

                if (empty || item == null) {
                    setText(null);
                    getStyleClass().removeAll("positive", "negative");
                    return;
                }

                setText(item);

                if (item.startsWith("-")) {
                    getStyleClass().remove("positive");
                    if (!getStyleClass().contains("negative")) {
                        getStyleClass().add("negative");
                    }
                } else {
                    getStyleClass().remove("negative");
                    if (!getStyleClass().contains("positive")) {
                        getStyleClass().add("positive");
                    }
                }
            }
        };
    }

    public StockGraph createStockGraph() {
        return new StockGraph(controller.getPortfolio().getShares().stream()
                .map(share -> share.getStock())
                .toList());
    }
}