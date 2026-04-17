package edu.ntnu.iir.bidata.idatt2003.group09.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;

import edu.ntnu.iir.bidata.idatt2003.group09.model.Share;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Stock;

public class PortfolioRow {

    private final String symbol;
    private final String company;
    private final BigDecimal quantity;
    private final BigDecimal currentPrice;
    private final BigDecimal purchasePrice;
    private final Stock stock;

    public PortfolioRow(Share share) {
        this.symbol = share.getStock().getSymbol();
        this.company = share.getStock().getCompany();
        this.quantity = share.getQuantity();
        this.currentPrice = share.getStock().getSalesPrice();
        this.purchasePrice = share.getPurchasePrice();
        this.stock = share.getStock();
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

    public BigDecimal getWeeklyPriceChange() {
        return stock.getLatestPriceChange();
    }

    public BigDecimal getPricePercentChange() {
        if (purchasePrice.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;

        return getPriceChange()
                .divide(purchasePrice, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    public BigDecimal getWeeklyPercentChange() {
        BigDecimal change = getWeeklyPriceChange();
        BigDecimal previous = currentPrice.subtract(change);

        if (previous.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return change
                .divide(previous, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    public String getSymbol() { return symbol; }
    public String getCompany() { return company; }
    public BigDecimal getQuantity() { return quantity; }
    public BigDecimal getCurrentPrice() { return currentPrice; }
}