package edu.ntnu.iir.bidata.idatt2003.group09.ui.screen;

import java.io.IOException;
import java.io.InputStream;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class CreateGameScree extends VBox {

	private static final String FONT_PATH = "/ThaleahFat.ttf";
	private static final double TITLE_FONT_SIZE = 32;
	private static final double BUTTON_FONT_SIZE = 26;

	public interface CreateGameHandler {
		void onCreateGame(String fileName);
		void onBack();
	}

	public CreateGameScree(CreateGameHandler handler) {
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

		getChildren().addAll(title, fileNameField, startButton, backButton);
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
