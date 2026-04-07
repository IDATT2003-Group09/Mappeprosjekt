package edu.ntnu.iir.bidata.idatt2003.group09.ui.screen;

import java.io.IOException;
import java.io.InputStream;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class StartScreen extends VBox {

    private static final String START_SCREEN_FONT_PATH = "/ThaleahFat.ttf";
    private static final double START_SCREEN_FONT_SIZE = 38;

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
        Button exitBtn = new Button("Avslutt");

        String fontFamily = loadStartScreenFontFamily();
        Font startScreenFont = Font.font(fontFamily, START_SCREEN_FONT_SIZE);
        newGameBtn.setFont(startScreenFont);
        loadGameBtn.setFont(startScreenFont);
        settingsBtn.setFont(startScreenFont);
        exitBtn.setFont(startScreenFont);

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

    private String loadStartScreenFontFamily() {
        try (InputStream fontStream = getClass().getResourceAsStream(START_SCREEN_FONT_PATH)) {
            if (fontStream == null) {
                return Font.getDefault().getFamily();
            }

            Font loadedFont = Font.loadFont(fontStream, START_SCREEN_FONT_SIZE);
            if (loadedFont != null) {
                return loadedFont.getFamily();
            }
        } catch (IOException e) {
            return Font.getDefault().getFamily();
        }

        return Font.getDefault().getFamily();
    }
}