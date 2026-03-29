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
            )
            //write down many more news events as we go on with the project
    );

    public MarketNews getRandomNews() {
        if (random.nextInt(100) < 67) {
            return null;
        }
        return newsPool.get(random.nextInt(newsPool.size()));
    }
}