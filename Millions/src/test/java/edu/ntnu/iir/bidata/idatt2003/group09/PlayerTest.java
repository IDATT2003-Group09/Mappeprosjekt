package edu.ntnu.iir.bidata.idatt2003.group09;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player("Alice", BigDecimal.valueOf(1000));
    }


    @Test
    void constructor_createsPlayerWithCorrectInitialValues() {
        assertEquals("Alice", player.getName());
        assertEquals(BigDecimal.valueOf(1000), player.getMoney());
        assertNotNull(player.getPortfolio());
        assertNotNull(player.getTransactionArchive());
    }

    @Test
    void constructor_throwsException_whenNameIsNull() {
        assertThrows(NullPointerException.class,
                () -> new Player(null, BigDecimal.valueOf(1000)));
    }

    @Test
    void constructor_throwsException_whenNameIsBlank() {
        assertThrows(IllegalArgumentException.class,
                () -> new Player("   ", BigDecimal.valueOf(1000)));
    }

    @Test
    void constructor_throwsException_whenStartingMoneyIsNull() {
        assertThrows(NullPointerException.class,
                () -> new Player("Alice", null));
    }

    @Test
    void constructor_throwsException_whenStartingMoneyIsZeroOrNegative() {
        assertThrows(IllegalArgumentException.class,
                () -> new Player("Alice", BigDecimal.ZERO));

        assertThrows(IllegalArgumentException.class,
                () -> new Player("Alice", BigDecimal.valueOf(-100)));
    }

    @Test
    void addMoney_increasesMoneyBalance() {
        player.addMoney(BigDecimal.valueOf(500));
        assertEquals(BigDecimal.valueOf(1500), player.getMoney());
    }

    @Test
    void addMoney_throwsException_whenAmountIsNull() {
        assertThrows(NullPointerException.class,
                () -> player.addMoney(null));
    }

    @Test
    void addMoney_throwsException_whenAmountIsZeroOrNegative() {
        assertThrows(IllegalArgumentException.class,
                () -> player.addMoney(BigDecimal.ZERO));

        assertThrows(IllegalArgumentException.class,
                () -> player.addMoney(BigDecimal.valueOf(-50)));
    }

    @Test
    void withdrawMoney_decreasesMoneyBalance() {
        player.withdrawMoney(BigDecimal.valueOf(300));
        assertEquals(BigDecimal.valueOf(700), player.getMoney());
    }

    @Test
    void withdrawMoney_throwsException_whenAmountIsNull() {
        assertThrows(NullPointerException.class,
                () -> player.withdrawMoney(null));
    }

    @Test
    void withdrawMoney_throwsException_whenAmountIsZeroOrNegative() {
        assertThrows(IllegalArgumentException.class,
                () -> player.withdrawMoney(BigDecimal.ZERO));

        assertThrows(IllegalArgumentException.class,
                () -> player.withdrawMoney(BigDecimal.valueOf(-10)));
    }

    @Test
    void withdrawMoney_throwsException_whenInsufficientFunds() {
        assertThrows(IllegalStateException.class,
                () -> player.withdrawMoney(BigDecimal.valueOf(2000)));
    }

    @Test
    void updateStatus_setsZeroAtStartingMoney() {
        player.updateStatus();
        assertEquals(0, player.getStatus());
    }

    @Test
    void updateStatus_setsPositivePercentageWhenMoneyIncreases() {
        player.addMoney(BigDecimal.valueOf(500));
        player.updateStatus();
        assertEquals(50, player.getStatus());
    }

    @Test
    void updateStatus_setsNegativePercentageWhenMoneyDecreases() {
        player.withdrawMoney(BigDecimal.valueOf(250));
        player.updateStatus();
        assertEquals(-25, player.getStatus());
    }
}