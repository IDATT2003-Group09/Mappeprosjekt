package edu.ntnu.iir.bidata.idatt2003.group09.base;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class MarketNews {

    private final String headline;
    private final String description;


    // Map of sector name to impact value
    private final Map<String, BigDecimal> sectorImpacts;


    /**
     * Creates a MarketNews event with sector-specific impacts.
     * @param headline The news headline
     * @param description The news description
     * @param sectorImpacts A map of sector names to their impact values
     */
    public MarketNews(String headline, String description, Map<String, BigDecimal> sectorImpacts) {
        this.headline = headline;
        this.description = description;
        this.sectorImpacts = new HashMap<>(sectorImpacts);
    }

    public String getHeadline() {
        return headline;
    }

    public String getDescription() {
        return description;
    }


    /**
     * Returns an unmodifiable view of the sector impacts.
     */
    public Map<String, BigDecimal> getSectorImpacts() {
        return java.util.Collections.unmodifiableMap(sectorImpacts);
    }

    /**
     * Gets the impact for a specific sector, or null if not present.
     */
    public BigDecimal getImpactForSector(String sector) {
        return sectorImpacts.get(sector);
    }
}