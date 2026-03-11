package edu.ntnu.iir.bidata.idatt2003.group09.ui;

import edu.ntnu.iir.bidata.idatt2003.group09.Exchange;
import edu.ntnu.iir.bidata.idatt2003.group09.Player;
import edu.ntnu.iir.bidata.idatt2003.group09.Stock;
import edu.ntnu.iir.bidata.idatt2003.group09.io.StockCsvReader;
import edu.ntnu.iir.bidata.idatt2003.group09.ui.screen.tradeScreen;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class MainUI extends Application {

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Stock Trading");

		try {
			List<Stock> stocks = StockCsvReader.readDefaultResource();
			Player player = new Player("Trader", new BigDecimal("100000"));
			Exchange exchange = new Exchange("Main Exchange", stocks);
			tradeScreen screen = new tradeScreen(exchange, player, stocks);

			Scene scene = new Scene(screen, 900, 650);
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


}
