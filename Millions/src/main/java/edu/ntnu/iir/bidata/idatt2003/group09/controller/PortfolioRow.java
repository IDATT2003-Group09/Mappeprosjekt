package edu.ntnu.iir.bidata.idatt2003.group09.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;

import edu.ntnu.iir.bidata.idatt2003.group09.model.Share;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Stock;

/**
 * Represents a row in the portfolio table, summarizing a stock holding for display.
 */
public class PortfolioRow {

    private final String symbol;
    private final String company;
    private final BigDecimal quantity;
    private final BigDecimal currentPrice;
    private final BigDecimal purchasePrice;
    private final Stock stock;

    /**
     * Constructs a PortfolioRow from a Share object.
     *
     * @param share the Share representing the stock holding
     */
    public PortfolioRow(Share share) {
        this.symbol = share.getStock().getSymbol();
        this.company = share.getStock().getCompany();
        this.quantity = share.getQuantity();
        this.currentPrice = share.getStock().getSalesPrice();
        this.purchasePrice = share.getPurchasePrice();
        this.stock = share.getStock();
    }

    /**
     * Gets the current value of this holding (current price × quantity).
     *
     * @return the value as BigDecimal
     */
    public BigDecimal getValue() {
        return currentPrice.multiply(quantity);
    }

    /**
     * Gets the total cost basis of this holding (purchase price × quantity).
     *
     * @return the cost as BigDecimal
     */
    public BigDecimal getCost() {
        return purchasePrice.multiply(quantity);
    }

    /**
     * Gets the total gain or loss (current value - cost).
     *
     * @return the gain or loss as BigDecimal
     */
    public BigDecimal getGain() {
        return getValue().subtract(getCost());
    }

    /**
     * Gets the percent gain or loss relative to cost.
     *
     * @return the percent gain/loss as BigDecimal
     */
    public BigDecimal getPercentGain() {
        if (getCost().compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;

        return getGain()
                .divide(getCost(), 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    /**
     * Gets the absolute price change (current price - purchase price).
     *
     * @return the price change as BigDecimal
     */
    public BigDecimal getPriceChange() {
        return currentPrice.subtract(purchasePrice);
    }

    /**
     * Gets the price change for the most recent week.
     *
     * @return the weekly price change as BigDecimal
     */
    public BigDecimal getWeeklyPriceChange() {
        return stock.getLatestPriceChange();
    }

    /**
     * Gets the percent price change since purchase.
     *
     * @return the percent price change as BigDecimal
     */
    public BigDecimal getPricePercentChange() {
        if (purchasePrice.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;

        return getPriceChange()
                .divide(purchasePrice, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    /**
     * Gets the percent price change for the most recent week.
     *
     * @return the weekly percent price change as BigDecimal
     */
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

    /**
     * Gets the stock symbol.
     *
     * @return the symbol
     */
    public String getSymbol() { return symbol; }

    /**
     * Gets the company name.
     *
     * @return the company name
     */
    public String getCompany() { return company; }

    /**
     * Gets the quantity of shares held.
     *
     * @return the quantity
     */
    public BigDecimal getQuantity() { return quantity; }

    /**
     * Gets the current price per share.
     *
     * @return the current price
     */
    public BigDecimal getCurrentPrice() { return currentPrice; }
}