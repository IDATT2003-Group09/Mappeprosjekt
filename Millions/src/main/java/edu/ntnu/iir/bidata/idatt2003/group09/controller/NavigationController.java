package edu.ntnu.iir.bidata.idatt2003.group09.controller;

import edu.ntnu.iir.bidata.idatt2003.group09.io.SaveGameService;
import edu.ntnu.iir.bidata.idatt2003.group09.view.screen.CreateGameScreen;
import edu.ntnu.iir.bidata.idatt2003.group09.view.screen.LoadGameScreen;
import edu.ntnu.iir.bidata.idatt2003.group09.view.screen.SettingsScreen;
import edu.ntnu.iir.bidata.idatt2003.group09.view.screen.StartScreen;
import edu.ntnu.iir.bidata.idatt2003.group09.view.tutorial.TutorialOverlay;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Hovedkontroller for navigasjon mellom skjermene i applikasjonen.
 */
public class NavigationController {

    private static final Logger LOGGER = Logger.getLogger(NavigationController.class.getName());

    private final BorderPane contentRoot;
    private final TutorialOverlay tutorialOverlay;
    private final SaveGameService saveGameService;
    private final GameSessionService gameSessionService;
    private final GameViewCoordinator gameViewCoordinator;

    /**
     * Oppretter en ny navigasjonskontroller for applikasjonen.
     *
     * @param contentRoot rotcontaineren der aktive skjermer rendres
     * @param tutorialOverlay overlay som styrer tutorial-hint i UI
     */
    public NavigationController(BorderPane contentRoot, TutorialOverlay tutorialOverlay) {
        this.contentRoot = contentRoot;
        this.tutorialOverlay = tutorialOverlay;
        this.saveGameService = new SaveGameService();
        this.gameSessionService = new GameSessionService();
        this.gameViewCoordinator = new GameViewCoordinator(contentRoot, tutorialOverlay, this::showStartScreen);
    }

    /**
     * Viser startskjermen og nullstiller eventuell aktiv tutorial.
     */
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
            GameSessionService.GameSession session = gameSessionService.createTutorialSession(playerName, startingMoney);
            gameViewCoordinator.showGame(session);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Could not initialize tutorial game stock data", e);
            showStockReadError(e);
        }
    }

    private void startNewGame(String playerName, String experienceLevel, String exchangeChoice, String startingMoney) {
        try {
            GameSessionService.GameSession session = gameSessionService
                .createNewSession(playerName, experienceLevel, exchangeChoice, startingMoney);
            gameViewCoordinator.showGame(session);

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Could not initialize new game stock data", e);
            if (exchangeChoice != null && exchangeChoice.startsWith("custom:")) {
                showCreateGameScreen("That CSV was invalid! Please pick a valid file, or choose sp500 or random.");
            } else {
                showStockReadError(e);
            }
        }
    }

    private void showStockReadError(IOException e) {
        Label errorLabel = new Label("Could not read stock data: " + e.getMessage());
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px; -fx-padding: 20;");
        contentRoot.setCenter(new VBox(errorLabel));
    }

    private void loadGame(String fileName) {
        var loadedSession = gameSessionService.loadSession(fileName);
        if (loadedSession.isEmpty()) {
            LOGGER.warning("No saved game found in file: " + fileName);
            showLoadGameScreen();
            return;
        }

        gameViewCoordinator.showGame(loadedSession.get());
    }
}
