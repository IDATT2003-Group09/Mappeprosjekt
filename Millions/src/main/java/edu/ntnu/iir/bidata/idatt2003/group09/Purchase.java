package edu.ntnu.iir.bidata.idatt2003.group09;


import java.math.BigDecimal;
import java.util.Objects;

public class Purchase extends Transaction {
    public Purchase(Share share, int week) {
        super(share, week, new PurchaseCalculator(share));
    }

    @Override
    public void commit(Player player) {
        validateCommit(player);

        BigDecimal totalCost = getCalculator().calculateTotal();

        player.withdrawMoney(totalCost);

        player.getPortfolio().addShare(getShare());

        //Store the transaction in the archive

        markCommited();
    }
}