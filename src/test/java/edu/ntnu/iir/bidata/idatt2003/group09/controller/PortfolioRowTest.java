package edu.ntnu.iir.bidata.idatt2003.group09.controller;

import edu.ntnu.iir.bidata.idatt2003.group09.model.Share;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Stock;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class PortfolioRowTest {

    @Test
    void calculations_areCorrectForBasicShare() {
        Stock s = new Stock("TST","TestCo", new BigDecimal("10"), "Tech", 1);
        Share share = new Share(s, new BigDecimal("5"), new BigDecimal("8"));

        // add a new price to create weekly change before building the row
        s.addNewSalesPrice(new BigDecimal("12"));

        PortfolioRow row = new PortfolioRow(share);

        assertEquals(new BigDecimal("60"), row.getValue());
        assertEquals(new BigDecimal("40"), row.getCost());
        assertEquals(new BigDecimal("20"), row.getGain());
        assertEquals(new BigDecimal("50.0000"), row.getPercentGain());
        assertEquals(new BigDecimal("4"), row.getPriceChange());
        assertEquals(new BigDecimal("2"), row.getWeeklyPriceChange());
        // previous price = currentPrice - change => 12 - 2 = 10; percent = 2/10*100 = 20
        assertEquals(new BigDecimal("20.0000"), row.getWeeklyPercentChange());
    }
}
