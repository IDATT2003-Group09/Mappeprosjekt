package edu.ntnu.iir.bidata.idatt2003.group09.base;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

public class NewsGenerator {

    private final Random random = new Random();

    private final List<MarketNews> newsPool = List.of(
            new MarketNews(
                    "Strong economic growth",
                    "Global economy shows strong growth," +
                            " and experts at wall street suggest the market will countinue to rise",
                    "All",
                    new BigDecimal("0.08")
            ),
            new MarketNews(
                    "Intrest rate hike",
                    "Central bank raises intrest rates",
                    "All",
                    new BigDecimal("-0.1")
            ),
            new MarketNews(
                    "Global economic slowdown",
                    "Economic indicators suggest a slowdown in global growth",
                    "ALL",
                    new BigDecimal("-0.05")
            ),

            new MarketNews(
                    "Government stimulus package",
                    "Governments announce major stimulus to boost economy",
                    "ALL",
                    new BigDecimal("0.06")
            ),

            new MarketNews(
                    "Recession fears grow",
                    "Markets react to increasing fears of a global recession",
                    "ALL",
                    new BigDecimal("-0.08")
            ),

            new MarketNews(
                    "Strong employment data",
                    "New data shows strong job growth across multiple sectors",
                    "ALL",
                    new BigDecimal("0.04")
            ),
            new MarketNews(
                    "AI breakthrough announced",
                    "Major tech companies reveal new AI models with huge potential",
                    "Technology",
                    new BigDecimal("0.07")
            ),

            new MarketNews(
                    "Tech regulation crackdown",
                    "Governments introduce stricter regulations on big tech firms",
                    "Technology",
                    new BigDecimal("-0.06")
            ),

            new MarketNews(
                    "Cloud demand surges",
                    "Demand for cloud services rises sharply among enterprises",
                    "Technology",
                    new BigDecimal("0.05")
            ),

            new MarketNews(
                    "Massive data breach reported",
                    "Several major tech firms affected by large-scale data breach",
                    "Technology",
                    new BigDecimal("-0.09")
            ),
            new MarketNews(
                    "Oil prices spike",
                    "Oil prices surge due to supply shortages",
                    "Energy",
                    new BigDecimal("0.08")
            ),

            new MarketNews(
                    "Renewable energy boom",
                    "Investments in renewable energy hit record highs",
                    "Energy",
                    new BigDecimal("0.05")
            ),

            new MarketNews(
                    "Oil demand drops",
                    "Global demand for oil declines unexpectedly",
                    "Energy",
                    new BigDecimal("-0.07")
            ),

            new MarketNews(
                    "New oil reserves discovered",
                    "Large oil reserves discovered, increasing supply outlook",
                    "Energy",
                    new BigDecimal("-0.04")
            ),
            new MarketNews(
                    "Bank profits exceed expectations",
                    "Major banks report stronger than expected earnings",
                    "Finance",
                    new BigDecimal("0.05")
            ),

            new MarketNews(
                    "Credit crisis emerging",
                    "Concerns rise over tightening credit conditions",
                    "Finance",
                    new BigDecimal("-0.08")
            ),
            new MarketNews(
                    "Breakthrough drug approved",
                    "New drug receives approval after successful trials",
                    "Healthcare",
                    new BigDecimal("0.09")
            ),

            new MarketNews(
                    "Clinical trial failure",
                    "Major pharmaceutical company reports failed trial",
                    "Healthcare",
                    new BigDecimal("-0.07")
            )
    );

    public MarketNews getRandomNews() {
        if (random.nextInt(100) < 67) {
            return null;
        }
        return newsPool.get(random.nextInt(newsPool.size()));
    }
}