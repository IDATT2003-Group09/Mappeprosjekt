package edu.ntnu.iir.bidata.idatt2003.group09.model.transaction;

import java.math.BigDecimal;

import edu.ntnu.iir.bidata.idatt2003.group09.model.Player;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Share;
import edu.ntnu.iir.bidata.idatt2003.group09.model.calculator.PurchaseCalculator;


public class Purchase extends Transaction {
    public Purchase(Share share, int week) {
        super(share, week, new PurchaseCalculator(share));
    }

    public Purchase(Share share, int week, java.math.BigDecimal commissionRate) {
        super(share, week, new PurchaseCalculator(share, commissionRate));
    }

    @Override
    protected PurchaseCalculator createCalculator(Share share) {
        return new PurchaseCalculator(share);
    }

    @Override
    public void commit(Player player) {
        validateCommit(player);

        BigDecimal totalCost = getCalculator().calculateTotal();

        player.withdrawMoney(totalCost);

        player.getPortfolio().addShare(getShare());

        player.getTransactionArchive().add(this);

        markCommited();
    }
}