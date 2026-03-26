
package edu.ntnu.iir.bidata.idatt2003.group09.base.transaction;

import edu.ntnu.iir.bidata.idatt2003.group09.base.Player;
import edu.ntnu.iir.bidata.idatt2003.group09.base.Share;
import edu.ntnu.iir.bidata.idatt2003.group09.base.calculator.PurchaseCalculator;

import java.math.BigDecimal;

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