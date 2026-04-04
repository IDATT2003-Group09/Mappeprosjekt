package edu.ntnu.iir.bidata.idatt2003.group09.ui.screen;

import edu.ntnu.iir.bidata.idatt2003.group09.base.Share;
import edu.ntnu.iir.bidata.idatt2003.group09.controller.GameController;
import edu.ntnu.iir.bidata.idatt2003.group09.controller.PortfolioRow;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.beans.property.SimpleStringProperty;

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

    private final NumberFormat currencyFormat =
            NumberFormat.getCurrencyInstance(Locale.US);

    public PortfolioScreen(GameController controller) {
        this.controller = controller;

        this.table = new TableView<>();
        this.totalValueLabel = new Label();
        this.changeLabel = new Label();

        buildTable();
        buildLayout();
        refresh();
    }

    private void buildLayout() {
        totalValueLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");
        changeLabel.setStyle("-fx-font-size: 16px;");

        BorderPane top = new BorderPane();
        top.setLeft(totalValueLabel);
        top.setRight(changeLabel);
        top.setPadding(new Insets(10));

        setTop(top);
        setCenter(table);
        setPadding(new Insets(10));
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

        TableColumn<PortfolioRow, String> gainCol = new TableColumn<>("Gain/Loss");
        gainCol.setCellValueFactory(data ->
                new SimpleStringProperty(formatWithSign(data.getValue().getGain())));

        TableColumn<PortfolioRow, String> percentCol = new TableColumn<>("ROI in %");
        percentCol.setCellValueFactory(data ->
                new SimpleStringProperty(formatPercent(data.getValue().getPercentGain())));

        TableColumn<PortfolioRow, String> priceChangeCol = new TableColumn<>("Price Change");
        priceChangeCol.setCellValueFactory(data ->
                new SimpleStringProperty(formatWithSign(data.getValue().getPriceChange())));

        TableColumn<PortfolioRow, String> pricePercentCol = new TableColumn<>("Price Percentage Change");
        pricePercentCol.setCellValueFactory(data ->
                new SimpleStringProperty(formatPercent(data.getValue().getPricePercentChange())));

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

}