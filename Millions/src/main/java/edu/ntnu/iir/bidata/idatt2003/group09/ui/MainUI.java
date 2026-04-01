package edu.ntnu.iir.bidata.idatt2003.group09.ui;

import edu.ntnu.iir.bidata.idatt2003.group09.base.Stock;
import edu.ntnu.iir.bidata.idatt2003.group09.io.StockCsvReader;
import java.io.IOException;
import java.util.List;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import edu.ntnu.iir.bidata.idatt2003.group09.ui.StockGraph;


public class MainUI extends Application {

  /**
   * Starts the JavaFX application. Initializes the exchange, player, and trade screen, and sets up the main stage.
   */
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Stock Trading");

		try {
			List<Stock> stocks = StockCsvReader.readDefaultResource();
			//Player player = new Player("Trader", new BigDecimal("100000"));
			//Exchange exchange = new Exchange("Main Exchange", stocks);
			//tradeScreen screen = new tradeScreen(exchange, player, stocks);

			StockGraph stockGraph = new StockGraph(stocks);
			Scene scene = new Scene(stockGraph, 900, 650);
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

  /**
   * write mvn javafx:run to run this method that starts the application
   * @param args
   */
	public static void main(String[] args) {
		launch(args);
	}


}
