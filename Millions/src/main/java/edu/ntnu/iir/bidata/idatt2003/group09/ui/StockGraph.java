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

	private final LineChart<Number, Number> lineChart;

	public StockGraph(List<Stock> stocks) {
		setPadding(new Insets(10));

		// Chart setup
		NumberAxis xAxis = new NumberAxis();
		xAxis.setLabel("Week");
		NumberAxis yAxis = new NumberAxis();
		yAxis.setLabel("Price");
		lineChart = new LineChart<>(xAxis, yAxis);
		lineChart.setAnimated(false);
		lineChart.setLegendVisible(false);

		setCenter(lineChart);

        if (!stocks.isEmpty()) {
            updateChart(stocks.getFirst());
        }
	}

    public void updateChart(Stock stock) {
        List<BigDecimal> prices = stock.getHistoricalPrices();

        int totalWeeks = prices.size();
        int windowSize = 50;

        int start = Math.max(0, totalWeeks - windowSize);
        int end = totalWeeks;

        XYChart.Series<Number, Number> series = new XYChart.Series<>();

        for (int i = 0; i < prices.size(); i++) {
            series.getData().add(new XYChart.Data<>(i + 1, prices.get(i)));
        }

        lineChart.getData().clear();
        lineChart.getData().add(series);

        lineChart.setTitle(stock.getCompany() + " (" + stock.getSymbol() + ")");

        NumberAxis xAxis = (NumberAxis) lineChart.getXAxis();
        xAxis.setAutoRanging(false);
        xAxis.setLowerBound(start + 1);
        xAxis.setUpperBound(end);
        xAxis.setTickUnit(5);
    }
}
