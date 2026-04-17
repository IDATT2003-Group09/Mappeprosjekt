package edu.ntnu.iir.bidata.idatt2003.group09.ui;

import edu.ntnu.iir.bidata.idatt2003.group09.base.Exchange;
import edu.ntnu.iir.bidata.idatt2003.group09.base.Player;
import edu.ntnu.iir.bidata.idatt2003.group09.base.Stock;
import edu.ntnu.iir.bidata.idatt2003.group09.base.PlayerStatus;
import edu.ntnu.iir.bidata.idatt2003.group09.controller.GameController;
import edu.ntnu.iir.bidata.idatt2003.group09.io.GameState;
import edu.ntnu.iir.bidata.idatt2003.group09.io.SaveManager;
import edu.ntnu.iir.bidata.idatt2003.group09.io.StockCsvReader;
import edu.ntnu.iir.bidata.idatt2003.group09.io.EnhanceCSV;
import edu.ntnu.iir.bidata.idatt2003.group09.io.TagsFactory;
import edu.ntnu.iir.bidata.idatt2003.group09.ui.screen.CreateGameScreen;
import edu.ntnu.iir.bidata.idatt2003.group09.ui.screen.LoadGameScreen;
import edu.ntnu.iir.bidata.idatt2003.group09.ui.screen.PortfolioScreen;
import edu.ntnu.iir.bidata.idatt2003.group09.ui.screen.SettingsScreen;
import edu.ntnu.iir.bidata.idatt2003.group09.ui.screen.StartScreen;
import edu.ntnu.iir.bidata.idatt2003.group09.ui.screen.TradeScreen;
import edu.ntnu.iir.bidata.idatt2003.group09.ui.UiSoundEffects;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import edu.ntnu.iir.bidata.idatt2003.group09.ui.screen.*;


/**
 * Main entry point for the Millions stock trading game application.
 */
public class Main extends Application {

    private static final double DESIGN_WIDTH = 1100;
    private static final double DESIGN_HEIGHT = 700;

    private StackPane root;
    private BorderPane contentRoot;
    private TutorialOverlay tutorialOverlay;

    /**
     * Starts the JavaFX application. Initializes the exchange, player, and trade screen, and sets up the main stage.
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Millions - A Stock Trading Game");

        UiSoundEffects.startBackgroundMusic();

        contentRoot = new BorderPane();
        contentRoot.setPrefSize(DESIGN_WIDTH, DESIGN_HEIGHT);
        contentRoot.setMinSize(DESIGN_WIDTH, DESIGN_HEIGHT);
        contentRoot.setMaxSize(DESIGN_WIDTH, DESIGN_HEIGHT);

        tutorialOverlay = new TutorialOverlay();

        root = new StackPane(contentRoot, tutorialOverlay.getLayer());
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

    @Override
    public void stop() {
        UiSoundEffects.stopBackgroundMusic();
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
                showSettingsScreen();
            }
        });

        tutorialOverlay.stopTutorial();
        contentRoot.setCenter(startScreen);
    }

    private void showSettingsScreen() {
        SettingsScreen settingsScreen = new SettingsScreen(new SettingsScreen.SettingsHandler() {
            @Override
            public void onBack() {
                showStartScreen();
            }
        });

        tutorialOverlay.stopTutorial();
        contentRoot.setCenter(settingsScreen);
        contentRoot.applyCss();
        contentRoot.layout();
    }

    private void showCreateGameScreen() {
        CreateGameScreen createGameScreen = new CreateGameScreen(new CreateGameScreen.CreateGameHandler() {
            @Override
            public void onCreateGame(String playerName, String experienceLevel, String exchangeChoice) {
                if ("tutorial".equalsIgnoreCase(experienceLevel)) {
                    startTutorialGame(playerName);
                } else {
                    startNewGame(playerName, experienceLevel, exchangeChoice);
                }
            }

            @Override
            public void onBack() {
                showStartScreen();
            }
        });

        tutorialOverlay.stopTutorial();
        contentRoot.setCenter(createGameScreen);
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

        tutorialOverlay.stopTutorial();
        contentRoot.setCenter(loadGameScreen);
    }

    private void startTutorialGame(String playerName) {
        try {
            String normalizedSaveFileName = SaveManager.normalizeSaveFileName(playerName + "-tutorial");
            List<Stock> stocks = StockCsvReader.readFromResource("/csv/output/sp500.csv");

            Player player = new Player(playerName, new BigDecimal("100000"), "Easy");
            Exchange exchange = new Exchange("S&P 500 Tutorial", stocks);

            // Determine commission rate based on player status

            PlayerStatus status = player.getStatus(0); // week 0 at game start
            BigDecimal commissionRate = switch (status) {
                case NOVICE -> new BigDecimal("0.005");
                case INVESTOR -> new BigDecimal("0.003");
                case SPECULATOR -> new BigDecimal("0.001");
            };
            exchange.setCommissionRate(commissionRate);

            GameController controller = new GameController(exchange, player, normalizedSaveFileName);
            controller.saveGame();

            tutorialOverlay.startTutorial();
            setupGameUI(controller, stocks, true);
        } catch (IOException e) {
            e.printStackTrace();

            Label errorLabel = new Label("Could not read stock data: " + e.getMessage());
            errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px; -fx-padding: 20;");
            contentRoot.setCenter(new VBox(errorLabel));
        }
    }

    private void startNewGame(String playerName, String experienceLevel, String exchangeChoice) {
        try {
            String normalizedSaveFileName = SaveManager.normalizeSaveFileName(playerName);
            List<Stock> stocks = loadStocksForExchange(exchangeChoice);
            if (stocks == null) {
                return;
            }
            Player player = new Player(playerName, new BigDecimal("100000"), experienceLevel);
            Exchange exchange = new Exchange(getExchangeName(exchangeChoice), stocks);

            // Set commission rate based on difficulty
            java.math.BigDecimal commissionRate;
            if ("Easy".equalsIgnoreCase(experienceLevel)) {
                commissionRate = new java.math.BigDecimal("0.005");
            } else if ("Medium".equalsIgnoreCase(experienceLevel)) {
                commissionRate = new java.math.BigDecimal("0.01");
            } else if ("Hard".equalsIgnoreCase(experienceLevel)) {
                commissionRate = new java.math.BigDecimal("0.02");
            } else {
                commissionRate = new java.math.BigDecimal("0.005"); // fallback
            }
            exchange.setCommissionRate(commissionRate);

            GameController controller = new GameController(exchange, player, normalizedSaveFileName);
            controller.saveGame();

            tutorialOverlay.stopTutorial();
            setupGameUI(controller, stocks, false);

        } catch (IOException e) {
            e.printStackTrace();

            Label errorLabel = new Label("Could not read stock data: " + e.getMessage());
            errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px; -fx-padding: 20;");

            contentRoot.setCenter(new VBox(errorLabel));
        }
    }

    private List<Stock> loadStocksForExchange(String exchangeChoice) throws IOException {
        if (exchangeChoice == null) {
            return StockCsvReader.readDefaultResource();
        }

        return switch (exchangeChoice.trim().toLowerCase()) {
            case "random" -> StockCsvReader.readFromResource("/csv/output/random.csv");
            case "sp500" -> StockCsvReader.readFromResource("/csv/output/sp500.csv");
            case "custom" -> {
                Path selectedCsvFile = promptCustomCsvFile();
                if (selectedCsvFile == null) {
                    yield null;
                }
                Path enhancedCsv = enhanceCustomCsv(selectedCsvFile);
                yield StockCsvReader.readFromFile(enhancedCsv);
            }
            default -> StockCsvReader.readDefaultResource();
        };
    }

    private String getExchangeName(String exchangeChoice) {
        if (exchangeChoice == null) {
            return "Main Exchange";
        }

        return switch (exchangeChoice.trim().toLowerCase()) {
            case "random" -> "Random Exchange";
            case "sp500" -> "S&P 500";
            case "custom" -> "Custom Exchange";
            default -> "Main Exchange";
        };
    }

    private Path promptCustomCsvFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select CSV file");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV files", "*.csv")
        );

        java.io.File selectedFile = fileChooser.showOpenDialog(root.getScene().getWindow());
        return selectedFile == null ? null : selectedFile.toPath();
    }

    private Path enhanceCustomCsv(Path selectedCsvFile) throws IOException {
        Path enhancedFile = Files.createTempFile("millions-custom-enhanced-", ".csv");
        EnhanceCSV enhancer = new EnhanceCSV(selectedCsvFile.toString(), new TagsFactory().getTags());
        enhancer.writeEnhancedCsv(enhancedFile.toString());
        enhancedFile.toFile().deleteOnExit();
        return enhancedFile;
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

        setupGameUI(controller, state.getExchange().getStocks(), false);
    }

    private void setupGameUI(GameController controller, List<Stock> stocks, boolean tutorialMode) {

        if (!tutorialMode) {
            tutorialOverlay.stopTutorial();
            controller.setOnGameOver(() -> {
                contentRoot.setCenter(new GameOverScreen(this::showStartScreen));
            });
        }
        TabPane tabPane = new TabPane();
        tabPane.getStylesheets().add(getClass().getResource("/styling/tabs.css").toExternalForm());
        tabPane.getStyleClass().add("game-tabs");
        StackPane newspaperContainer = new StackPane();

        Tab newspaperTab = new Tab("Newspaper", newspaperContainer);
        newspaperTab.setClosable(false);

        TradeScreen tradeScreen = new TradeScreen(controller, stocks, this::showStartScreen, tutorialMode, tutorialOverlay);
        PortfolioScreen portfolioScreen = new PortfolioScreen(controller);
        TransactionHistoryScreen transactionHistoryScreen = new TransactionHistoryScreen(controller);

        Tab tradeTab = new Tab("Trade", tradeScreen);
        Tab portfolioTab = new Tab("Portfolio", portfolioScreen);
        Tab historyTab = new Tab("Transaction History", transactionHistoryScreen);
        SettingsScreen inGameSettingsScreen = new SettingsScreen(() ->
            tabPane.getSelectionModel().select(tradeTab));
        Tab settingsTab = new Tab("Settings", inGameSettingsScreen);

        tradeTab.setClosable(false);
        portfolioTab.setClosable(false);
        historyTab.setClosable(false);
        settingsTab.setClosable(false);

        Tab spacerTab = new Tab("");
        spacerTab.setDisable(true);
        spacerTab.setClosable(false);
        spacerTab.getStyleClass().add("spacer-tab");

        Tab saveQuitTab = new Tab("Save & Quit");
        saveQuitTab.setClosable(false);
        tabPane.getTabs().addAll(tradeTab, portfolioTab, newspaperTab, historyTab, settingsTab, spacerTab, saveQuitTab);

        tabPane.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldTab, newTab) -> {
                    if (newTab == portfolioTab) {
                        portfolioScreen.refresh();
                        if (tutorialMode) {
                        }
                    }
                    if (newTab == newspaperTab) {
                        newspaperContainer.getChildren()
                                .setAll(new NewsPaperView(controller.getWeek(), controller.getPendingNewsPaper()));
                        if (tutorialMode) {
                            tutorialOverlay.onNewspaperViewed();
                        }
                    }
                    if (newTab == historyTab) {
                        transactionHistoryScreen.refresh();
                        if (tutorialMode) {
                            tutorialOverlay.onTransactionHistoryViewed();
                        }
                    }
                    if (newTab == tradeTab && tutorialMode) {
                        if (tutorialOverlay.isActive()) {
                            tutorialOverlay.onStockSelected();
                            tutorialOverlay.onBuyButtonClicked();
                            tutorialOverlay.onTradeScreenViewed();
                        }
                    }
                    if (newTab == saveQuitTab) {
                        controller.saveGame();
                        showStartScreen();
                    }
                });

        Platform.runLater(() -> UiSoundEffects.installHoverSound(tabPane));

        contentRoot.setCenter(tabPane);
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