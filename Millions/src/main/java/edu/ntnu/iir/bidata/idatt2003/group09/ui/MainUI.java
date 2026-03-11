package edu.ntnu.iir.bidata.idatt2003.group09.ui;

import edu.ntnu.iir.bidata.idatt2003.group09.Stock;
import edu.ntnu.iir.bidata.idatt2003.group09.io.StockCsvReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainUI extends Application {

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Stock Overview");

		try {
			List<Stock> stocks = StockCsvReader.readDefaultResource();

			Label header = new Label("Stocks loaded from sp500.csv: " + stocks.size());
			header.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 10;");

			TableView<Stock> stockTable = createStockTable();
			stockTable.setItems(FXCollections.observableArrayList(stocks));

			BorderPane root = new BorderPane();
			root.setTop(header);
			root.setCenter(stockTable);

			Scene scene = new Scene(root, 900, 650);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			Label errorLabel = new Label("Could not read stock data: " + e.getMessage());
			errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px; -fx-padding: 20;");

			Scene scene = new Scene(new VBox(errorLabel), 700, 200);
			primaryStage.setScene(scene);
			primaryStage.show();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	private TableView<Stock> createStockTable() {
		NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);

		TableColumn<Stock, String> symbolColumn = new TableColumn<>("Symbol");
		symbolColumn.setCellValueFactory(data ->
				new SimpleStringProperty(data.getValue().getSymbol()));

		TableColumn<Stock, String> companyColumn = new TableColumn<>("Company");
		companyColumn.setCellValueFactory(data ->
				new SimpleStringProperty(data.getValue().getCompany()));

		TableColumn<Stock, String> priceColumn = new TableColumn<>("Price");
		priceColumn.setCellValueFactory(data ->
				new SimpleStringProperty(currencyFormat.format(data.getValue().getSalesPrice())));

		symbolColumn.setPrefWidth(120);
		companyColumn.setPrefWidth(540);
		priceColumn.setPrefWidth(160);

		TableView<Stock> stockTable = new TableView<>();
		stockTable.getColumns().add(symbolColumn);
		stockTable.getColumns().add(companyColumn);
		stockTable.getColumns().add(priceColumn);
		stockTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

		return stockTable;
	}
}
