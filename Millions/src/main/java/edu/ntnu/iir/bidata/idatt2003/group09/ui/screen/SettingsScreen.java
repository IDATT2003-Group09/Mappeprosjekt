package edu.ntnu.iir.bidata.idatt2003.group09.ui.screen;

import edu.ntnu.iir.bidata.idatt2003.group09.ui.UiSoundEffects;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class SettingsScreen extends VBox {

	public interface SettingsHandler {
		void onBack();
	}

	public SettingsScreen(SettingsHandler handler) {
		getStylesheets().add(getClass().getResource("/styling/startscreen.css").toExternalForm());

		setSpacing(20);
		setAlignment(Pos.CENTER);
		setPadding(new Insets(40));
		setStyle("""
		-fx-background-image: url('/images/Millions_background.png');
		-fx-background-size: cover;
		-fx-background-position: center;
""");

		Label titleLabel = new Label("Settings");
		titleLabel.setStyle("-fx-font-size: 52px; -fx-text-fill: #f5f5f5;");

		CheckBox backgroundMusicToggle = new CheckBox("Background music");
		backgroundMusicToggle.setSelected(UiSoundEffects.isBackgroundMusicEnabled());
		backgroundMusicToggle.setStyle("-fx-font-size: 26px; -fx-text-fill: #f5f5f5;");
		backgroundMusicToggle.selectedProperty().addListener((obs, oldValue, enabled) ->
				UiSoundEffects.setBackgroundMusicEnabled(enabled)
		);

		Button backButton = new Button("Back");
		backButton.getStyleClass().add("start-button");
		backButton.setPrefWidth(400);
		backButton.setPrefHeight(60);
		backButton.setOnAction(e -> handler.onBack());

		UiSoundEffects.installHoverSound(backButton);
		UiSoundEffects.installClickSound(backButton);

		getChildren().addAll(titleLabel, backgroundMusicToggle, backButton);
	}
}
