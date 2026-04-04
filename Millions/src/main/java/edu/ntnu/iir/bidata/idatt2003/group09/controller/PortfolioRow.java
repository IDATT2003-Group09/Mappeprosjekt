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

    public BigDecimal getValue() {
        return currentPrice.multiply(quantity);
    }

    public BigDecimal getCost() {
        return purchasePrice.multiply(quantity);
    }

    public BigDecimal getGain() {
        return getValue().subtract(getCost());
    }

    public BigDecimal getPercentGain() {
        if (getCost().compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;

        return getGain()
                .divide(getCost(), 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    public BigDecimal getPriceChange() {
        return currentPrice.subtract(purchasePrice);
    }

    public BigDecimal getPricePercentChange() {
        if (purchasePrice.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;

        return getPriceChange()
                .divide(purchasePrice, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    public String getSymbol() { return symbol; }
    public String getCompany() { return company; }
    public BigDecimal getQuantity() { return quantity; }
    public BigDecimal getCurrentPrice() { return currentPrice; }
}