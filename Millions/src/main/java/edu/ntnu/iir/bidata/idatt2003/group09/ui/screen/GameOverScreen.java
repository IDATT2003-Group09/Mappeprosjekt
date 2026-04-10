package edu.ntnu.iir.bidata.idatt2003.group09.ui.screen;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class GameOverScreen extends VBox {

    public GameOverScreen(Runnable onBack) {

        Label title = new Label("Game Over");
        title.setStyle("-fx-font-size: 24px; -fx-text-fill: red;");

        Button back = new Button("Back to menu");
        back.setOnAction(e -> onBack.run());

        setSpacing(20);
        setAlignment(Pos.CENTER);
        getChildren().addAll(title, back);
    }
}