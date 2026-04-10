package edu.ntnu.iir.bidata.idatt2003.group09.ui.screen;

import java.io.IOException;
import java.io.InputStream;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class CreateGameScreen extends VBox {

	private static final String FONT_PATH = "/ThaleahFat.ttf";
	private static final String BOSS_GIF_PATH = "/images/boss/boss.gif";
	private static final double TITLE_FONT_SIZE = 32;
	private static final double BUTTON_FONT_SIZE = 26;

	public interface CreateGameHandler {
		void onCreateGame(String fileName);
		void onBack();
	}

	public CreateGameScreen(CreateGameHandler handler) {
		getStylesheets().add(getClass().getResource("/styling/startscreen.css").toExternalForm());

		setSpacing(16);
		setAlignment(Pos.CENTER);
		setPadding(new Insets(40));
		setStyle("""
		-fx-background-image: url('/images/Millions_background.png');
		-fx-background-size: cover;
		-fx-background-position: center;
""");

		String fontFamily = loadFontFamily();

		Label title = new Label("Opprett nytt spill");
		title.setStyle("-fx-text-fill: white;");
		title.setFont(Font.font(fontFamily, TITLE_FONT_SIZE));

		ImageView bossImageView = createBossImageView();

		TextField fileNameField = new TextField();
		fileNameField.setPromptText("Filnavn (f.eks. slot1)");
		fileNameField.setMaxWidth(450);
		fileNameField.setPrefHeight(50);

		Button startButton = new Button("Start spill");
		startButton.getStyleClass().add("start-button");
		startButton.setFont(Font.font(fontFamily, BUTTON_FONT_SIZE));
		startButton.setPrefWidth(450);
		startButton.setPrefHeight(55);
		startButton.setOnAction(e -> handler.onCreateGame(fileNameField.getText()));

		Button backButton = new Button("Tilbake");
		backButton.getStyleClass().add("start-button");
		backButton.setFont(Font.font(fontFamily, BUTTON_FONT_SIZE));
		backButton.setPrefWidth(450);
		backButton.setPrefHeight(55);
		backButton.setOnAction(e -> handler.onBack());

		if (bossImageView != null) {
			getChildren().addAll(title, bossImageView, fileNameField, startButton, backButton);
		} else {
			getChildren().addAll(title, fileNameField, startButton, backButton);
		}
	}

	private ImageView createBossImageView() {
		InputStream gifStream = getClass().getResourceAsStream(BOSS_GIF_PATH);
		if (gifStream == null) {
			return null;
		}

		Image bossImage = new Image(gifStream);
		ImageView bossImageView = new ImageView(bossImage);
		bossImageView.setPreserveRatio(true);
		bossImageView.setFitHeight(180);
		bossImageView.setSmooth(true);
		return bossImageView;
	}

	private String loadFontFamily() {
		try (InputStream fontStream = getClass().getResourceAsStream(FONT_PATH)) {
			if (fontStream == null) {
				return Font.getDefault().getFamily();
			}

			Font loadedFont = Font.loadFont(fontStream, TITLE_FONT_SIZE);
			if (loadedFont != null) {
				return loadedFont.getFamily();
			}
		} catch (IOException e) {
			return Font.getDefault().getFamily();
		}

		return Font.getDefault().getFamily();
	}
}
