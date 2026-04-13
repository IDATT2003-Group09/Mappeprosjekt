package edu.ntnu.iir.bidata.idatt2003.group09.ui.screen;

import edu.ntnu.iir.bidata.idatt2003.group09.ui.UiSoundEffects;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SettingsScreen extends VBox {

	private static final double SLIDER_WIDTH = 300;

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

		Label masterVolumeLabel = new Label("Master volume");
		masterVolumeLabel.setStyle("-fx-font-size: 26px; -fx-text-fill: #f5f5f5;");

		Slider masterVolumeSlider = new Slider(0.0, 1.0, UiSoundEffects.getMasterVolume());
		masterVolumeSlider.setPrefWidth(SLIDER_WIDTH);
		masterVolumeSlider.setMajorTickUnit(0.25);
		masterVolumeSlider.setMinorTickCount(4);
		masterVolumeSlider.setShowTickMarks(true);
		masterVolumeSlider.setShowTickLabels(true);
		Label masterVolumeIcon = createVolumeIcon(masterVolumeSlider.getValue());
		masterVolumeSlider.valueProperty().addListener((obs, oldValue, value) ->
				{
					UiSoundEffects.setMasterVolume(value.doubleValue());
					masterVolumeIcon.setText(getVolumeIcon(value.doubleValue()));
				}
		);
		HBox masterVolumeRow = createVolumeRow(masterVolumeSlider, masterVolumeIcon);

		Label soundEffectsVolumeLabel = new Label("Sound FX volume");
		soundEffectsVolumeLabel.setStyle("-fx-font-size: 26px; -fx-text-fill: #f5f5f5;");

		Slider soundEffectsVolumeSlider = new Slider(0.0, 1.0, UiSoundEffects.getSoundEffectsVolume());
		soundEffectsVolumeSlider.setPrefWidth(SLIDER_WIDTH);
		soundEffectsVolumeSlider.setMajorTickUnit(0.25);
		soundEffectsVolumeSlider.setMinorTickCount(4);
		soundEffectsVolumeSlider.setShowTickMarks(true);
		soundEffectsVolumeSlider.setShowTickLabels(true);
		Label soundEffectsVolumeIcon = createVolumeIcon(soundEffectsVolumeSlider.getValue());
		soundEffectsVolumeSlider.valueProperty().addListener((obs, oldValue, value) ->
				{
					UiSoundEffects.setSoundEffectsVolume(value.doubleValue());
					soundEffectsVolumeIcon.setText(getVolumeIcon(value.doubleValue()));
				}
		);
		HBox soundEffectsVolumeRow = createVolumeRow(soundEffectsVolumeSlider, soundEffectsVolumeIcon);

		Label musicVolumeLabel = new Label("Music volume");
		musicVolumeLabel.setStyle("-fx-font-size: 26px; -fx-text-fill: #f5f5f5;");

		Slider musicVolumeSlider = new Slider(0.0, 1.0, UiSoundEffects.getMusicVolume());
		musicVolumeSlider.setPrefWidth(SLIDER_WIDTH);
		musicVolumeSlider.setMajorTickUnit(0.25);
		musicVolumeSlider.setMinorTickCount(4);
		musicVolumeSlider.setShowTickMarks(true);
		musicVolumeSlider.setShowTickLabels(true);
		Label musicVolumeIcon = createVolumeIcon(musicVolumeSlider.getValue());
		musicVolumeSlider.valueProperty().addListener((obs, oldValue, value) ->
				{
					UiSoundEffects.setMusicVolume(value.doubleValue());
					musicVolumeIcon.setText(getVolumeIcon(value.doubleValue()));
				}
		);
		HBox musicVolumeRow = createVolumeRow(musicVolumeSlider, musicVolumeIcon);

		Button backButton = new Button("Back");
		backButton.getStyleClass().add("start-button");
		backButton.setPrefWidth(400);
		backButton.setPrefHeight(60);
		backButton.setOnAction(e -> handler.onBack());

		UiSoundEffects.installHoverSound(backButton);
		UiSoundEffects.installClickSound(backButton);

		getChildren().addAll(
				titleLabel,
				masterVolumeLabel,
				masterVolumeRow,
				soundEffectsVolumeLabel,
				soundEffectsVolumeRow,
				musicVolumeLabel,
				musicVolumeRow,
				backButton
		);
	}

	private HBox createVolumeRow(Slider slider, Label iconLabel) {
		HBox row = new HBox(12, slider, iconLabel);
		row.setAlignment(Pos.CENTER);
		return row;
	}

	private Label createVolumeIcon(double volume) {
		Label icon = new Label(getVolumeIcon(volume));
		icon.setStyle("-fx-font-size: 28px; -fx-text-fill: #f5f5f5;");
		return icon;
	}

	private String getVolumeIcon(double volume) {
		if (volume <= 0.01) {
			return "🔇";
		}
		if (volume < 0.34) {
			return "🔈";
		}
		if (volume < 0.67) {
			return "🔉";
		}
		return "🔊";
	}
}
