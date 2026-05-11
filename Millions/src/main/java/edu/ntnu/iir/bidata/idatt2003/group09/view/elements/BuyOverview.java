package edu.ntnu.iir.bidata.idatt2003.group09.view.elements;

import javafx.scene.layout.GridPane;
import java.math.BigDecimal;

public class BuyOverview extends TransactionOverview {
    public BuyOverview(String stockSymbol, BigDecimal quantity, BigDecimal price, BigDecimal commission, BigDecimal tax, BigDecimal total, Runnable onConfirm) {
        super("Buy", stockSymbol, quantity, price, commission, tax, total, onConfirm);
    }

    @Override
    protected GridPane createBreakdownGrid(BigDecimal quantity, BigDecimal price, BigDecimal commission, BigDecimal tax, BigDecimal total) {
        GridPane breakdownGrid = new GridPane();
        breakdownGrid.setHgap(8);
        breakdownGrid.setVgap(4);
        breakdownGrid.setMaxWidth(Double.MAX_VALUE);

        // Buy: COST + COMMISSION + TAX = TOTAL
        javafx.scene.control.Label emptySign = new javafx.scene.control.Label("");
        emptySign.getStyleClass().add("transaction-overview-sign");
        javafx.scene.control.Label costText = new javafx.scene.control.Label("COST:");
        costText.getStyleClass().add("transaction-overview-label-text");
        costText.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
        costText.setMaxWidth(Double.MAX_VALUE);
        javafx.scene.control.Label costValue = new javafx.scene.control.Label(quantity.multiply(price).toString());
        costValue.getStyleClass().add("transaction-overview-label-value");
        costValue.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        costValue.setMaxWidth(Double.MAX_VALUE);

        javafx.scene.control.Label plusCommission = new javafx.scene.control.Label("+");
        plusCommission.getStyleClass().add("transaction-overview-sign");
        javafx.scene.control.Label commissionText = new javafx.scene.control.Label("COMMISSION:");
        commissionText.getStyleClass().add("transaction-overview-label-text");
        commissionText.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
        commissionText.setMaxWidth(Double.MAX_VALUE);
        javafx.scene.control.Label commissionValue = new javafx.scene.control.Label(commission.toString());
        commissionValue.getStyleClass().add("transaction-overview-label-value");
        commissionValue.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        commissionValue.setMaxWidth(Double.MAX_VALUE);

        javafx.scene.control.Label plusTax = new javafx.scene.control.Label("+");
        plusTax.getStyleClass().add("transaction-overview-sign");
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

        breakdownGrid.add(costText, 0, 0);
        breakdownGrid.add(emptySign, 1, 0);
        breakdownGrid.add(costValue, 2, 0);

        breakdownGrid.add(commissionText, 0, 1);
        breakdownGrid.add(plusCommission, 1, 1);
        breakdownGrid.add(commissionValue, 2, 1);

        breakdownGrid.add(taxText, 0, 2);
        breakdownGrid.add(plusTax, 1, 2);
        breakdownGrid.add(taxValue, 2, 2);

        breakdownGrid.add(totalText, 0, 3);
        breakdownGrid.add(eqTotal, 1, 3);
        breakdownGrid.add(totalValue, 2, 3);

        return breakdownGrid;
    }
}
