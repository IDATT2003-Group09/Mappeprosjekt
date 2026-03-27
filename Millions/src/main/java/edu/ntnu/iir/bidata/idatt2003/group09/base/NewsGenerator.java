package edu.ntnu.iir.bidata.idatt2003.group09.base;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;

public class NewsGenerator {

    private final Random random = new Random();

    private final List<MarketNews> newsPool = List.of(
            new MarketNews(
                    "Strong economic growth",
                    "Global economy shows strong growth, and experts at wall street suggest the market will continue to rise",
                    Map.of(
                        "Technology", new BigDecimal("0.10"),
                        "Finance", new BigDecimal("0.08"),
                        "Healthcare", new BigDecimal("0.07"),
                        "Energy", new BigDecimal("0.09"),
                        "Consumer", new BigDecimal("0.06")
                    )
            ),
            new MarketNews(
                    "Interest rate hike",
                    "Central bank raises interest rates",
                    Map.of(
                        "Technology", new BigDecimal("-0.12"),
                        "Finance", new BigDecimal("0.05"),
                        "Healthcare", new BigDecimal("-0.04"),
                        "Energy", new BigDecimal("-0.08"),
                        "Consumer", new BigDecimal("-0.10")
                    )
            ),
            new MarketNews(
                "War in the middle east",
                "War breaks out in the middle east, causing uncertainty in the markets",
                Map.of(
                    "Technology", new BigDecimal("-0.15"),
                    "Finance", new BigDecimal("-0.10"),
                    "Healthcare", new BigDecimal("-0.05"),
                    "Energy", new BigDecimal("0.20"),
                    "Consumer", new BigDecimal("-0.12")
                )
            )
            // Add more news events as needed
    );

    public MarketNews getRandomNews() {
        if (random.nextInt(100) < 67) {
            return null;
        }
        return newsPool.get(random.nextInt(newsPool.size()));
    }
}