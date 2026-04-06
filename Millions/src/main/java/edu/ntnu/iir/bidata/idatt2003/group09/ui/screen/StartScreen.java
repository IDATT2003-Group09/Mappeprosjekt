package edu.ntnu.iir.bidata.idatt2003.group09.ui.screen;

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

        setSpacing(20);
        setAlignment(Pos.CENTER);
        setStyle("-fx-background-color: Grey;");

        Button newGameBtn = new Button("Nytt Spill");
        Button loadGameBtn = new Button("Last Spill");
        Button settingsBtn = new Button("Innstillinger");

        newGameBtn.setPrefWidth(400);
        loadGameBtn.setPrefWidth(400);
        settingsBtn.setPrefWidth(400);

        newGameBtn.setOnAction(e -> handler.onNewGame());
        loadGameBtn.setOnAction(e -> handler.onLoadGame());
        settingsBtn.setOnAction(e -> handler.onSettings());

        getChildren().addAll(newGameBtn, loadGameBtn, settingsBtn);
    }
}