package edu.ntnu.iir.bidata.idatt2003.group09.transaction;

import edu.ntnu.iir.bidata.idatt2003.group09.Stock;
import edu.ntnu.iir.bidata.idatt2003.group09.Share;
import edu.ntnu.iir.bidata.idatt2003.group09.Player;
import edu.ntnu.iir.bidata.idatt2003.group09.transaction.Purchase;
import edu.ntnu.iir.bidata.idatt2003.group09.transaction.Sale;
import edu.ntnu.iir.bidata.idatt2003.group09.transaction.Transaction;
import edu.ntnu.iir.bidata.idatt2003.group09.transaction.TransactionArchive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionArchiveTest {

    private TransactionArchive archive;
    private Stock stock;
    private Share share1;
    private Share share2;
    private Purchase purchase1;
    private Purchase purchase2;
    private Sale sale1;
    private Sale sale2;

    @BeforeEach
    void setUp() {
        archive = new TransactionArchive();
        stock = new Stock("AAPL", "Apple", new BigDecimal("150.00"));
        share1 = new Share(stock, new BigDecimal("2"), new BigDecimal("100.00"));
        share2 = new Share(stock, new BigDecimal("3"), new BigDecimal("120.00"));
        purchase1 = new Purchase(share1, 1);
        purchase2 = new Purchase(share2, 2);
        sale1 = new Sale(share1, 1);
        sale2 = new Sale(share2, 3);
    }

    @Test
    void constructorShouldCreateEmptyArchive() {
        assertTrue(archive.isEmpty());
    }

    @Test
    void addShouldThrowWhenTransactionIsNull() {
        assertThrows(NullPointerException.class, () -> archive.add(null));
    }

    @Test
    void addShouldAddTransactionAndReturnTrue() {
        assertTrue(archive.add(purchase1));
        assertFalse(archive.isEmpty());
    }

    @Test
    void addShouldAddMultipleTransactions() {
        archive.add(purchase1);
        archive.add(purchase2);
        archive.add(sale1);
        assertFalse(archive.isEmpty());
    }

    @Test
    void isEmptyShouldReturnTrueWhenEmpty() {
        assertTrue(archive.isEmpty());
    }

    @Test
    void isEmptyShouldReturnFalseWhenNotEmpty() {
        archive.add(purchase1);
        assertFalse(archive.isEmpty());
    }

    @Test
    void getTransactionsShouldThrowWhenWeekIsZero() {
        assertThrows(IllegalArgumentException.class, () -> archive.getTransactions(0));
    }

    @Test
    void getTransactionsShouldThrowWhenWeekIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> archive.getTransactions(-1));
    }

    @Test
    void getTransactionsShouldReturnEmptyListWhenNoTransactions() {
        List<Transaction> transactions = archive.getTransactions(1);
        assertNotNull(transactions);
        assertTrue(transactions.isEmpty());
    }

    @Test
    void getTransactionsShouldReturnTransactionsForSpecificWeek() {
        archive.add(purchase1);
        archive.add(purchase2);
        archive.add(sale1);
        
        List<Transaction> week1 = archive.getTransactions(1);
        assertEquals(2, week1.size());
        assertTrue(week1.contains(purchase1));
        assertTrue(week1.contains(sale1));
    }

    @Test
    void getTransactionsShouldReturnUnmodifiableList() {
        archive.add(purchase1);
        List<Transaction> transactions = archive.getTransactions(1);
        assertThrows(UnsupportedOperationException.class, () -> 
            transactions.add(purchase2)
        );
    }

    @Test
    void getPurchasesShouldThrowWhenWeekIsZero() {
        assertThrows(IllegalArgumentException.class, () -> archive.getPurchases(0));
    }

    @Test
    void getPurchasesShouldThrowWhenWeekIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> archive.getPurchases(-1));
    }

    @Test
    void getPurchasesShouldReturnEmptyListWhenNoPurchases() {
        List<Purchase> purchases = archive.getPurchases(1);
        assertNotNull(purchases);
        assertTrue(purchases.isEmpty());
    }

    @Test
    void getPurchasesShouldReturnOnlyPurchasesForSpecificWeek() {
        archive.add(purchase1);
        archive.add(purchase2);
        archive.add(sale1);
        archive.add(sale2);
        
        List<Purchase> week1Purchases = archive.getPurchases(1);
        assertEquals(1, week1Purchases.size());
        assertSame(purchase1, week1Purchases.get(0));
    }

    @Test
    void getPurchasesShouldNotIncludeSales() {
        archive.add(purchase1);
        archive.add(sale1);
        
        List<Purchase> purchases = archive.getPurchases(1);
        assertEquals(1, purchases.size());
        assertSame(purchase1, purchases.get(0));
    }

    @Test
    void getPurchasesShouldReturnUnmodifiableList() {
        archive.add(purchase1);
        List<Purchase> purchases = archive.getPurchases(1);
        assertThrows(UnsupportedOperationException.class, () -> 
            purchases.add(purchase2)
        );
    }

    @Test
    void getSalesShouldThrowWhenWeekIsZero() {
        assertThrows(IllegalArgumentException.class, () -> archive.getSales(0));
    }

    @Test
    void getSalesShouldThrowWhenWeekIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> archive.getSales(-1));
    }

    @Test
    void getSalesShouldReturnEmptyListWhenNoSales() {
        List<Sale> sales = archive.getSales(1);
        assertNotNull(sales);
        assertTrue(sales.isEmpty());
    }

    @Test
    void getSalesShouldReturnOnlySalesForSpecificWeek() {
        archive.add(purchase1);
        archive.add(purchase2);
        archive.add(sale1);
        archive.add(sale2);
        
        List<Sale> week1Sales = archive.getSales(1);
        assertEquals(1, week1Sales.size());
        assertSame(sale1, week1Sales.get(0));
    }

    @Test
    void getSalesShouldNotIncludePurchases() {
        archive.add(purchase1);
        archive.add(sale1);
        
        List<Sale> sales = archive.getSales(1);
        assertEquals(1, sales.size());
        assertSame(sale1, sales.get(0));
    }

    @Test
    void getSalesShouldReturnUnmodifiableList() {
        archive.add(sale1);
        List<Sale> sales = archive.getSales(1);
        assertThrows(UnsupportedOperationException.class, () -> 
            sales.add(sale2)
        );
    }

    @Test
    void countDistinctWeeksShouldReturnZeroWhenEmpty() {
        assertEquals(0, archive.countDistinctWeeks());
    }

    @Test
    void countDistinctWeeksShouldReturnOneWhenAllTransactionsInSameWeek() {
        archive.add(purchase1);
        archive.add(sale1);
        assertEquals(1, archive.countDistinctWeeks());
    }

    @Test
    void countDistinctWeeksShouldReturnCorrectCountForMultipleWeeks() {
        archive.add(purchase1); // week 1
        archive.add(purchase2); // week 2
        archive.add(sale1);     // week 1
        archive.add(sale2);     // week 3
        assertEquals(3, archive.countDistinctWeeks());
    }

    @Test
    void countDistinctWeeksShouldNotCountDuplicateWeeks() {
        Purchase purchase3 = new Purchase(share1, 1);
        Purchase purchase4 = new Purchase(share2, 1);
        archive.add(purchase1);
        archive.add(purchase3);
        archive.add(purchase4);
        assertEquals(1, archive.countDistinctWeeks());
    }

    @Test
    void getTransactionsShouldReturnCorrectTransactionsForDifferentWeeks() {
        archive.add(purchase1); // week 1
        archive.add(purchase2); // week 2
        archive.add(sale2);     // week 3
        
        assertEquals(1, archive.getTransactions(1).size());
        assertEquals(1, archive.getTransactions(2).size());
        assertEquals(1, archive.getTransactions(3).size());
        assertTrue(archive.getTransactions(4).isEmpty());
    }
}
