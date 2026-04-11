package edu.ntnu.iir.bidata.idatt2003.group09.ui;

import edu.ntnu.iir.bidata.idatt2003.group09.base.Exchange;
import edu.ntnu.iir.bidata.idatt2003.group09.base.Player;
import edu.ntnu.iir.bidata.idatt2003.group09.base.Stock;
import edu.ntnu.iir.bidata.idatt2003.group09.controller.GameController;
import edu.ntnu.iir.bidata.idatt2003.group09.io.GameState;
import edu.ntnu.iir.bidata.idatt2003.group09.io.SaveManager;
import edu.ntnu.iir.bidata.idatt2003.group09.io.StockCsvReader;
import edu.ntnu.iir.bidata.idatt2003.group09.ui.screen.CreateGameScreen;
import edu.ntnu.iir.bidata.idatt2003.group09.ui.screen.LoadGameScreen;
import edu.ntnu.iir.bidata.idatt2003.group09.ui.screen.PortfolioScreen;
import edu.ntnu.iir.bidata.idatt2003.group09.ui.screen.StartScreen;
import edu.ntnu.iir.bidata.idatt2003.group09.ui.screen.TradeScreen;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import edu.ntnu.iir.bidata.idatt2003.group09.ui.screen.*;


/**
 * Main entry point for the Millions stock trading game application.
 */
public class Main extends Application {

    private static final double DESIGN_WIDTH = 1100;
    private static final double DESIGN_HEIGHT = 700;

    private BorderPane root;

    /**
     * Starts the JavaFX application. Initializes the exchange, player, and trade screen, and sets up the main stage.
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Millions - A Stock Trading Game");

        root = new BorderPane();
        root.setPrefSize(DESIGN_WIDTH, DESIGN_HEIGHT);
        root.setMinSize(DESIGN_WIDTH, DESIGN_HEIGHT);
        root.setMaxSize(DESIGN_WIDTH, DESIGN_HEIGHT);

        showStartScreen();

        StackPane viewport = new StackPane(root);
        viewport.setStyle("-fx-background-color: #202020;");

        Scene scene = new Scene(viewport, DESIGN_WIDTH, DESIGN_HEIGHT);

        root.scaleXProperty().bind(
            Bindings.createDoubleBinding(
                () -> Math.min(scene.getWidth() / DESIGN_WIDTH, scene.getHeight() / DESIGN_HEIGHT),
                scene.widthProperty(),
                scene.heightProperty()
            )
        );
        root.scaleYProperty().bind(root.scaleXProperty());

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
            public void onCreateGame(String playerName, String experienceLevel, String exchangeChoice) {
                startNewGame(playerName);
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

    private void startNewGame(String playerName) {
        try {
            String normalizedSaveFileName = SaveManager.normalizeSaveFileName(playerName);
            List<Stock> stocks = StockCsvReader.readDefaultResource();
            Player player = new Player(playerName, new BigDecimal("100000"));
            Exchange exchange = new Exchange("Main Exchange", stocks);
            GameController controller = new GameController(exchange, player, normalizedSaveFileName);
            controller.saveGame();

            setupGameUI(controller, stocks);

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

        setupGameUI(controller, state.getExchange().getStocks());
    }

    private void setupGameUI(GameController controller, List<Stock> stocks) {

        controller.setOnGameOver(() -> {
            root.setCenter(new GameOverScreen(this::showStartScreen));
        });
        TabPane tabPane = new TabPane();
        tabPane.getStylesheets().add(getClass().getResource("/styling/tabs.css").toExternalForm());
        tabPane.getStyleClass().add("game-tabs");
        StackPane newspaperContainer = new StackPane();

        Tab newspaperTab = new Tab("Newspaper", newspaperContainer);
        newspaperTab.setClosable(false);

        TradeScreen tradeScreen = new TradeScreen(controller, stocks, this::showStartScreen);
        PortfolioScreen portfolioScreen = new PortfolioScreen(controller);
        TransactionHistoryScreen transactionHistoryScreen = new TransactionHistoryScreen(controller);

        Tab tradeTab = new Tab("Trade", tradeScreen);
        Tab portfolioTab = new Tab("Portfolio", portfolioScreen);
        Tab historyTab = new Tab("Transaction History", transactionHistoryScreen);

        tradeTab.setClosable(false);
        portfolioTab.setClosable(false);
        historyTab.setClosable(false);

        tabPane.getTabs().addAll(tradeTab, portfolioTab, newspaperTab, historyTab);

        tabPane.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldTab, newTab) -> {
                    if (newTab == portfolioTab) {
                        portfolioScreen.refresh();
                    }
                    if (newTab == newspaperTab) {
                        newspaperContainer.getChildren()
                                .setAll(new NewsPaperView(controller.getWeek(), controller.getPendingNewsPaper()));
                    }
                    if (newTab == historyTab) {
                        transactionHistoryScreen.refresh();
                    }
                });

        root.setCenter(tabPane);
    }

    /**
     * write mvn javafx:run to run this method that starts the application
     *
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}