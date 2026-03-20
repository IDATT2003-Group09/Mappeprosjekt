package edu.ntnu.iir.bidata.idatt2003.group09;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TitleTest {

    private Player player;
    private Exchange exchange;
    private Title title;

    @BeforeEach
    void setUp() {
        player = new Player("Alice", BigDecimal.valueOf(1000));
        Stock stock = new Stock("AAPL", "Apple", BigDecimal.valueOf(100));
        exchange = new Exchange("Test Exchange", List.of(stock));
        title = new Title(player, exchange);
    }

    @Test
    void assignTitle_returnsNovice_whenOnlyStatusRequirementIsMet() {
        String assigned = title.assignTitle(20, 9);
        assertEquals("Novice", assigned);
    }

    @Test
    void assignTitle_returnsNovice_whenOnlyWeeksRequirementIsMet() {
        String assigned = title.assignTitle(19, 10);
        assertEquals("Novice", assigned);
    }

    @Test
    void assignTitle_returnsInvestor_whenBothInvestorRequirementsAreMet() {
        String assigned = title.assignTitle(20, 10);
        assertEquals("Investor", assigned);
    }

    @Test
    void assignTitle_returnsSpeculator_whenBothSpeculatorRequirementsAreMet() {
        String assigned = title.assignTitle(100, 20);
        assertEquals("Speculator", assigned);
    }

    @Test
    void assignTitle_withPlayer_usesExchangeWeeksAndPlayerStatus() {
        for (int i = 0; i < 10; i++) {
            exchange.Advance();
        }
        player.addMoney(BigDecimal.valueOf(200));

        String assigned = title.assignTitle(player);

        assertEquals("Investor", assigned);
    }

    @Test
    void assignTitle_throwsException_whenWeeksPlayedIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> title.assignTitle(20, -1));
    }
}
