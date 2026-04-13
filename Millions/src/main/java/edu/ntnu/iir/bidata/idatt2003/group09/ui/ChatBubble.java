package edu.ntnu.iir.bidata.idatt2003.group09.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class ChatBubble extends VBox {

	private final Label textLabel;
	private final StackPane bubbleBody;

	public ChatBubble(String text, String fontFamily) {
		getStyleClass().add("chat-bubble");
		setAlignment(Pos.TOP_LEFT);
		setPickOnBounds(false);

		textLabel = new Label(text);
		textLabel.getStyleClass().add("chat-bubble-label");
		textLabel.setWrapText(true);
		textLabel.setMaxWidth(320);
		textLabel.setMinHeight(Region.USE_PREF_SIZE);
		textLabel.setPrefHeight(Region.USE_COMPUTED_SIZE);
		textLabel.setTextFill(javafx.scene.paint.Color.web("#111111"));
		textLabel.setFont(Font.font(fontFamily, 22));

		bubbleBody = new StackPane(textLabel);
		bubbleBody.setPadding(new Insets(14, 20, 14, 20));
		bubbleBody.setMinWidth(280);
		bubbleBody.setPrefWidth(360);
		bubbleBody.setMaxWidth(360);
		bubbleBody.setMinHeight(Region.USE_PREF_SIZE);
		bubbleBody.setPrefHeight(Region.USE_COMPUTED_SIZE);
		bubbleBody.setStyle("""
			-fx-background-color: #f5f5f5;
			-fx-border-color: #111111;
			-fx-border-width: 3px;
			-fx-background-radius: 0;
			-fx-border-radius: 0;
		""");

		getChildren().addAll(bubbleBody);
	}

	public void setText(String text) {
		textLabel.setText(text);
		showTextOnly();
	}

	public String getText() {
		return textLabel.getText();
	}

	public void addContinueButton(Runnable onContinue) {
		VBox content = new VBox(8);
		content.setAlignment(Pos.TOP_LEFT);
		content.setMouseTransparent(false);

		content.getChildren().add(textLabel);

		Button continueButton = new Button("Continue →");
		String buttonFontFamily = textLabel.getFont() != null
			? textLabel.getFont().getFamily()
			: "System";
		continueButton.setStyle("""
			-fx-background-color: transparent;
			-fx-text-fill: #111111;
			-fx-font-family: '%s';
			-fx-font-size: 18;
			-fx-padding: 2 0 0 0;
			-fx-border-color: transparent;
			-fx-border-width: 0;
			-fx-border-radius: 0;
			-fx-background-radius: 0;
			-fx-cursor: hand;
		""".formatted(buttonFontFamily));
		continueButton.setOnAction(e -> onContinue.run());
		continueButton.setMouseTransparent(false);

		content.getChildren().add(continueButton);

		bubbleBody.getChildren().clear();
		bubbleBody.getChildren().add(content);
		bubbleBody.setMouseTransparent(false);
	}

	private void showTextOnly() {
		bubbleBody.getChildren().clear();
		bubbleBody.getChildren().add(textLabel);
	}
}
