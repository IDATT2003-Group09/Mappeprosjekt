package edu.ntnu.iir.bidata.idatt2003.group09.view.elements;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Separator;
import javafx.scene.layout.GridPane;
import java.math.BigDecimal;

public abstract class TransactionOverview extends StackPane {
	public TransactionOverview(String action, String stockSymbol, BigDecimal quantity, BigDecimal price, BigDecimal commission, BigDecimal tax, BigDecimal total, Runnable onConfirm) {
		getStyleClass().addAll("trade-screen", "transaction-overview-root");
		Rectangle background = new Rectangle();
		background.setFill(Color.rgb(0, 0, 0, 0.6));
		background.widthProperty().bind(widthProperty());
		background.heightProperty().bind(heightProperty());

		VBox box = new VBox(12);
		box.setPadding(new Insets(32));
		box.getStyleClass().addAll("transaction-overview-box", "transaction-overview-content");
		box.setMaxWidth(560);
		box.setMinWidth(560);
		box.setMaxHeight(400);
		box.setMinHeight(400);
		box.setAlignment(javafx.geometry.Pos.TOP_LEFT);

		Label actionLabel = new Label(action + " " + quantity.toPlainString() + " x " + stockSymbol);
		actionLabel.getStyleClass().add("transaction-overview-action");

		GridPane breakdownGrid = createBreakdownGrid(quantity, price, commission, tax, total);

		Button confirmButton = new Button("Confirm");
		confirmButton.getStyleClass().add("transaction-overview-confirm");
		confirmButton.setOnAction(e -> onConfirm.run());
		confirmButton.setDefaultButton(true);

		Button closeButton = new Button("Cancel");
		closeButton.getStyleClass().add("transaction-overview-cancel");
		closeButton.setOnAction(e -> {
			StackPane parent = (StackPane) getParent();
			if (parent != null) parent.getChildren().remove(this);
		});

		HBox buttonBox = new HBox(10, confirmButton, closeButton);
		buttonBox.setAlignment(javafx.geometry.Pos.CENTER);
		buttonBox.getStyleClass().add("transaction-overview-buttons");

		Separator sepBelowTotal1 = new Separator();
		sepBelowTotal1.getStyleClass().add("transaction-overview-separator");
		Separator sepBelowTotal2 = new Separator();
		sepBelowTotal2.getStyleClass().add("transaction-overview-separator");

		VBox doubleLineBox = new VBox(2, sepBelowTotal1, sepBelowTotal2);
		doubleLineBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

		box.getChildren().addAll(
			actionLabel,
			breakdownGrid,
			doubleLineBox,
			buttonBox
		);

		setAlignment(box, javafx.geometry.Pos.CENTER);
		getChildren().addAll(background, box);
	}

	protected abstract GridPane createBreakdownGrid(BigDecimal quantity, BigDecimal price, BigDecimal commission, BigDecimal tax, BigDecimal total);
}
