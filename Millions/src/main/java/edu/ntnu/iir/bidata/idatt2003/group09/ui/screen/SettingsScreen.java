package edu.ntnu.iir.bidata.idatt2003.group09.ui.screen;

import edu.ntnu.iir.bidata.idatt2003.group09.ui.UiSoundEffects;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
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

		CheckBox soundEffectsToggle = new CheckBox("Sound effects");
		soundEffectsToggle.setSelected(UiSoundEffects.isSoundEffectsEnabled());
		soundEffectsToggle.setStyle("-fx-font-size: 26px; -fx-text-fill: #f5f5f5;");
		soundEffectsToggle.selectedProperty().addListener((obs, oldValue, enabled) ->
				UiSoundEffects.setSoundEffectsEnabled(enabled)
		);

		Label masterVolumeLabel = new Label("Master volume");
		masterVolumeLabel.setStyle("-fx-font-size: 26px; -fx-text-fill: #f5f5f5;");

		Slider masterVolumeSlider = new Slider(0.0, 1.0, UiSoundEffects.getMasterVolume());
		masterVolumeSlider.setPrefWidth(420);
		masterVolumeSlider.setMajorTickUnit(0.25);
		masterVolumeSlider.setMinorTickCount(4);
		masterVolumeSlider.setShowTickMarks(true);
		masterVolumeSlider.setShowTickLabels(true);
		masterVolumeSlider.valueProperty().addListener((obs, oldValue, value) ->
				UiSoundEffects.setMasterVolume(value.doubleValue())
		);

		Label soundEffectsVolumeLabel = new Label("Sound FX volume");
		soundEffectsVolumeLabel.setStyle("-fx-font-size: 26px; -fx-text-fill: #f5f5f5;");

		Slider soundEffectsVolumeSlider = new Slider(0.0, 1.0, UiSoundEffects.getSoundEffectsVolume());
		soundEffectsVolumeSlider.setPrefWidth(420);
		soundEffectsVolumeSlider.setMajorTickUnit(0.25);
		soundEffectsVolumeSlider.setMinorTickCount(4);
		soundEffectsVolumeSlider.setShowTickMarks(true);
		soundEffectsVolumeSlider.setShowTickLabels(true);
		soundEffectsVolumeSlider.valueProperty().addListener((obs, oldValue, value) ->
				UiSoundEffects.setSoundEffectsVolume(value.doubleValue())
		);

		Label musicVolumeLabel = new Label("Music volume");
		musicVolumeLabel.setStyle("-fx-font-size: 26px; -fx-text-fill: #f5f5f5;");

		Slider musicVolumeSlider = new Slider(0.0, 1.0, UiSoundEffects.getMusicVolume());
		musicVolumeSlider.setPrefWidth(420);
		musicVolumeSlider.setMajorTickUnit(0.25);
		musicVolumeSlider.setMinorTickCount(4);
		musicVolumeSlider.setShowTickMarks(true);
		musicVolumeSlider.setShowTickLabels(true);
		musicVolumeSlider.valueProperty().addListener((obs, oldValue, value) ->
				UiSoundEffects.setMusicVolume(value.doubleValue())
		);

		Button backButton = new Button("Back");
		backButton.getStyleClass().add("start-button");
		backButton.setPrefWidth(400);
		backButton.setPrefHeight(60);
		backButton.setOnAction(e -> handler.onBack());

		UiSoundEffects.installHoverSound(backButton);
		UiSoundEffects.installClickSound(backButton);

		getChildren().addAll(
				titleLabel,
				backgroundMusicToggle,
				soundEffectsToggle,
				masterVolumeLabel,
				masterVolumeSlider,
				soundEffectsVolumeLabel,
				soundEffectsVolumeSlider,
				musicVolumeLabel,
				musicVolumeSlider,
				backButton
		);
	}
}
