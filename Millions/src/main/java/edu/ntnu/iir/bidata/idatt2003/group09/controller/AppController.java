package edu.ntnu.iir.bidata.idatt2003.group09.controller;

import edu.ntnu.iir.bidata.idatt2003.group09.io.EnhanceCSV;
import edu.ntnu.iir.bidata.idatt2003.group09.io.GameState;
import edu.ntnu.iir.bidata.idatt2003.group09.io.SaveGameService;
import edu.ntnu.iir.bidata.idatt2003.group09.io.SaveManager;
import edu.ntnu.iir.bidata.idatt2003.group09.io.StockCsvReader;
import edu.ntnu.iir.bidata.idatt2003.group09.io.TagsFactory;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Exchange;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Player;
import edu.ntnu.iir.bidata.idatt2003.group09.model.PlayerStatus;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Stock;
import edu.ntnu.iir.bidata.idatt2003.group09.view.elements.NewsPaperView;
import edu.ntnu.iir.bidata.idatt2003.group09.view.screen.CreateGameScreen;
import edu.ntnu.iir.bidata.idatt2003.group09.view.screen.GameOverScreen;
import edu.ntnu.iir.bidata.idatt2003.group09.view.screen.LoadGameScreen;
import edu.ntnu.iir.bidata.idatt2003.group09.view.screen.PortfolioScreen;
import edu.ntnu.iir.bidata.idatt2003.group09.view.screen.SettingsScreen;
import edu.ntnu.iir.bidata.idatt2003.group09.view.screen.StartScreen;
import edu.ntnu.iir.bidata.idatt2003.group09.view.screen.TradeScreenView;
import edu.ntnu.iir.bidata.idatt2003.group09.view.screen.TransactionHistoryScreen;
import edu.ntnu.iir.bidata.idatt2003.group09.view.sound.UiSoundEffects;
import edu.ntnu.iir.bidata.idatt2003.group09.view.tutorial.TutorialOverlay;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppController {

    private static final Logger LOGGER = Logger.getLogger(AppController.class.getName());

    private final BorderPane contentRoot;
    private final TutorialOverlay tutorialOverlay;
    private final SaveGameService saveGameService;

    public AppController(BorderPane contentRoot, TutorialOverlay tutorialOverlay) {
        this.contentRoot = contentRoot;
        this.tutorialOverlay = tutorialOverlay;
        this.saveGameService = new SaveGameService();
    }

    public void showStartScreen() {
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
        }, saveGameService.countSaveFiles());

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

        setCenterView(settingsScreen, true);
        contentRoot.applyCss();
        contentRoot.layout();
    }

    private void showCreateGameScreen() {
        showCreateGameScreen(null);
    }

    private void showCreateGameScreen(String bossMessage) {
        CreateGameScreen createGameScreen = new CreateGameScreen(new CreateGameScreen.CreateGameHandler() {
            @Override
            public void onCreateGame(String playerName, String experienceLevel, String exchangeChoice, String startingMoney) {
                if ("tutorial".equalsIgnoreCase(experienceLevel)) {
                    startTutorialGame(playerName, startingMoney);
                } else {
                    startNewGame(playerName, experienceLevel, exchangeChoice, startingMoney);
                }
            }

            @Override
            public boolean doesSaveFileExist(String playerName) {
                return saveGameService.doesSaveFileExist(playerName);
            }

            @Override
            public void onBack() {
                showStartScreen();
            }
        }, bossMessage);

        setCenterView(createGameScreen, true);
    }

    private void showLoadGameScreen() {
        LoadGameScreen loadGameScreen = new LoadGameScreen(
            saveGameService.listSaveGames(),
            new LoadGameScreen.LoadGameHandler() {
                @Override
                public void onLoadSelected(String fileName) {
                    if (fileName != null) {
                        loadGame(fileName);
                    } else {
                        showLoadGameScreen();
                    }
                }

                @Override
                public void onDeleteSelected(String fileName) {
                    saveGameService.deleteSaveFile(fileName);
                    showLoadGameScreen();
                }

                @Override
                public void onBack() {
                    showStartScreen();
                }
            }
        );

        setCenterView(loadGameScreen, true);
    }

    private void setCenterView(Node centerView, boolean stopTutorial) {
        if (stopTutorial) {
            tutorialOverlay.stopTutorial();
        }
        contentRoot.setCenter(centerView);
    }

    private void startTutorialGame(String playerName, String startingMoney) {
        try {
            String normalizedSaveFileName = SaveManager.normalizeSaveFileName(playerName + "-tutorial");
            List<Stock> stocks = StockCsvReader.readFromResource("/csv/output/sp500.csv");

            BigDecimal startMoney = parseStartingMoney(startingMoney);
            Player player = new Player(playerName, startMoney, "Easy");
            Exchange exchange = new Exchange("S&P 500 Tutorial", stocks);

            PlayerStatus status = player.getStatus(0);
            exchange.setCommissionRate(getTutorialCommissionRate(status));

            GameController controller = new GameController(exchange, player, normalizedSaveFileName);
            controller.saveGame();

            tutorialOverlay.startTutorial();
            setupGameUI(controller, stocks, true);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Could not initialize tutorial game stock data", e);

            Label errorLabel = new Label("Could not read stock data: " + e.getMessage());
            errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px; -fx-padding: 20;");
            contentRoot.setCenter(new VBox(errorLabel));
        }
    }

    private void startNewGame(String playerName, String experienceLevel, String exchangeChoice, String startingMoney) {
        try {
            String normalizedSaveFileName = SaveManager.normalizeSaveFileName(playerName);
            List<Stock> stocks = loadStocksForExchange(exchangeChoice);
            if (stocks == null) {
                return;
            }
            BigDecimal startMoney = parseStartingMoney(startingMoney);
            Player player = new Player(playerName, startMoney, experienceLevel);
            Exchange exchange = new Exchange(getExchangeName(exchangeChoice), stocks);

            exchange.setCommissionRate(getDifficultyCommissionRate(experienceLevel));

            GameController controller = new GameController(exchange, player, normalizedSaveFileName);
            controller.saveGame();

            tutorialOverlay.stopTutorial();
            setupGameUI(controller, stocks, false);

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Could not initialize new game stock data", e);
            if (exchangeChoice != null && exchangeChoice.startsWith("custom:")) {
                showCreateGameScreen("That CSV was invalid! Please pick a valid file, or choose sp500 or random.");
            } else {
                Label errorLabel = new Label("Could not read stock data: " + e.getMessage());
                errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px; -fx-padding: 20;");
                contentRoot.setCenter(new VBox(errorLabel));
            }
        }
    }

    private BigDecimal parseStartingMoney(String money) {
        try {
            return new BigDecimal(money);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Invalid starting money input: {0}. Using default.", money);
            return new BigDecimal("100000");
        }
    }

    private BigDecimal getTutorialCommissionRate(PlayerStatus status) {
        return switch (status) {
            case NOVICE -> new BigDecimal("0.005");
            case INVESTOR -> new BigDecimal("0.003");
            case SPECULATOR -> new BigDecimal("0.001");
        };
    }

    private BigDecimal getDifficultyCommissionRate(String experienceLevel) {
        if ("Medium".equalsIgnoreCase(experienceLevel)) {
            return new BigDecimal("0.01");
        }
        if ("Hard".equalsIgnoreCase(experienceLevel)) {
            return new BigDecimal("0.02");
        }
        return new BigDecimal("0.005");
    }

    private List<Stock> loadStocksForExchange(String exchangeChoice) throws IOException {
        if (exchangeChoice == null) {
            return StockCsvReader.readDefaultResource();
        }

        String trimmed = exchangeChoice.trim().toLowerCase();
        if (trimmed.equals("random")) {
            return StockCsvReader.readFromResource("/csv/output/random.csv");
        } else if (trimmed.equals("sp500")) {
            return StockCsvReader.readFromResource("/csv/output/sp500.csv");
        } else if (trimmed.startsWith("custom:")) {
            String filePath = trimmed.substring("custom:".length());
            Path selectedCsvFile = Path.of(filePath);
            Path enhancedCsv = enhanceCustomCsv(selectedCsvFile);
            return StockCsvReader.readFromFile(enhancedCsv);
        } else {
            return StockCsvReader.readDefaultResource();
        }
    }

    private String getExchangeName(String exchangeChoice) {
        if (exchangeChoice == null) {
            return "Main Exchange";
        }

        return switch (exchangeChoice.trim().toLowerCase()) {
            case "random" -> "Random Exchange";
            case "sp500" -> "S&P 500";
            default -> exchangeChoice.trim().toLowerCase().startsWith("custom:")
                ? "Custom Exchange"
                : "Main Exchange";
        };
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
            LOGGER.warning("No saved game found in file: " + fileName);
            showLoadGameScreen();
            return;
        }

        String normalizedSaveFileName = SaveManager.normalizeSaveFileName(fileName);

        GameController controller =
            new GameController(state.getExchange(), state.getPlayer(), normalizedSaveFileName, state.getProgress());

        setupGameUI(controller, state.getExchange().getStocks(), false);
    }

    private void setupGameUI(GameController controller, List<Stock> stocks, boolean tutorialMode) {

        Runnable onGameOver = () -> contentRoot.setCenter(new GameOverScreen(controller, this::showStartScreen));

        if (!tutorialMode) {
            tutorialOverlay.stopTutorial();
        }
        controller.setOnGameOver(onGameOver);

        TradeScreenView tradeScreen = new TradeScreenView(
            controller,
            stocks,
            this::showStartScreen,
            onGameOver,
            tutorialMode,
            tutorialOverlay
        );
        PortfolioScreen portfolioScreen = new PortfolioScreen(controller);
        TransactionHistoryScreen transactionHistoryScreen = new TransactionHistoryScreen(controller);

        StackPane newspaperContainer = new StackPane();
        TabPane tabPane = createGameTabPane();
        Tab newspaperTab = createNonClosableTab("Newspaper", newspaperContainer);
        Tab tradeTab = createNonClosableTab("Trade", tradeScreen);
        Tab portfolioTab = createNonClosableTab("Portfolio", portfolioScreen);
        Tab historyTab = createNonClosableTab("Transaction History", transactionHistoryScreen);
        Tab settingsTab = createInGameSettingsTab(tabPane, tradeTab);
        Tab spacerTab = createSpacerTab();
        Tab saveQuitTab = createNonClosableTab("Save & Quit", null);

        tabPane.getTabs().addAll(tradeTab, portfolioTab, newspaperTab, historyTab, settingsTab, spacerTab, saveQuitTab);
        installTabSelectionBehavior(
            tabPane,
            tutorialMode,
            controller,
            portfolioScreen,
            transactionHistoryScreen,
            newspaperContainer,
            tradeTab,
            portfolioTab,
            newspaperTab,
            historyTab,
            saveQuitTab
        );

        Platform.runLater(() -> UiSoundEffects.installHoverSound(tabPane));

        contentRoot.setCenter(tabPane);
    }

    private TabPane createGameTabPane() {
        TabPane tabPane = new TabPane();
        tabPane.getStylesheets().add(getClass().getResource("/styling/tabs.css").toExternalForm());
        tabPane.getStyleClass().add("game-tabs");
        return tabPane;
    }

    private Tab createNonClosableTab(String title, Node content) {
        Tab tab = new Tab(title, content);
        tab.setClosable(false);
        return tab;
    }

    private Tab createInGameSettingsTab(TabPane tabPane, Tab tradeTab) {
        SettingsScreen inGameSettingsScreen = new SettingsScreen(() -> tabPane.getSelectionModel().select(tradeTab));
        return createNonClosableTab("Settings", inGameSettingsScreen);
    }

    private Tab createSpacerTab() {
        Tab spacerTab = createNonClosableTab("", null);
        spacerTab.setDisable(true);
        spacerTab.getStyleClass().add("spacer-tab");
        return spacerTab;
    }

    private void installTabSelectionBehavior(
        TabPane tabPane,
        boolean tutorialMode,
        GameController controller,
        PortfolioScreen portfolioScreen,
        TransactionHistoryScreen transactionHistoryScreen,
        StackPane newspaperContainer,
        Tab tradeTab,
        Tab portfolioTab,
        Tab newspaperTab,
        Tab historyTab,
        Tab saveQuitTab
    ) {
        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab == portfolioTab) {
                portfolioScreen.refresh();
            }
            if (newTab == newspaperTab) {
                newspaperContainer.getChildren().setAll(new NewsPaperView(controller.getWeek(), controller.getPendingNewsPaper()));
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
            if (newTab == tradeTab && tutorialMode && tutorialOverlay.isActive()) {
                tutorialOverlay.onStockSelected();
                tutorialOverlay.onBuyButtonClicked();
                tutorialOverlay.onTradeScreenViewed();
            }
            if (newTab == saveQuitTab) {
                controller.saveGame();
                showStartScreen();
            }
        });
    }
}
