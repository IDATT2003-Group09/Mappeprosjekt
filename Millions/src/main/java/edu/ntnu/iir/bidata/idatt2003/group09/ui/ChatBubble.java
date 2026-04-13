package edu.ntnu.iir.bidata.idatt2003.group09.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class ChatBubble extends VBox {

	private final Label textLabel;

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

		StackPane bubbleBody = new StackPane(textLabel);
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
	}

	public String getText() {
		return textLabel.getText();
	}
}
