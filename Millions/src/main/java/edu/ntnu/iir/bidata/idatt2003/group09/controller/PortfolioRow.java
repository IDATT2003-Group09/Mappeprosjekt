package edu.ntnu.iir.bidata.idatt2003.group09.controller;

import edu.ntnu.iir.bidata.idatt2003.group09.base.Share;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PortfolioRow {

    private final String symbol;
    private final String company;
    private final BigDecimal quantity;
    private final BigDecimal currentPrice;
    private final BigDecimal purchasePrice;

    public PortfolioRow(Share share) {
        this.symbol = share.getStock().getSymbol();
        this.company = share.getStock().getCompany();
        this.quantity = share.getQuantity();
        this.currentPrice = share.getStock().getSalesPrice();
        this.purchasePrice = share.getPurchasePrice();
    }


}