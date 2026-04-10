package edu.ntnu.iir.bidata.idatt2003.group09.ui.screen;

import java.io.IOException;
import java.io.InputStream;
import edu.ntnu.iir.bidata.idatt2003.group09.ui.ChatBubble;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class CreateGameScreen extends StackPane {

	private static final String FONT_PATH = "/ThaleahFat.ttf";
	private static final String BOSS_GIF_PATH = "/images/boss/boss.gif";
	private static final double BOSS_SIZE = 500;
	private static final double TITLE_FONT_SIZE = 32;
	private static final double BUTTON_FONT_SIZE = 26;

	public interface CreateGameHandler {
		void onCreateGame(String fileName);
		void onBack();
	}

	public CreateGameScreen(CreateGameHandler handler) {
		getStylesheets().add(getClass().getResource("/styling/startscreen.css").toExternalForm());

		setPadding(new Insets(40));
		setStyle("""
		-fx-background-image: url('/images/Millions_background.png');
		-fx-background-size: cover;
		-fx-background-position: center;
""");

		String fontFamily = loadFontFamily();

		VBox contentBox = new VBox(16);
		contentBox.setAlignment(Pos.CENTER);
		contentBox.setFillWidth(false);

		Label title = new Label("Opprett nytt spill");
		title.setStyle("-fx-text-fill: white;");
		title.setFont(Font.font(fontFamily, TITLE_FONT_SIZE));

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

		contentBox.getChildren().addAll(title, fileNameField, startButton, backButton);
		getChildren().add(contentBox);
		StackPane.setAlignment(contentBox, Pos.CENTER);

		ImageView bossImageView = createBossImageView();
		if (bossImageView != null) {
			getChildren().add(bossImageView);
			StackPane.setAlignment(bossImageView, Pos.BOTTOM_LEFT);
			StackPane.setMargin(bossImageView, new Insets(0, 0, -90, -70));

			ChatBubble bossBubble = new ChatBubble("Velg et filnavn for å starte!", fontFamily);
			getChildren().add(bossBubble);
			StackPane.setAlignment(bossBubble, Pos.BOTTOM_LEFT);
			StackPane.setMargin(bossBubble, new Insets(0, 0, 250, 250));
		}
	}

	private ImageView createBossImageView() {
		InputStream gifStream = getClass().getResourceAsStream(BOSS_GIF_PATH);
		if (gifStream == null) {
			return null;
		}

		Image bossImage = new Image(gifStream, BOSS_SIZE, BOSS_SIZE, true, false);
		ImageView bossImageView = new ImageView(bossImage);
		bossImageView.setPreserveRatio(true);
		bossImageView.setSmooth(false);
		bossImageView.setCache(false);
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
