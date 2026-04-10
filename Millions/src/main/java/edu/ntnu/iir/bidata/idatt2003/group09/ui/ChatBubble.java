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
		setAlignment(Pos.TOP_LEFT);
		setPickOnBounds(false);

		textLabel = new Label(text);
		textLabel.setWrapText(true);
		textLabel.setTextFill(javafx.scene.paint.Color.web("#111111"));
		textLabel.setFont(Font.font(fontFamily, 20));

		StackPane bubbleBody = new StackPane(textLabel);
		bubbleBody.setPadding(new Insets(12, 16, 12, 16));
		bubbleBody.setMinWidth(220);
		bubbleBody.setMaxWidth(280);
		bubbleBody.setStyle("""
			-fx-background-color: #f5f5f5;
			-fx-border-color: #111111;
			-fx-border-width: 3px;
			-fx-background-radius: 0;
			-fx-border-radius: 0;
		""");

		Region pointer = new Region();
		pointer.setPrefSize(18, 18);
		pointer.setMinSize(18, 18);
		pointer.setMaxSize(18, 18);
		pointer.setRotate(45);
		pointer.setStyle("""
			-fx-background-color: #f5f5f5;
			-fx-border-color: #111111;
			-fx-border-width: 3px;
		""");

		StackPane pointerWrap = new StackPane(pointer);
		pointerWrap.setAlignment(Pos.CENTER_LEFT);
		pointerWrap.setPadding(new Insets(-6, 0, 0, 22));

		getChildren().addAll(bubbleBody, pointerWrap);
	}

	public void setText(String text) {
		textLabel.setText(text);
	}

	public String getText() {
		return textLabel.getText();
	}
}
