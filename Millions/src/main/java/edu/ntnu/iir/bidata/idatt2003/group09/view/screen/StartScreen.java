package edu.ntnu.iir.bidata.idatt2003.group09.view.screen;

import edu.ntnu.iir.bidata.idatt2003.group09.view.sound.UiSoundEffects;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import edu.ntnu.iir.bidata.idatt2003.group09.view.elements.Boss;

import javafx.scene.layout.StackPane;
import javafx.scene.Node;

public class StartScreen extends StackPane {

    public interface StartHandler {
        void onNewGame();
        void onLoadGame();
        void onSettings();
    }

    public StartScreen(StartHandler handler) {
        getStylesheets().add(getClass().getResource("/styling/startscreen.css").toExternalForm());
        setStyle("""
    -fx-background-image: url('/images/Millions_background.png');
    -fx-background-size: cover;
    -fx-background-position: center;
""");

        VBox buttonBox = new VBox(15);
        buttonBox.setAlignment(Pos.BOTTOM_CENTER);
        buttonBox.setPadding(new Insets(0, 0, 90, 0));

        Button newGameBtn = new Button("New Game");
        Button loadGameBtn = new Button("Load Game");
        Button settingsBtn = new Button("Settings");
        Button exitBtn = new Button("Exit");

        Boss boss = new Boss("What?! All out employess quit?", 500);
        newGameBtn.getStyleClass().add("start-button");
        loadGameBtn.getStyleClass().add("start-button");
        settingsBtn.getStyleClass().add("start-button");
        exitBtn.getStyleClass().add("start-button");

        int buttonWidth = 400;
        int buttonHeight = 60;
        newGameBtn.setPrefWidth(buttonWidth);
        newGameBtn.setPrefHeight(buttonHeight);
        loadGameBtn.setPrefWidth(buttonWidth);
        loadGameBtn.setPrefHeight(buttonHeight);
        settingsBtn.setPrefWidth(buttonWidth);
        settingsBtn.setPrefHeight(buttonHeight);
        exitBtn.setPrefWidth(buttonWidth);
        exitBtn.setPrefHeight(buttonHeight);
        newGameBtn.setOnAction(e -> handler.onNewGame());
        loadGameBtn.setOnAction(e -> handler.onLoadGame());
        settingsBtn.setOnAction(e -> handler.onSettings());
        exitBtn.setOnAction(e -> System.exit(0));

        UiSoundEffects.installHoverSound(newGameBtn);
        UiSoundEffects.installHoverSound(loadGameBtn);
        UiSoundEffects.installHoverSound(settingsBtn);
        UiSoundEffects.installHoverSound(exitBtn);
        UiSoundEffects.installClickSound(newGameBtn);
        UiSoundEffects.installClickSound(loadGameBtn);
        UiSoundEffects.installClickSound(settingsBtn);
        UiSoundEffects.installClickSound(exitBtn);

        syncMouseFocus(newGameBtn);
        syncMouseFocus(loadGameBtn);
        syncMouseFocus(settingsBtn);
        syncMouseFocus(exitBtn);

        buttonBox.getChildren().addAll(newGameBtn, loadGameBtn, settingsBtn, exitBtn);
        getChildren().addAll(buttonBox, boss);
        StackPane.setAlignment(buttonBox, Pos.BOTTOM_CENTER);
        StackPane.setAlignment(boss, Pos.BOTTOM_LEFT);
        StackPane.setMargin(boss, new Insets(120, 0, -90, -70));
    }

    private void syncMouseFocus(Button button) {
        button.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> button.requestFocus());
    }
}