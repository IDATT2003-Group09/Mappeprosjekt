package edu.ntnu.iir.bidata.idatt2003.group09.ui.screen;

import edu.ntnu.iir.bidata.idatt2003.group09.ui.UiSoundEffects;
import java.io.InputStream;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SettingsScreen extends VBox {

	private static final double SLIDER_WIDTH = 300;
	private static final double VOLUME_ICON_SIZE = 28;

	private static final String VOLUME_ICON_0_PATH = "/images/util/volume/0.png";
	private static final String VOLUME_ICON_1_PATH = "/images/util/volume/1.png";
	private static final String VOLUME_ICON_2_PATH = "/images/util/volume/2.png";
	private static final String VOLUME_ICON_3_PATH = "/images/util/volume/3.png";

	private final Image volumeIcon0;
	private final Image volumeIcon1;
	private final Image volumeIcon2;
	private final Image volumeIcon3;

	public interface SettingsHandler {
		void onBack();
	}

	public SettingsScreen(SettingsHandler handler) {
		this.volumeIcon0 = loadVolumeIcon(VOLUME_ICON_0_PATH);
		this.volumeIcon1 = loadVolumeIcon(VOLUME_ICON_1_PATH);
		this.volumeIcon2 = loadVolumeIcon(VOLUME_ICON_2_PATH);
		this.volumeIcon3 = loadVolumeIcon(VOLUME_ICON_3_PATH);

		getStylesheets().add(getClass().getResource("/styling/startscreen.css").toExternalForm());
		getStylesheets().add(getClass().getResource("/styling/settingsscreen.css").toExternalForm());
		getStyleClass().add("settings-screen");

		setSpacing(0);
		setAlignment(Pos.CENTER);
		setPadding(new Insets(40));

		Label titleLabel = new Label("Settings");
		titleLabel.getStyleClass().add("settings-title");

		Label masterVolumeLabel = new Label("Master volume");
		masterVolumeLabel.getStyleClass().add("settings-label");

		Slider masterVolumeSlider = new Slider(0.0, 1.0, UiSoundEffects.getMasterVolume());
		masterVolumeSlider.setPrefWidth(SLIDER_WIDTH);
		masterVolumeSlider.setMajorTickUnit(0.25);
		masterVolumeSlider.setMinorTickCount(4);
		masterVolumeSlider.setShowTickMarks(true);
		masterVolumeSlider.setShowTickLabels(true);
		ImageView masterVolumeIcon = createVolumeIcon(masterVolumeSlider.getValue());
		masterVolumeSlider.valueProperty().addListener((obs, oldValue, value) ->
				{
					UiSoundEffects.setMasterVolume(value.doubleValue());
					updateVolumeIcon(masterVolumeIcon, value.doubleValue());
				}
		);
		HBox masterVolumeRow = createVolumeRow(masterVolumeSlider, masterVolumeIcon);

		Label soundEffectsVolumeLabel = new Label("Sound FX volume");
		soundEffectsVolumeLabel.getStyleClass().add("settings-label");

		Slider soundEffectsVolumeSlider = new Slider(0.0, 1.0, UiSoundEffects.getSoundEffectsVolume());
		soundEffectsVolumeSlider.setPrefWidth(SLIDER_WIDTH);
		soundEffectsVolumeSlider.setMajorTickUnit(0.25);
		soundEffectsVolumeSlider.setMinorTickCount(4);
		soundEffectsVolumeSlider.setShowTickMarks(true);
		soundEffectsVolumeSlider.setShowTickLabels(true);
		ImageView soundEffectsVolumeIcon = createVolumeIcon(soundEffectsVolumeSlider.getValue());
		soundEffectsVolumeSlider.valueProperty().addListener((obs, oldValue, value) ->
				{
					UiSoundEffects.setSoundEffectsVolume(value.doubleValue());
					updateVolumeIcon(soundEffectsVolumeIcon, value.doubleValue());
				}
		);
		HBox soundEffectsVolumeRow = createVolumeRow(soundEffectsVolumeSlider, soundEffectsVolumeIcon);

		Label musicVolumeLabel = new Label("Music volume");
		musicVolumeLabel.getStyleClass().add("settings-label");

		Slider musicVolumeSlider = new Slider(0.0, 1.0, UiSoundEffects.getMusicVolume());
		musicVolumeSlider.setPrefWidth(SLIDER_WIDTH);
		musicVolumeSlider.setMajorTickUnit(0.25);
		musicVolumeSlider.setMinorTickCount(4);
		musicVolumeSlider.setShowTickMarks(true);
		musicVolumeSlider.setShowTickLabels(true);
		ImageView musicVolumeIcon = createVolumeIcon(musicVolumeSlider.getValue());
		musicVolumeSlider.valueProperty().addListener((obs, oldValue, value) ->
				{
					UiSoundEffects.setMusicVolume(value.doubleValue());
					updateVolumeIcon(musicVolumeIcon, value.doubleValue());
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

		VBox settingsCard = new VBox(16);
		settingsCard.getStyleClass().add("settings-card");
		settingsCard.setAlignment(Pos.CENTER);
		settingsCard.setPadding(new Insets(28));
		settingsCard.setMaxWidth(560);

		settingsCard.getChildren().addAll(
				titleLabel,
				masterVolumeLabel,
				masterVolumeRow,
				soundEffectsVolumeLabel,
				soundEffectsVolumeRow,
				musicVolumeLabel,
				musicVolumeRow,
				backButton
		);

		getChildren().add(settingsCard);
	}

	private HBox createVolumeRow(Slider slider, ImageView iconView) {
		HBox row = new HBox(12, slider, iconView);
		row.setAlignment(Pos.CENTER);
		return row;
	}

	private ImageView createVolumeIcon(double volume) {
		ImageView iconView = new ImageView();
		iconView.setFitWidth(VOLUME_ICON_SIZE);
		iconView.setFitHeight(VOLUME_ICON_SIZE);
		iconView.setPreserveRatio(true);
		iconView.setSmooth(false);
		updateVolumeIcon(iconView, volume);
		return iconView;
	}

	private void updateVolumeIcon(ImageView iconView, double volume) {
		iconView.setImage(getVolumeIconImage(volume));
	}

	private Image getVolumeIconImage(double volume) {
		if (volume <= 0.01) {
			return volumeIcon0;
		}
		if (volume < 0.34) {
			return volumeIcon1;
		}
		if (volume < 0.67) {
			return volumeIcon2;
		}
		return volumeIcon3;
	}

	private Image loadVolumeIcon(String path) {
		try (InputStream stream = getClass().getResourceAsStream(path)) {
			if (stream == null) {
				return null;
			}
			return new Image(stream, VOLUME_ICON_SIZE, VOLUME_ICON_SIZE, true, false);
		} catch (Exception e) {
			return null;
		}
	}
}
