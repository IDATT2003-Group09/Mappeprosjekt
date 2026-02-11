package edu.ntnu.iir.bidata.idatt2003.group09;

import java.math.BigDecimal;

public class Sale extends Transaction {
    public Sale(Share share, int week) {
        super(share, week, new SaleCalculator(share));
    }

    @Override
    public void commit(Player player) {
        validateCommit(player);

        boolean removed = player.getPortfolio().removeShare(getShare());
        if (!removed) {
            throw new IllegalStateException("The player does not own the share being sold");
        }

        BigDecimal totalValue = getCalculator().calculateTotal();

        player.addMoney(totalValue);

        //store the transaction in the archive

        markCommited();
    }
}