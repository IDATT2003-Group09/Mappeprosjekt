package edu.ntnu.iir.bidata.idatt2003.group09.view.screen;

import edu.ntnu.iir.bidata.idatt2003.group09.view.sound.UiSoundEffects;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import edu.ntnu.iir.bidata.idatt2003.group09.view.elements.Boss;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.layout.StackPane;


/**
 * Startskjerm for applikasjonen med hovedmenyknapper (New, Load, Settings, Exit).
 * Viser en dekorativ bakgrunn og en `Boss`-tegnfigur som gir meldinger til brukeren.
 */
public class StartScreen extends StackPane {

    private Boss boss;
    public interface StartHandler {
        void onNewGame();
        void onLoadGame();
        void onSettings();
    }

    public StartScreen(StartHandler handler, int saveFileCount) {
        getStylesheets().add(getClass().getResource("/styling/startscreen.css").toExternalForm());
        String imageUrl = getClass().getResource("/images/Millions_background.png").toExternalForm();
        setStyle("""
            -fx-background-image: url('%s');
            -fx-background-size: cover;
            -fx-background-position: center;
        """.formatted(imageUrl));

        VBox buttonBox = new VBox(15);
        buttonBox.setAlignment(Pos.BOTTOM_CENTER);
        buttonBox.setPadding(new Insets(0, 0, 90, 0));

        Button newGameBtn = new Button("New Game");
        Button loadGameBtn = new Button("Load Game");
        Button settingsBtn = new Button("Settings");
        Button exitBtn = new Button("Exit");

        if (saveFileCount == 0){
            boss = new Boss("What?! All our employess quit?", 500);
        }
        else if (saveFileCount == 1){
            boss = new Boss("Where is everyone? One might think we only have one employee", 500);
        }
        else if (saveFileCount == 2){
            boss = new Boss("Do you want to earn me some money kid? Welcome aboard!", 500);
        }
        else if (saveFileCount == 3){
            boss = new Boss("Business is booming. So many talented traders!",500);
        }
        else {
            boss = new Boss("We are not hiring. Delete some savefiles to make room",500);
            newGameBtn.setDisable(true);
        }

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
        StackPane.setMargin(boss, new Insets(120, 0, 0, -70));

        // Play hair.gif every 10 seconds
        Timeline hairTimeline = new Timeline(
            new KeyFrame(Duration.seconds(10), e -> boss.playHairAnimation())
        );
        hairTimeline.setCycleCount(Timeline.INDEFINITE);
        hairTimeline.play();
    }

    private void syncMouseFocus(Button button) {
        button.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> button.requestFocus());
    }
}