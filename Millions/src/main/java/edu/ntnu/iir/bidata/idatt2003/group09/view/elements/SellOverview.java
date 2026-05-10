package edu.ntnu.iir.bidata.idatt2003.group09.view.elements;

import javafx.scene.layout.GridPane;
import java.math.BigDecimal;

public class SellOverview extends TransactionOverview {
    public SellOverview(String stockSymbol, BigDecimal quantity, BigDecimal price, BigDecimal commission, BigDecimal tax, BigDecimal total, Runnable onConfirm) {
        super("Sell", stockSymbol, quantity, price, commission, tax, total, onConfirm);
    }

    @Override
    protected GridPane createBreakdownGrid(BigDecimal quantity, BigDecimal price, BigDecimal commission, BigDecimal tax, BigDecimal total) {
        GridPane breakdownGrid = new GridPane();
        breakdownGrid.setHgap(8);
        breakdownGrid.setVgap(4);
        breakdownGrid.setMaxWidth(Double.MAX_VALUE);

        // Sell: PROCEEDS - COMMISSION - TAX = TOTAL
        javafx.scene.control.Label emptySign = new javafx.scene.control.Label("");
        emptySign.getStyleClass().add("transaction-overview-sign");
        javafx.scene.control.Label proceedsText = new javafx.scene.control.Label("PROCEEDS:");
        proceedsText.getStyleClass().add("transaction-overview-label-text");
        proceedsText.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
        proceedsText.setMaxWidth(Double.MAX_VALUE);
        javafx.scene.control.Label proceedsValue = new javafx.scene.control.Label(quantity.multiply(price).toString());
        proceedsValue.getStyleClass().add("transaction-overview-label-value");
        proceedsValue.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        proceedsValue.setMaxWidth(Double.MAX_VALUE);

        javafx.scene.control.Label minusCommission = new javafx.scene.control.Label("-");
        minusCommission.getStyleClass().add("transaction-overview-sign");
        javafx.scene.control.Label commissionText = new javafx.scene.control.Label("COMMISSION:");
        commissionText.getStyleClass().add("transaction-overview-label-text");
        commissionText.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
        commissionText.setMaxWidth(Double.MAX_VALUE);
        javafx.scene.control.Label commissionValue = new javafx.scene.control.Label(commission.toString());
        commissionValue.getStyleClass().add("transaction-overview-label-value");
        commissionValue.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        commissionValue.setMaxWidth(Double.MAX_VALUE);

        javafx.scene.control.Label minusTax = new javafx.scene.control.Label("-");
        minusTax.getStyleClass().add("transaction-overview-sign");
        javafx.scene.control.Label taxText = new javafx.scene.control.Label("TAX:");
        taxText.getStyleClass().add("transaction-overview-label-text");
        taxText.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
        taxText.setMaxWidth(Double.MAX_VALUE);
        javafx.scene.control.Label taxValue = new javafx.scene.control.Label(tax.toString());
        taxValue.getStyleClass().add("transaction-overview-label-value");
        taxValue.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        taxValue.setMaxWidth(Double.MAX_VALUE);

        javafx.scene.control.Label eqTotal = new javafx.scene.control.Label("=");
        eqTotal.getStyleClass().add("transaction-overview-sign");
        javafx.scene.control.Label totalText = new javafx.scene.control.Label("TOTAL:");
        totalText.getStyleClass().add("transaction-overview-label-text");
        totalText.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
        totalText.setMaxWidth(Double.MAX_VALUE);
        javafx.scene.control.Label totalValue = new javafx.scene.control.Label(total.toString());
        totalValue.getStyleClass().add("transaction-overview-total");
        totalValue.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        totalValue.setMaxWidth(Double.MAX_VALUE);

        breakdownGrid.add(proceedsText, 0, 0);
        breakdownGrid.add(emptySign, 1, 0);
        breakdownGrid.add(proceedsValue, 2, 0);

        breakdownGrid.add(commissionText, 0, 1);
        breakdownGrid.add(minusCommission, 1, 1);
        breakdownGrid.add(commissionValue, 2, 1);

        breakdownGrid.add(taxText, 0, 2);
        breakdownGrid.add(minusTax, 1, 2);
        breakdownGrid.add(taxValue, 2, 2);

        breakdownGrid.add(totalText, 0, 3);
        breakdownGrid.add(eqTotal, 1, 3);
        breakdownGrid.add(totalValue, 2, 3);

        return breakdownGrid;
    }
}
