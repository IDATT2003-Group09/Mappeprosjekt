package edu.ntnu.iir.bidata.idatt2003.group09.ui;

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

public class TransactionOverview extends StackPane {
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
		Label priceLabel = new Label("Cost: " + quantity.multiply(price));
		priceLabel.getStyleClass().add("transaction-overview-label");
		priceLabel.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
		priceLabel.setMaxWidth(Double.MAX_VALUE);

		Label plusCommission = new Label("+");
		plusCommission.getStyleClass().add("transaction-overview-sign");
		Label commissionText = new Label("COMMISSION:");
		commissionText.getStyleClass().add("transaction-overview-label-text");
		commissionText.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
		commissionText.setMaxWidth(Double.MAX_VALUE);
		Label commissionValue = new Label(commission.toString());
		commissionValue.getStyleClass().add("transaction-overview-label-value");
		commissionValue.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
		commissionValue.setMaxWidth(Double.MAX_VALUE);

		Label plusTax = new Label("+");
		plusTax.getStyleClass().add("transaction-overview-sign");
		Label taxText = new Label("TAX:");
		taxText.getStyleClass().add("transaction-overview-label-text");
		taxText.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
		taxText.setMaxWidth(Double.MAX_VALUE);
		Label taxValue = new Label(tax.toString());
		taxValue.getStyleClass().add("transaction-overview-label-value");
		taxValue.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
		taxValue.setMaxWidth(Double.MAX_VALUE);

		Label eqTotal = new Label("=");
		eqTotal.getStyleClass().add("transaction-overview-sign");
		Label totalText = new Label("TOTAL:");
		totalText.getStyleClass().add("transaction-overview-label-text");
		totalText.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
		totalText.setMaxWidth(Double.MAX_VALUE);
		Label totalValue = new Label(total.toString());
		totalValue.getStyleClass().add("transaction-overview-total");
		totalValue.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
		totalValue.setMaxWidth(Double.MAX_VALUE);

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

		VBox doubleLineBox = new VBox(2, sepBelowTotal1, sepBelowTotal2); // tight spacing for double line
		doubleLineBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

		GridPane breakdownGrid = new GridPane();
		breakdownGrid.setHgap(8);
		breakdownGrid.setVgap(4);
		breakdownGrid.setMaxWidth(Double.MAX_VALUE);
		// Cost row (no sign, just label and value)
		breakdownGrid.add(priceLabel, 2, 0);
		// Commission row
		breakdownGrid.add(plusCommission, 0, 1);
		breakdownGrid.add(commissionText, 1, 1);
		breakdownGrid.add(commissionValue, 2, 1);
		// Tax row
		breakdownGrid.add(plusTax, 0, 2);
		breakdownGrid.add(taxText, 1, 2);
		breakdownGrid.add(taxValue, 2, 2);
		// Total row
		breakdownGrid.add(eqTotal, 0, 3);
		breakdownGrid.add(totalText, 1, 3);
		breakdownGrid.add(totalValue, 2, 3);

		box.getChildren().addAll(
			actionLabel,
			breakdownGrid,
			doubleLineBox,
			buttonBox
		);

		setAlignment(box, javafx.geometry.Pos.CENTER);
		getChildren().addAll(background, box);
	}
}
