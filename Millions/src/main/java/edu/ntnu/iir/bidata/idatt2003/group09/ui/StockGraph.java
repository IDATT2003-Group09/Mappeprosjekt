package edu.ntnu.iir.bidata.idatt2003.group09.ui;

import edu.ntnu.iir.bidata.idatt2003.group09.base.Stock;
import edu.ntnu.iir.bidata.idatt2003.group09.base.PriceGenerator;
import edu.ntnu.iir.bidata.idatt2003.group09.controller.GameController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class StockGraph extends BorderPane {

	private final TableView<Stock> stockTable;
	private final LineChart<Number, Number> lineChart;

	public StockGraph(List<Stock> stocks) {
		setPadding(new Insets(10));

		// Table setup
		stockTable = new TableView<>();
		stockTable.setItems(FXCollections.observableArrayList(stocks));
		stockTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		TableColumn<Stock, String> symbolCol = new TableColumn<>("Symbol");
		symbolCol.setCellValueFactory(new PropertyValueFactory<>("symbol"));
		symbolCol.setPrefWidth(100);

		TableColumn<Stock, String> companyCol = new TableColumn<>("Company");
		companyCol.setCellValueFactory(new PropertyValueFactory<>("company"));
		companyCol.setPrefWidth(180);

		stockTable.getColumns().addAll(symbolCol, companyCol);
		stockTable.setPrefWidth(280);

		// Chart setup
		NumberAxis xAxis = new NumberAxis(1, 100, 1);
		xAxis.setLabel("Week");
		NumberAxis yAxis = new NumberAxis();
		yAxis.setLabel("Price");
		lineChart = new LineChart<>(xAxis, yAxis);
		lineChart.setAnimated(false);
		lineChart.setLegendVisible(false);

		// Layout
		SplitPane splitPane = new SplitPane();
		VBox left = new VBox(stockTable);
		VBox.setVgrow(stockTable, Priority.ALWAYS);
		splitPane.getItems().addAll(left, lineChart);
		splitPane.setDividerPositions(0.3);
		setCenter(splitPane);

		// Table selection listener
		stockTable.getSelectionModel().selectedItemProperty().addListener((obs, oldStock, newStock) -> {
			if (newStock != null) {
				updateChart(newStock);
			}
		});

		// Select first stock by default
		if (!stocks.isEmpty()) {
			stockTable.getSelectionModel().selectFirst();
			updateChart(stocks.get(0));
		}
	}

    public void updateChart(Stock stock) {
        List<BigDecimal> prices = stock.getHistoricalPrices();

        XYChart.Series<Number, Number> series = new XYChart.Series<>();

        for (int i = 0; i < prices.size(); i++) {
            series.getData().add(new XYChart.Data<>(i + 1, prices.get(i)));
        }

        lineChart.getData().clear();
        lineChart.getData().add(series);

        lineChart.setTitle(stock.getCompany() + " (" + stock.getSymbol() + ")");
    }
}
