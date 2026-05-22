package edu.ntnu.iir.bidata.idatt2003.group09.view.screen;

import edu.ntnu.iir.bidata.idatt2003.group09.controller.GameController;
import edu.ntnu.iir.bidata.idatt2003.group09.controller.PortfolioRow;
import edu.ntnu.iir.bidata.idatt2003.group09.model.screen.PortfolioScreenModel;
import edu.ntnu.iir.bidata.idatt2003.group09.model.PlayerStatus;
import edu.ntnu.iir.bidata.idatt2003.group09.view.elements.StockGraph;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import javafx.collections.ListChangeListener;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.chart.XYChart.Data;

public class PortfolioScreen extends BorderPane {

    private final GameController controller;
    private final PortfolioScreenModel model;
    private final TableView<PortfolioRow> table;

    private final Label totalValueLabel;
    private final Label changeLabel;
    private final Label cashLabel;
    private final Label statusLabel;
    private final NumberFormat currencyFormat =
        NumberFormat.getCurrencyInstance(Locale.US);

    private final LineChart<Number, Number> portfolioChart;


    public PortfolioScreen(GameController controller) {
        this.controller = controller;
        this.model = new PortfolioScreenModel();

        getStylesheets().add(getClass().getResource("/styling/portfolio.css").toExternalForm());
        getStylesheets().add(getClass().getResource("/styling/tradescreen.css").toExternalForm());
        getStyleClass().add("portfolio-screen");

        this.table = new TableView<>();
        this.totalValueLabel = new Label();
        this.changeLabel = new Label();
        this.cashLabel = new Label();
        this.statusLabel = new Label();
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("");
        xAxis.setTickLabelsVisible(false); 
        xAxis.setTickMarkVisible(false);
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Portfolio Value");
        yAxis.setTickLabelsVisible(false); 
        yAxis.setTickMarkVisible(false);
        this.portfolioChart = new LineChart<>(xAxis, yAxis);
        portfolioChart.setAnimated(true);
        portfolioChart.setLegendVisible(false);
        portfolioChart.setMinHeight(250);
        portfolioChart.setMaxHeight(250);
        portfolioChart.getStyleClass().add("trade-graph");
        portfolioChart.setHorizontalGridLinesVisible(false);
        portfolioChart.setVerticalGridLinesVisible(false);

        buildTable();
        buildLayout();
        bindModelToView();
        refresh();
    }

    private void buildLayout() {
        totalValueLabel.getStyleClass().add("portfolio-total-value");
        changeLabel.getStyleClass().add("portfolio-change");
        cashLabel.getStyleClass().add("portfolio-cash");
        statusLabel.getStyleClass().addAll("portfolio-status", "portfolio-cash");

        Button sellAllButton = new Button("Sell All");
        sellAllButton.getStyleClass().add("portfolio-sell-all-button");
        sellAllButton.setOnAction(e -> {
            controller.sellAllShares();
            refresh();
        });

        HBox topBox = new HBox(20, totalValueLabel, changeLabel, cashLabel, statusLabel, sellAllButton);
        topBox.getStyleClass().add("portfolio-top-box");
        topBox.setPadding(new Insets(10));
        
        topBox.getStyleClass().add("portfolio-top-box");
        topBox.setPadding(new Insets(10));

        table.getStyleClass().add("portfolio-table");
        table.setItems(model.rowsProperty());

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

        TableColumn<PortfolioRow, String> sectorCol = new TableColumn<>("Sector");
        sectorCol.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue().getSector()));

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

        TableColumn<PortfolioRow, Void> sellAllCol = new TableColumn<>("Sell All");
        sellAllCol.setCellFactory(col -> new TableCell<>() {
            private final Button sellButton = new Button("Sell All");

            {
                sellButton.getStyleClass().add("portfolio-sell-all-button");
                sellButton.setOnAction(e -> {
                    PortfolioRow row = getTableView().getItems().get(getIndex());
                    controller.sellAllShares(row.getSymbol());
                    refresh();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : sellButton);
            }
        });

        table.getColumns().addAll(
                symbolCol,
                sectorCol,
                quantityCol,
                valueCol,
                gainCol,
                percentCol,
                priceChangeCol,
                pricePercentCol,
                sellAllCol
        );

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    public void refresh() {
        model.updateFromGameState(controller);
    }

    private void bindModelToView() {
        model.currentNetWorthProperty().addListener((obs, oldVal, newVal) -> updateSummaryLabels());
        model.previousNetWorthProperty().addListener((obs, oldVal, newVal) -> updateSummaryLabels());
        model.cashProperty().addListener((obs, oldVal, newVal) -> {
            cashLabel.setText("Cash: " + currencyFormat.format(newVal == null ? BigDecimal.ZERO : newVal));
        });
        model.statusProperty().addListener((obs, oldVal, newVal) -> {
            statusLabel.setText("Status: " + (newVal != null ? newVal.name() : "-"));
        });
        model.chartValuesProperty().addListener((ListChangeListener<BigDecimal>) change -> updateChart());

        updateSummaryLabels();
        BigDecimal initCash = model.cashProperty().get();
        cashLabel.setText("Cash: " + currencyFormat.format(initCash == null ? BigDecimal.ZERO : initCash));
        PlayerStatus initStatus = model.statusProperty().get();
        statusLabel.setText("Status: " + (initStatus != null ? initStatus.name() : "-"));
        updateChart();
    }

    private void updateSummaryLabels() {
        BigDecimal current = model.currentNetWorthProperty().get();
        BigDecimal previous = model.previousNetWorthProperty().get();
        if (current == null) {
            current = BigDecimal.ZERO;
        }
        if (previous == null) {
            previous = BigDecimal.ZERO;
        }

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
        changeLabel.setText(formatWithSign(change) + " (" + formatPercent(percentChange) + ")");
    }

    private void updateChart() {
        Series<Number, Number> series = new Series<>();
        List<BigDecimal> values = model.chartValuesProperty();
        for (int i = 0; i < values.size(); i++) {
            series.getData().add(new Data<>(i + 1, values.get(i)));
        }
        portfolioChart.getData().clear();
        portfolioChart.getData().add(series);
        portfolioChart.setTitle("");
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