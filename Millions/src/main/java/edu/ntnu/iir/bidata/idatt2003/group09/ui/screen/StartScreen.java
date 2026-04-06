package edu.ntnu.iir.bidata.idatt2003.group09.ui.screen;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class StartScreen extends VBox {

    public interface StartHandler {
        void onNewGame();
        void onLoadGame();
        void onSettings();
    }

    public StartScreen(StartHandler handler) {

        setSpacing(15);
        setAlignment(Pos.BOTTOM_CENTER);
        setPadding(new Insets(0, 0, 90, 0));
        setStyle("""
    -fx-background-image: url('/images/Millions_background.png');
    -fx-background-size: cover;
    -fx-background-position: center;
""");

        Button newGameBtn = new Button("Nytt Spill");
        Button loadGameBtn = new Button("Last Spill");
        Button settingsBtn = new Button("Innstillinger");

        newGameBtn.setPrefWidth(400);
        newGameBtn.setPrefHeight(30);
        loadGameBtn.setPrefWidth(400);
        loadGameBtn.setPrefHeight(30);
        settingsBtn.setPrefWidth(400);
        settingsBtn.setPrefHeight(30);

        newGameBtn.setOnAction(e -> handler.onNewGame());
        loadGameBtn.setOnAction(e -> handler.onLoadGame());
        settingsBtn.setOnAction(e -> handler.onSettings());

        getChildren().addAll(newGameBtn, loadGameBtn, settingsBtn);
    }
}