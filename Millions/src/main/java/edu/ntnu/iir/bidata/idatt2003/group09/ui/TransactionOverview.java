package edu.ntnu.iir.bidata.idatt2003.group09.ui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.math.BigDecimal;

public class TransactionOverview extends StackPane {
	public TransactionOverview(String action, String stockSymbol, BigDecimal quantity, BigDecimal price, BigDecimal commission, BigDecimal tax, BigDecimal total, Runnable onClose) {
		Rectangle background = new Rectangle();
		background.setFill(Color.rgb(0, 0, 0, 0.6));
		background.widthProperty().bind(widthProperty());
		background.heightProperty().bind(heightProperty());

		VBox box = new VBox(12);
		box.setPadding(new Insets(32));
		box.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, #222, 16, 0.2, 0, 4);");
		box.setMaxWidth(340);
		box.setMinWidth(260);
		box.setAlignment(javafx.geometry.Pos.CENTER);

		Label actionLabel = new Label(action + " " + quantity.toPlainString() + " x " + stockSymbol);
		actionLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
		Label priceLabel = new Label("Price per share: " + price);
		Label commissionLabel = new Label("Commission (fee): " + commission);
		Label taxLabel = new Label("Tax: " + tax);
		Label totalLabel = new Label("Total: " + total);
		totalLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

		Button closeButton = new Button("Close");
		closeButton.setOnAction(e -> onClose.run());
		closeButton.setDefaultButton(true);

		box.getChildren().addAll(actionLabel, priceLabel, commissionLabel, taxLabel, totalLabel, closeButton);

		setAlignment(box, javafx.geometry.Pos.CENTER);
		getChildren().addAll(background, box);
	}
}
