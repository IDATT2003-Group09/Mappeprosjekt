package edu.ntnu.iir.bidata.idatt2003.group09.base;

import java.io.Serializable;
import java.math.BigDecimal;

public class MarketNews implements Serializable {

    private final String headline;
    private final String description;

    private final String sector;
    private final BigDecimal impact;

    public MarketNews(String headline, String description, String sector, BigDecimal impact) {
        this.headline = headline;
        this.description = description;
        this.sector = sector;
        this.impact = impact;
    }

    public String getHeadline() {
        return headline;
    }

    public String getDescription() {
        return description;
    }

    public String getSector() {
        return sector;
    }

    public BigDecimal getImpact() {
        return impact;
    }
}