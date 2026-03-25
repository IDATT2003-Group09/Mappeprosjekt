package edu.ntnu.iir.bidata.idatt2003.group09.transaction;

import edu.ntnu.iir.bidata.idatt2003.group09.Stock;
import edu.ntnu.iir.bidata.idatt2003.group09.Share;
import edu.ntnu.iir.bidata.idatt2003.group09.Player;
import edu.ntnu.iir.bidata.idatt2003.group09.calculator.PurchaseCalculator;
import edu.ntnu.iir.bidata.idatt2003.group09.calculator.TransactionCalculator;
import edu.ntnu.iir.bidata.idatt2003.group09.transaction.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionTest {

    private Stock stock;
    private Share share;
    private Player player;

    @BeforeEach
    void setUp() {
        stock = new Stock("AAPL", "Apple", new BigDecimal("150.00"), "Technology", 4);
        share = new Share(stock, new BigDecimal("2"), new BigDecimal("100.00"));
        player = new Player("Alice", new BigDecimal("1000.00"));
    }

    @Test
    void constructorShouldThrowWhenShareIsNull() {
        assertThrows(NullPointerException.class, () -> 
            new TestTransaction(null, 1, new PurchaseCalculator(share))
        );
    }

    @Test
    void constructorShouldThrowWhenCalculatorIsNull() {
        assertThrows(NullPointerException.class, () -> 
            new TestTransaction(share, 1, null)
        );
    }

    @Test
    void constructorShouldThrowWhenWeekIsZero() {
        assertThrows(IllegalArgumentException.class, () -> 
            new TestTransaction(share, 0, new PurchaseCalculator(share))
        );
    }

    @Test
    void constructorShouldThrowWhenWeekIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> 
            new TestTransaction(share, -1, new PurchaseCalculator(share))
        );
    }

    @Test
    void constructorShouldAcceptValidParameters() {
        assertDoesNotThrow(() -> 
            new TestTransaction(share, 1, new PurchaseCalculator(share))
        );
    }

    @Test
    void getShareShouldReturnShare() {
        Transaction transaction = new TestTransaction(share, 1, new PurchaseCalculator(share));
        assertSame(share, transaction.getShare());
    }

    @Test
    void getWeekShouldReturnWeek() {
        Transaction transaction = new TestTransaction(share, 5, new PurchaseCalculator(share));
        assertEquals(5, transaction.getWeek());
    }

    @Test
    void getCalculatorShouldReturnCalculator() {
        TransactionCalculator calculator = new PurchaseCalculator(share);
        Transaction transaction = new TestTransaction(share, 1, calculator);
        assertSame(calculator, transaction.getCalculator());
    }

    @Test
    void isCommittedShouldReturnFalseInitially() {
        Transaction transaction = new TestTransaction(share, 1, new PurchaseCalculator(share));
        assertFalse(transaction.isCommited());
    }

    @Test
    void isCommittedShouldReturnTrueAfterCommit() {
        Transaction transaction = new TestTransaction(share, 1, new PurchaseCalculator(share));
        transaction.commit(player);
        assertTrue(transaction.isCommited());
    }

    @Test
    void validateCommitShouldThrowWhenPlayerIsNull() {
        Transaction transaction = new TestTransaction(share, 1, new PurchaseCalculator(share));
        assertThrows(NullPointerException.class, () -> transaction.commit(null));
    }

    @Test
    void validateCommitShouldThrowWhenAlreadyCommitted() {
        Transaction transaction = new TestTransaction(share, 1, new PurchaseCalculator(share));
        transaction.commit(player);
        assertThrows(IllegalStateException.class, () -> transaction.commit(player));
    }

    @Test
    void commitShouldAllowValidCommit() {
        Transaction transaction = new TestTransaction(share, 1, new PurchaseCalculator(share));
        assertDoesNotThrow(() -> transaction.commit(player));
    }

    /**
     * A concrete implementation of Transaction for testing purposes
     */
    private static class TestTransaction extends Transaction {
        
        public TestTransaction(Share share, int week, TransactionCalculator calculator) {
            super(share, week, calculator);
        }

        @Override
        public void commit(Player player) {
            validateCommit(player);
            markCommited();
        }
    }
}
