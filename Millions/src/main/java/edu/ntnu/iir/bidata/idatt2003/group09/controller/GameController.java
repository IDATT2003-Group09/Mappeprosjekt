package edu.ntnu.iir.bidata.idatt2003.group09.controller;

import edu.ntnu.iir.bidata.idatt2003.group09.base.*;
import edu.ntnu.iir.bidata.idatt2003.group09.base.Game.GameProgress;
import edu.ntnu.iir.bidata.idatt2003.group09.base.news.NewsPaper;
import edu.ntnu.iir.bidata.idatt2003.group09.io.GameState;
import edu.ntnu.iir.bidata.idatt2003.group09.io.SaveManager;

import java.math.BigDecimal;
import java.util.List;

//Extend this class anywhere if needed for more game logic,
//maybe split into multiple classes later if needed

public class GameController {

    private final Exchange exchange;
    private final Player player;
    private final String saveFileName;
    private final GameProgress progress;

    private Runnable onGameOver;

    public GameController(Exchange exchange, Player player) {
        this(exchange, player, null);
    }

    public GameController(Exchange exchange, Player player, String saveFileName) {
        this.exchange = exchange;
        this.player = player;
        this.saveFileName = saveFileName;
        this.progress = new GameProgress(new BigDecimal("0.10"));
    }

    public void setOnGameOver(Runnable onGameOver) {
        this.onGameOver = onGameOver;
    }

    //game flow
    public void nextWeek() {
        player.setLastWeekNetWorth(player.getNetWorth());

        progress.nextWeek();

        int newLevel = progress.calculateLevel(
                player.getNetWorth(),
                player.getStartingMoney()
        );

        if (newLevel > progress.getLastCalculatedLevel()) {
            progress.advanceCheckpoint(
                    player.getNetWorth(),
                    player.getStartingMoney()
            );
            progress.setLastCalculatedLevel(newLevel);
        }

        if (progress.isQuarterComplete()) {

            boolean success = progress.meetsRequirement(
                    player.getNetWorth(),
                    player.getStartingMoney());

            if (!success) {
                if (onGameOver != null) {
                    onGameOver.run();
                }
                return;
            }

            progress.advanceCheckpoint(
                    player.getNetWorth(),
                    player.getStartingMoney()
            );
        }

        exchange.advance();
        saveGame();
    }

    public GameProgress getProgress() {
        return progress;
    }

    public void saveGame() {
        SaveManager.save(new GameState(player, exchange), saveFileName);
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

    public BigDecimal getLastWeekNetWorth() {
        return player.getLastWeekNetWorth();
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

    public NewsPaper getPendingNewsPaper() {
        return exchange.getPendingNewsPaper();
    }
}
