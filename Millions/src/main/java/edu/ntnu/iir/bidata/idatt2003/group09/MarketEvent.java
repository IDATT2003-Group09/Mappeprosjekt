package edu.ntnu.iir.bidata.idatt2003.group09;

import java.math.BigDecimal;

public class MarketEvent {

    private final String name;
    private final String sector;
    private final BigDecimal impact;

    public MarketEvent(String name, String sector, BigDecimal impact) {
        this.name = name;
        this.sector = sector;
        this.impact = impact;
    }

    public String getName() {
        return name;
    }

    public String getSector() {
        return sector;
    }

    public BigDecimal getImpact() {
        return impact;
    }
}