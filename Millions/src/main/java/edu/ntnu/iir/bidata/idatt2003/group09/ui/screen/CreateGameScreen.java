package edu.ntnu.iir.bidata.idatt2003.group09.ui.screen;

import java.io.IOException;
import java.io.InputStream;
import edu.ntnu.iir.bidata.idatt2003.group09.ui.ChatBubble;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class CreateGameScreen extends StackPane {

	private static final String FONT_PATH = "/ThaleahFat.ttf";
	private static final String BOSS_GIF_PATH = "/images/boss/boss.gif";
	private static final String EXIT_RED_PATH = "/images/util/exit/pixilart-frames/exitred.png";
	private static final String EXIT_GREEN_PATH = "/images/util/exit/pixilart-frames/exitgreen.png";
	private static final double BOSS_SIZE = 500;
	private static final double TITLE_FONT_SIZE = 32;
	private static final double BUTTON_FONT_SIZE = 26;
	private static final double EXIT_BUTTON_SIZE = 50;

	public interface CreateGameHandler {
		void onCreateGame(String fileName);
		void onBack();
	}

	public CreateGameScreen(CreateGameHandler handler) {
		getStylesheets().add(getClass().getResource("/styling/startscreen.css").toExternalForm());

		setPadding(new Insets(40));
		setStyle("""
		-fx-background-image: url('/images/stockfloor.png');
		-fx-background-size: cover;
		-fx-background-position: center;
""");

		String fontFamily = loadFontFamily();

		VBox contentBox = new VBox(16);
		contentBox.setAlignment(Pos.CENTER);
		contentBox.setFillWidth(false);

		TextField fileNameField = new TextField();
		fileNameField.setPromptText("");
		fileNameField.setMaxWidth(250);
		fileNameField.setPrefHeight(38);
		fileNameField.setFont(Font.font(fontFamily, 22));
		fileNameField.setAlignment(Pos.CENTER);
		fileNameField.setStyle("""
			-fx-background-color: transparent;
			-fx-background-insets: 0;
			-fx-background-radius: 0;
			-fx-border-color: transparent;
			-fx-border-width: 0;
			-fx-text-fill: #111111;
			-fx-highlight-fill: #ffd447;
			-fx-highlight-text-fill: #111111;
		""");

		StackPane inputBubble = new StackPane(fileNameField);
		inputBubble.setPadding(new Insets(12, 16, 12, 16));
		inputBubble.setMinWidth(300);
		inputBubble.setMaxWidth(380);
		inputBubble.setStyle("""
			-fx-background-color: #f5f5f5;
			-fx-border-color: #111111;
			-fx-border-width: 3px;
			-fx-background-radius: 0;
			-fx-border-radius: 0;
		""");

		Button startButton = new Button("Confirm");
		startButton.getStyleClass().add("start-button");
		startButton.setFont(Font.font(fontFamily, BUTTON_FONT_SIZE));
		startButton.setPrefWidth(380);
		startButton.setPrefHeight(55);
		startButton.setOnAction(e -> handler.onCreateGame(fileNameField.getText()));
		fileNameField.setOnAction(e -> handler.onCreateGame(fileNameField.getText()));

		ImageView exitRedImage = createExitImageView(EXIT_RED_PATH);
		ImageView exitGreenImage = createExitImageView(EXIT_GREEN_PATH);
		exitGreenImage.setVisible(false);

		StackPane backButton = new StackPane(exitRedImage, exitGreenImage);
		backButton.setPickOnBounds(false);
		backButton.setMinSize(EXIT_BUTTON_SIZE, EXIT_BUTTON_SIZE);
		backButton.setPrefSize(EXIT_BUTTON_SIZE, EXIT_BUTTON_SIZE);
		backButton.setMaxSize(EXIT_BUTTON_SIZE, EXIT_BUTTON_SIZE);
		backButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
		backButton.setOnMouseEntered(e -> {
			exitRedImage.setVisible(false);
			exitGreenImage.setVisible(true);
		});
		backButton.setOnMouseExited(e -> {
			exitGreenImage.setVisible(false);
			exitRedImage.setVisible(true);
		});
		backButton.setOnMouseClicked(e -> handler.onBack());

		contentBox.getChildren().addAll(inputBubble, startButton);
		getChildren().add(contentBox);
		StackPane.setAlignment(contentBox, Pos.CENTER);
		StackPane.setMargin(contentBox, new Insets(90, 0, 0, 0));

		getChildren().add(backButton);
		StackPane.setAlignment(backButton, Pos.TOP_LEFT);
		StackPane.setMargin(backButton, new Insets(20, 0, 0, 20));

		ImageView bossImageView = createBossImageView();
		if (bossImageView != null) {
			getChildren().add(bossImageView);
			StackPane.setAlignment(bossImageView, Pos.BOTTOM_LEFT);
			StackPane.setMargin(bossImageView, new Insets(0, 0, -90, -70));

			ChatBubble bossBubble = new ChatBubble("Hey you! What's your name?", fontFamily);
			getChildren().add(bossBubble);
			StackPane.setAlignment(bossBubble, Pos.TOP_CENTER);
			StackPane.setMargin(bossBubble, new Insets(220, 0, 0, 0));

			fileNameField.requestFocus();
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

	private ImageView createExitImageView(String path) {
		InputStream imageStream = getClass().getResourceAsStream(path);
		if (imageStream == null) {
			return new ImageView();
		}

		Image image = new Image(imageStream, EXIT_BUTTON_SIZE, EXIT_BUTTON_SIZE, true, false);
		ImageView imageView = new ImageView(image);
		imageView.setPreserveRatio(true);
		imageView.setSmooth(false);
		imageView.setCache(false);
		return imageView;
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
