package edu.ntnu.iir.bidata.idatt2003.group09.view.elements;

import javafx.scene.layout.GridPane;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javafx.scene.control.Label;
import javafx.geometry.Pos;

public class BuyOverview extends TransactionOverview {
    public BuyOverview(String stockSymbol, BigDecimal quantity, BigDecimal price, BigDecimal commission, BigDecimal tax, BigDecimal total, Runnable onConfirm, Runnable onCancel) {
        super("Buy", stockSymbol, quantity, price, commission, tax, total, onConfirm, onCancel);
    }

    @Override
    protected GridPane createBreakdownGrid(BigDecimal quantity, BigDecimal price, BigDecimal commission, BigDecimal tax, BigDecimal total) {
        GridPane breakdownGrid = new GridPane();
        breakdownGrid.setHgap(8);
        breakdownGrid.setVgap(4);
        breakdownGrid.setMaxWidth(Double.MAX_VALUE);

        // Buy: COST + COMMISSION + TAX = TOTAL
        Label emptySign = new Label("");
        emptySign.getStyleClass().add("transaction-overview-sign");
        Label costText = new Label("COST:");
        costText.getStyleClass().add("transaction-overview-label-text");
        costText.setAlignment(Pos.CENTER_RIGHT);
        costText.setMaxWidth(Double.MAX_VALUE);
        Label costValue = new Label(quantity.multiply(price).setScale(2, RoundingMode.HALF_UP).toString());
        costValue.getStyleClass().add("transaction-overview-label-value");
        costValue.setAlignment(Pos.CENTER_LEFT);
        costValue.setMaxWidth(Double.MAX_VALUE);

        Label plusCommission = new Label("+");
        plusCommission.getStyleClass().add("transaction-overview-sign");
        Label commissionText = new Label("COMMISSION:");
        commissionText.getStyleClass().add("transaction-overview-label-text");
        commissionText.setAlignment(Pos.CENTER_RIGHT);
        commissionText.setMaxWidth(Double.MAX_VALUE);
        Label commissionValue = new Label(commission.setScale(2, RoundingMode.HALF_UP).toString());
        commissionValue.getStyleClass().add("transaction-overview-label-value");
        commissionValue.setAlignment(Pos.CENTER_LEFT);
        commissionValue.setMaxWidth(Double.MAX_VALUE);

        Label plusTax = new Label("+");
        plusTax.getStyleClass().add("transaction-overview-sign");
        Label taxText = new Label("TAX:");
        taxText.getStyleClass().add("transaction-overview-label-text");
        taxText.setAlignment(Pos.CENTER_RIGHT);
        taxText.setMaxWidth(Double.MAX_VALUE);
        Label taxValue = new Label(tax.setScale(2, RoundingMode.HALF_UP).toString());
        taxValue.getStyleClass().add("transaction-overview-label-value");
        taxValue.setAlignment(Pos.CENTER_LEFT);
        taxValue.setMaxWidth(Double.MAX_VALUE);

        Label eqTotal = new Label("=");
        eqTotal.getStyleClass().add("transaction-overview-sign");
        Label totalText = new Label("TOTAL:");
        totalText.getStyleClass().add("transaction-overview-label-text");
        totalText.setAlignment(Pos.CENTER_RIGHT);
        totalText.setMaxWidth(Double.MAX_VALUE);
        Label totalValue = new Label(total.setScale(2, RoundingMode.HALF_UP).toString());
        totalValue.getStyleClass().add("transaction-overview-total");
        totalValue.setAlignment(Pos.CENTER_LEFT);
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
