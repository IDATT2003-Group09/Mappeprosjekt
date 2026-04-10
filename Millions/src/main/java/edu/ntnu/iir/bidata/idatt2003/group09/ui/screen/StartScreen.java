package edu.ntnu.iir.bidata.idatt2003.group09.ui.screen;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class StartScreen extends VBox {

    public interface StartHandler {
        void onNewGame();
        void onLoadGame();
        void onSettings();
    }

    public StartScreen(StartHandler handler) {

        getStylesheets().add(getClass().getResource("/styling/startscreen.css").toExternalForm());

        setSpacing(15);
        setAlignment(Pos.BOTTOM_CENTER);
        setPadding(new Insets(0, 0, 90, 0));
        setStyle("""
    -fx-background-image: url('/images/Millions_background.png');
    -fx-background-size: cover;
    -fx-background-position: center;
""");

        Button newGameBtn = new Button("New Game");
        Button loadGameBtn = new Button("Load Game");
        Button settingsBtn = new Button("Settings");
        Button exitBtn = new Button("Exit");

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

        getChildren().addAll(newGameBtn, loadGameBtn, settingsBtn, exitBtn);
    }
}