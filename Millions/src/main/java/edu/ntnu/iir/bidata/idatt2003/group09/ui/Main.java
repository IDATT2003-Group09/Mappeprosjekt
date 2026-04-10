package edu.ntnu.iir.bidata.idatt2003.group09.ui;

import edu.ntnu.iir.bidata.idatt2003.group09.base.Exchange;
import edu.ntnu.iir.bidata.idatt2003.group09.base.Player;
import edu.ntnu.iir.bidata.idatt2003.group09.base.Stock;
import edu.ntnu.iir.bidata.idatt2003.group09.controller.GameController;
import edu.ntnu.iir.bidata.idatt2003.group09.io.EnhanceCSV;
import edu.ntnu.iir.bidata.idatt2003.group09.io.GameState;
import edu.ntnu.iir.bidata.idatt2003.group09.io.SaveManager;
import edu.ntnu.iir.bidata.idatt2003.group09.io.StockCsvReader;
import edu.ntnu.iir.bidata.idatt2003.group09.io.TagsFactory;
import edu.ntnu.iir.bidata.idatt2003.group09.ui.screen.CreateGameScreen;
import edu.ntnu.iir.bidata.idatt2003.group09.ui.screen.LoadGameScreen;
import edu.ntnu.iir.bidata.idatt2003.group09.ui.screen.PortfolioScreen;
import edu.ntnu.iir.bidata.idatt2003.group09.ui.screen.StartScreen;
import edu.ntnu.iir.bidata.idatt2003.group09.ui.screen.TradeScreen;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * Main entry point for the Millions stock trading game application.
 */
public class Main extends Application {

    private BorderPane root;

    /**
     * Starts the JavaFX application. Initializes the exchange, player, and trade screen, and sets up the main stage.
     */
	@Override
	public void start(Stage primaryStage) {
        primaryStage.setTitle("Millions - A Stock Trading Game");

        root = new BorderPane();

        showStartScreen();

        Scene scene = new Scene(root, 1100, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showStartScreen() {
        StartScreen startScreen = new StartScreen(new StartScreen.StartHandler() {

            @Override
            public void onNewGame() {
                showCreateGameScreen();
            }

            @Override
            public void onLoadGame() {
                showLoadGameScreen();
            }

            @Override
            public void onSettings() {
                System.out.println("settings");
            }
        });

        root.setCenter(startScreen);
    }

    private void showCreateGameScreen() {
        CreateGameScreen createGameScreen = new CreateGameScreen(new CreateGameScreen.CreateGameHandler() {
            @Override
            public void onCreateGame(String fileName) {
                startNewGame(fileName);
            }

            @Override
            public void onBack() {
                showStartScreen();
            }
        });

        root.setCenter(createGameScreen);
    }

    private void showLoadGameScreen() {
        LoadGameScreen loadGameScreen = new LoadGameScreen(
                SaveManager.listSaveFiles(),
                new LoadGameScreen.LoadGameHandler() {
                    @Override
                    public void onLoadSelected(String fileName) {
                        loadGame(fileName);
                    }

                    @Override
                    public void onBack() {
                        showStartScreen();
                    }
                }
        );

        root.setCenter(loadGameScreen);
    }

    private void startNewGame(String fileName) {
		try {
			String normalizedSaveFileName = SaveManager.normalizeSaveFileName(fileName);
			List<Stock> stocks = StockCsvReader.readDefaultResource();
			Player player = new Player("Trader", new BigDecimal("100000"));
			Exchange exchange = new Exchange("Main Exchange", stocks);
            GameController controller = new GameController(exchange, player, normalizedSaveFileName);
            controller.saveGame();

            TradeScreen tradeScreen = new TradeScreen(controller, stocks, this::showStartScreen);
            PortfolioScreen portfolioScreen = new PortfolioScreen(controller);

            TabPane tabPane = new TabPane();
            Tab tradeTab = new Tab("Trade", tradeScreen);
            Tab portfolioTab = new Tab("Portfolio", portfolioScreen);

            tradeTab.setClosable(false);
            portfolioTab.setClosable(false);

            tabPane.getTabs().addAll(tradeTab, portfolioTab);

            tabPane.getSelectionModel().selectedItemProperty()
                    .addListener((obs, oldTab, newTab) -> {
                if (newTab == portfolioTab) {
                    portfolioScreen.refresh();
                }
            });

            root.setCenter(tabPane);

		} catch (IOException e) {
            e.printStackTrace();

			Label errorLabel = new Label("Could not read stock data: " + e.getMessage());
			errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px; -fx-padding: 20;");

            root.setCenter(new VBox(errorLabel));
		}
	}

    private void loadGame(String fileName) {
        GameState state = SaveManager.load(fileName);
        if (state == null) {
            System.out.println("No saved game found in file: " + fileName);
            showLoadGameScreen();
            return;
        }

        String normalizedSaveFileName = SaveManager.normalizeSaveFileName(fileName);
        GameController controller =
                new GameController(state.getExchange(), state.getPlayer(), normalizedSaveFileName);

        TradeScreen tradeScreen =
            new TradeScreen(controller, state.getExchange().getStocks(), this::showStartScreen);

        PortfolioScreen portfolioScreen =
                new PortfolioScreen(controller);

        TabPane tabPane = new TabPane();
        Tab tradeTab = new Tab("Trade", tradeScreen);
        Tab portfolioTab = new Tab("Portfolio", portfolioScreen);

        tradeTab.setClosable(false);
        portfolioTab.setClosable(false);

        tabPane.getTabs().addAll(tradeTab, portfolioTab);

        tabPane.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldTab, newTab) -> {
                    if (newTab == portfolioTab) {
                        portfolioScreen.refresh();
                    }
                });

        root.setCenter(tabPane);
    }

    /**
     * Launches the application.
     * Run with: mvn javafx:run
     * @param args command line arguments
     */
	public static void main(String[] args) {
        TagsFactory tagsFactory = new TagsFactory();
        tagsFactory.enhanceCsv("sp500Standard.csv", "sp500Enhanced.csv");
		launch(args);
	}
}
