package edu.ntnu.iir.bidata.idatt2003.group09.view.elements;

import edu.ntnu.iir.bidata.idatt2003.group09.model.Stock;
import java.math.BigDecimal;
import java.util.List;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextAlignment;
import javafx.util.StringConverter;

public class StockGraph extends BorderPane {

    private final LineChart<Number, Number> lineChart;
    private final Label placeholderLabel;

    public StockGraph(List<Stock> stocks) {
        setPadding(new Insets(10));

        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        yAxis.setTickLabelFormatter(new StringConverter<Number>() {
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
        getStylesheets().add(getClass().getResource("/styling/tradescreen.css").toExternalForm());

        xAxis.setTickLabelsVisible(false);
        xAxis.setTickMarkVisible(false);

        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setHorizontalGridLinesVisible(false);
        lineChart.setVerticalGridLinesVisible(false);
        lineChart.setAnimated(false);
        lineChart.setLegendVisible(false);

        placeholderLabel = new Label("Please select a stock");
        placeholderLabel.setStyle("-fx-text-fill: #9cb6db; -fx-font-size: 24px; -fx-font-weight: bold;");
        placeholderLabel.setWrapText(true);
        placeholderLabel.setAlignment(Pos.CENTER);
        placeholderLabel.setTextAlignment(TextAlignment.CENTER);
        placeholderLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        placeholderLabel.setMaxWidth(Double.MAX_VALUE);
        placeholderLabel.setMaxHeight(Double.MAX_VALUE);

        StackPane chartContainer = new StackPane(lineChart, placeholderLabel);
        StackPane.setAlignment(placeholderLabel, Pos.CENTER);
        setCenter(chartContainer);
        showNoSelectionMessage();
    }

    public void updateChart(Stock stock) {
        if (stock == null) {
            showNoSelectionMessage();
            return;
        }

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
        lineChart.setTitle("");
        lineChart.setVisible(true);
        lineChart.setManaged(true);
        placeholderLabel.setVisible(false);
        placeholderLabel.setManaged(false);

        NumberAxis xAxis = (NumberAxis) lineChart.getXAxis();
        xAxis.setAutoRanging(false);
        xAxis.setLowerBound(start + 1);
        xAxis.setUpperBound(end);
        xAxis.setTickUnit(5);

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

    public void showNoSelectionMessage() {
        lineChart.getData().clear();
        lineChart.setVisible(false);
        lineChart.setManaged(false);
        placeholderLabel.setVisible(true);
        placeholderLabel.setManaged(true);
        lineChart.setTitle("");
    }
}
