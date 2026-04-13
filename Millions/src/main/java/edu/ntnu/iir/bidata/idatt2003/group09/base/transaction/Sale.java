package edu.ntnu.iir.bidata.idatt2003.group09.base.transaction;

import edu.ntnu.iir.bidata.idatt2003.group09.base.Player;
import edu.ntnu.iir.bidata.idatt2003.group09.base.Share;
import edu.ntnu.iir.bidata.idatt2003.group09.base.calculator.SaleCalculator;

import java.math.BigDecimal;

public class Sale extends Transaction {
    public Sale(Share share, int week) {
        super(share, week, new SaleCalculator(share));
    }

    @Override
    protected SaleCalculator createCalculator(Share share) {
        return new SaleCalculator(share);
    }

    @Override
    public void commit(Player player) {
        validateCommit(player);

        boolean removed = player.getPortfolio().removeShare(getShare());
        if (!removed) {
            Share ownedShare = player.getPortfolio().getShares().stream()
                .filter(share -> share.getStock().getSymbol().equalsIgnoreCase(getShare().getStock().getSymbol()))
                .filter(share -> share.getPurchasePrice().compareTo(getShare().getPurchasePrice()) == 0)
                .filter(share -> share.getQuantity().compareTo(getShare().getQuantity()) >= 0)
                .findFirst()
                .orElse(null);

            if (ownedShare == null) {
                throw new IllegalStateException("The player does not own the share being sold");
            }

            BigDecimal remainingQuantity = ownedShare.getQuantity().subtract(getShare().getQuantity());
            if (remainingQuantity.compareTo(BigDecimal.ZERO) == 0) {
                player.getPortfolio().removeShare(ownedShare);
            } else {
                ownedShare.setQuantity(remainingQuantity);
            }
        }

        BigDecimal totalValue = getCalculator().calculateTotal();

        player.addMoney(totalValue);

        player.getTransactionArchive().add(this);

        markCommited();
    }
}