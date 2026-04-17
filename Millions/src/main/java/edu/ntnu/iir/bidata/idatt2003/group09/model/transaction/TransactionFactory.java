package edu.ntnu.iir.bidata.idatt2003.group09.model.transaction;

import edu.ntnu.iir.bidata.idatt2003.group09.model.Share;

public class TransactionFactory {

    private TransactionFactory() {
    }

    public static Transaction createPurchase(Share share, int week) {
        return new Purchase(share, week);
    }

    public static Transaction createPurchase(Share share, int week, java.math.BigDecimal commissionRate) {
        return new Purchase(share, week, commissionRate);
    }

    public static Transaction createSale(Share share, int week) {
        return new Sale(share, week);
    }
}