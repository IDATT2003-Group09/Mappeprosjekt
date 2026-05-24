package edu.ntnu.iir.bidata.idatt2003.group09.controller.screen;

import edu.ntnu.iir.bidata.idatt2003.group09.controller.GameController;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Exchange;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Player;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Share;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Stock;
import edu.ntnu.iir.bidata.idatt2003.group09.model.transaction.Transaction;
import edu.ntnu.iir.bidata.idatt2003.group09.model.transaction.TransactionFactory;
import edu.ntnu.iir.bidata.idatt2003.group09.model.transaction.TransactionArchive;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionHistoryScreenControllerTest {

    @Test
    void refresh_populatesModelWithReversedTransactions() {
        Stock s = new Stock("TST","TestCo", new BigDecimal("10"), "Tech", 1);
        Exchange exchange = new Exchange("Ex", List.of(s));
        Player player = new Player("U", new BigDecimal("1000"), "Easy");
        GameController gc = new GameController(exchange, player);

        // create two transactions in chronological order
        Share share1 = new Share(s, new BigDecimal("1"), new BigDecimal("10"));
        Transaction t1 = TransactionFactory.createPurchase(share1, 1);
        Transaction t2 = TransactionFactory.createPurchase(share1, 2);

        TransactionArchive archive = player.getTransactionArchive();
        archive.add(t1);
        archive.add(t2);

        TransactionHistoryScreenController ctrl = new TransactionHistoryScreenController(gc);
        ctrl.refresh();

        var modelList = ctrl.getModel().transactionsProperty();
        assertEquals(2, modelList.size());
        // newest first
        assertSame(t2, modelList.get(0));
        assertSame(t1, modelList.get(1));
    }
}
