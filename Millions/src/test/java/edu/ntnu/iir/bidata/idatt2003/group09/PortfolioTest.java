package edu.ntnu.iir.bidata.idatt2003.group09;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Portfolio class.
 */
public class PortfolioTest {

    private Portfolio portfolio;
    private Share appleShare1;
    private Share appleShare2;
    private Share microsoftShare;

    /**
     * Sets up a portfolio with valid shares before each test.
     */
    @BeforeEach
    void setUp() {
        portfolio = new Portfolio();

        Stock apple = new Stock("AAPL", "Apple", new BigDecimal("150.00"));
        Stock microsoft = new Stock("MSFT", "Microsoft", new BigDecimal("300.00"));

        appleShare1 = new Share(apple, new BigDecimal("10"), new BigDecimal("120"));
        appleShare2 = new Share(apple, new BigDecimal("5"), new BigDecimal("130"));
        microsoftShare = new Share(microsoft, new BigDecimal("8"), new BigDecimal("250"));
    }

    /**
     * Verifies that adding a share returns true.
     */
    @Test
    void addShareShouldReturnTrue() {
        assertTrue(portfolio.addShare(appleShare1));
    }

    /**
     * Verifies that an existing share can be removed.
     */
    @Test
    void removeShareShouldReturnTrueWhenShareExists() {
        portfolio.addShare(appleShare1);
        assertTrue(portfolio.removeShare(appleShare1));
    }

    /**
     * Verifies that shares are correctly filtered by stock symbol.
     */
    @Test
    void getSharesBySymbolShouldReturnMatchingShares() {
        portfolio.addShare(appleShare1);
        portfolio.addShare(appleShare2);
        portfolio.addShare(microsoftShare);

        List<Share> result = portfolio.getShares("AAPL");

        assertEquals(2, result.size());
        assertTrue(result.contains(appleShare1));
        assertTrue(result.contains(appleShare2));
    }

    /**
     * Verifies that symbol matching is case-insensitive.
     */
    @Test
    void getSharesBySymbolShouldIgnoreCase() {
        portfolio.addShare(appleShare1);

        List<Share> result = portfolio.getShares("aapl");

        assertEquals(1, result.size());
    }

    /**
     * Verifies that removing a non-existing share returns false.
     */
    @Test
    void removeShareShouldReturnFalseWhenShareDoesNotExist() {
        assertFalse(portfolio.removeShare(appleShare1));
    }

    /**
     * Verifies that filtering by a symbol not present returns an empty list.
     */
    @Test
    void getSharesBySymbolShouldReturnEmptyListWhenNoMatch() {
        portfolio.addShare(appleShare1);

        List<Share> result = portfolio.getShares("AMZN");

        assertTrue(result.isEmpty());
    }

    /**
     * Verifies that filtering an empty portfolio returns an empty list.
     */
    @Test
    void getSharesBySymbolShouldReturnEmptyListWhenPortfolioIsEmpty() {
        List<Share> result = portfolio.getShares("AAPL");

        assertTrue(result.isEmpty());
    }

    /**
     * Verifies that the returned list of shares is unmodifiable.
     */
    @Test
    void getSharesShouldReturnUnmodifiableList() {
        portfolio.addShare(appleShare1);

        List<Share> shares = portfolio.getShares();

        assertThrows(UnsupportedOperationException.class,
                () -> shares.add(appleShare2));
    }
}
