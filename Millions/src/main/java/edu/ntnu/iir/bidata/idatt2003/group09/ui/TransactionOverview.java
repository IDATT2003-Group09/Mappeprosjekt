package edu.ntnu.iir.bidata.idatt2003.group09.ui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.math.BigDecimal;

public class TransactionOverview extends Stage {
	public TransactionOverview(String action, String stockSymbol, BigDecimal quantity, BigDecimal price, BigDecimal commission, BigDecimal tax, BigDecimal total) {
		setTitle("Transaction Overview");
		initModality(Modality.APPLICATION_MODAL);
		setResizable(false);

		VBox root = new VBox(12);
		root.setPadding(new Insets(20));

		Label actionLabel = new Label(action + " " + quantity.toPlainString() + " x " + stockSymbol);
		actionLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
		Label priceLabel = new Label("Price per share: " + price);
		Label commissionLabel = new Label("Commission (fee): " + commission);
		Label taxLabel = new Label("Tax: " + tax);
		Label totalLabel = new Label("Total: " + total);
		totalLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

		Button closeButton = new Button("Close");
		closeButton.setOnAction(e -> close());
		closeButton.setDefaultButton(true);

		root.getChildren().addAll(actionLabel, priceLabel, commissionLabel, taxLabel, totalLabel, closeButton);
		Scene scene = new Scene(root);
		setScene(scene);
	}
}
