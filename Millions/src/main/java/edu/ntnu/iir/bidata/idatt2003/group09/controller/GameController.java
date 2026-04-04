package edu.ntnu.iir.bidata.idatt2003.group09.controller;

import edu.ntnu.iir.bidata.idatt2003.group09.base.*;

import java.math.BigDecimal;
import java.util.List;

//Extend this class anywhere if needed for more game logic,
//maybe split into multiple classes later if needed

public class GameController {

    private final Exchange exchange;
    private final Player player;

    public GameController(Exchange exchange, Player player) {
        this.exchange = exchange;
        this.player = player;
    }

    //game flow
    public void nextWeek() {
        exchange.advance();
    }

    public int getWeek() {
        return exchange.getWeek();
    }

    //player info
    public BigDecimal getMoney() {
        return player.getMoney();
    }

    public BigDecimal getNetWorth() {
        return player.getNetWorth();
    }

    public PlayerStatus getStatus() {
        return player.getStatus(exchange.getWeek());
    }

    public Player getPlayer() {
        return player;
    }

    //stock info
    public List<Stock> getAllStocks() {
        return exchange.getStockMap().values().stream().toList();
    }

    public Stock getStock(String symbol) {
        return exchange.getStock(symbol);
    }

    public List<Stock> getGainers() {
        return exchange.getGainers();
    }

    public List<Stock> getLosers() {
        return exchange.getLosers();
    }

    //portfolio
    public Portfolio getPortfolio() {
        return player.getPortfolio();
    }

    //trading
    public void buy(String symbol, BigDecimal quantity) {
        exchange.buy(symbol, player, quantity);
    }

    public void sell(Share share) {
        exchange.sell(share, player);
    }

    public String getLatestNews() {
        MarketNews news = exchange.getPendingNews();
        return news != null ? news.getHeadline() : "No news";
    }
}
