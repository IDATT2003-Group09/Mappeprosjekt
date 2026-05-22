package edu.ntnu.iir.bidata.idatt2003.group09.controller;

import edu.ntnu.iir.bidata.idatt2003.group09.view.elements.NewsPaperView;
import edu.ntnu.iir.bidata.idatt2003.group09.view.screen.GameOverScreen;
import edu.ntnu.iir.bidata.idatt2003.group09.view.screen.PortfolioScreen;
import edu.ntnu.iir.bidata.idatt2003.group09.view.screen.SettingsScreen;
import edu.ntnu.iir.bidata.idatt2003.group09.view.screen.TradeScreenView;
import edu.ntnu.iir.bidata.idatt2003.group09.view.screen.TransactionHistoryScreen;
import edu.ntnu.iir.bidata.idatt2003.group09.view.sound.UiSoundEffects;
import edu.ntnu.iir.bidata.idatt2003.group09.view.tutorial.TutorialOverlay;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

/**
 * Koordinerer oppsett og oppførsel for hovedvisningen under selve spillet.
 */
public class GameViewCoordinator {

    private final BorderPane contentRoot;
    private final TutorialOverlay tutorialOverlay;
    private final Runnable onReturnToStart;

    /**
     * Oppretter en ny koordinator for spillvisningen.
     *
     * @param contentRoot rotcontaineren der spillinnholdet vises
     * @param tutorialOverlay overlay som styrer tutorial-flyt
     * @param onReturnToStart callback som kjøres når brukeren går tilbake til start
     */
    public GameViewCoordinator(BorderPane contentRoot, TutorialOverlay tutorialOverlay, Runnable onReturnToStart) {
        this.contentRoot = contentRoot;
        this.tutorialOverlay = tutorialOverlay;
        this.onReturnToStart = onReturnToStart;
    }

    /**
     * Viser en aktiv spillsesjon i hovedområdet og kobler alle faner til riktig oppførsel.
     *
     * @param session spillsesjonen som skal presenteres
     */
    public void showGame(GameSessionService.GameSession session) {
        GameController controller = session.controller();
        boolean tutorialMode = session.tutorialMode();

        Runnable onGameOver = () -> contentRoot.setCenter(new GameOverScreen(controller, onReturnToStart));

        if (tutorialMode) {
            tutorialOverlay.startTutorial();
        } else {
            tutorialOverlay.stopTutorial();
        }
        controller.setOnGameOver(onGameOver);

        TradeScreenView tradeScreen = new TradeScreenView(
            controller,
            session.stocks(),
            onReturnToStart,
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
                onReturnToStart.run();
            }
        });
    }
}
