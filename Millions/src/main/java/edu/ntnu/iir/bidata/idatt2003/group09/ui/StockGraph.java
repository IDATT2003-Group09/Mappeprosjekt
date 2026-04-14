package edu.ntnu.iir.bidata.idatt2003.group09.ui;

import edu.ntnu.iir.bidata.idatt2003.group09.base.Stock;
import javafx.geometry.Insets;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.BorderPane;

import java.math.BigDecimal;
import java.util.List;

public class StockGraph extends BorderPane {

	private final LineChart<Number, Number> lineChart;

	public StockGraph(List<Stock> stocks) {
		setPadding(new Insets(10));

		// Chart setup

        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        // Round y-axis tick labels to nearest integer
        yAxis.setTickLabelFormatter(new javafx.util.StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                return String.valueOf(Math.round(object.doubleValue()));
            }
            @Override
            public Number fromString(String string) {
                try {
                    return Integer.parseInt(string);
                } catch (NumberFormatException e) {
                    return 0;
                }
            }
        });
        yAxis.setStyle("-fx-tick-label-font-family: 'ThaleahFat';");
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

        //y axis
        BigDecimal min = prices.get(start);
        BigDecimal max = prices.get(start);

        for (int i = start; i < end; i++) {
            if (prices.get(i).compareTo(min) < 0) {
                min = prices.get(i);
            }
            if (prices.get(i).compareTo(max) > 0) {
                max = prices.get(i);
            }
        }

        double minVal = min.doubleValue();
        double maxVal = max.doubleValue();

        double padding = (maxVal - minVal) * 0.1;

        if (padding == 0) {
            padding = maxVal * 0.1;
        }

        NumberAxis yAxis = (NumberAxis) lineChart.getYAxis();
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(minVal - padding);
        yAxis.setUpperBound(maxVal + padding);
        yAxis.setTickUnit((maxVal - minVal) / 5);

        yAxis.setForceZeroInRange(false);
        yAxis.setLowerBound(Math.max(0, minVal - padding));
    }
}
