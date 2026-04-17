package edu.ntnu.iir.bidata.idatt2003.group09.view.elements;

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

	public ChatBubble(String text) {
    this.getStylesheets().add(getClass().getResource("/styling/chatbubble.css").toExternalForm());
		getStyleClass().add("chat-bubble");
		setAlignment(Pos.TOP_LEFT);
		setPickOnBounds(false);

		textLabel = new Label(text);
		textLabel.getStyleClass().add("chat-bubble-label");
		textLabel.setWrapText(true);
		textLabel.setMaxWidth(320);
		textLabel.setMinHeight(100);
		textLabel.setPrefHeight(700);

		bubbleBody = new StackPane(textLabel);
		bubbleBody.getStyleClass().add("chat-bubble-body");

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
		continueButton.getStyleClass().add("chat-bubble-continue-button");
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
