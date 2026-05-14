package edu.ntnu.iir.bidata.idatt2003.group09.controller;

import edu.ntnu.iir.bidata.idatt2003.group09.io.GameState;
import edu.ntnu.iir.bidata.idatt2003.group09.io.SaveManager;
import edu.ntnu.iir.bidata.idatt2003.group09.model.*;
import edu.ntnu.iir.bidata.idatt2003.group09.model.game.GameProgress;
import edu.ntnu.iir.bidata.idatt2003.group09.model.news.NewsPaper;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

/**
 * Main controller for managing the game state, player, exchange, and game progress.
 */
public class GameController {

    /**
     * Record representing the result of advancing a week in the game.
     * Indicates if the game is over, if a quarter advanced, and checkpoint/target info.
     */
    public record WeekAdvanceResult(
        boolean gameOver,
        boolean quarterAdvanced,
        int completedQuarter,
        int unlockedQuarter,
        BigDecimal clearedNetWorth,
        BigDecimal clearedTarget,
        BigDecimal nextTarget
    ) {
        public static WeekAdvanceResult noChange() {
            return new WeekAdvanceResult(false, false, 0, 0, null, null, null);
        }

        public static WeekAdvanceResult gameOverResult() {
            return new WeekAdvanceResult(true, false, 0, 0, null, null, null);
        }
    }

    private final Exchange exchange;
    private final Player player;
    private final String saveFileName;
    private final GameProgress progress;
    private boolean lost = false;
    private Runnable onGameOver;
    private static final BigDecimal baseRequirement = new BigDecimal("0.04");


    /**
     * Constructs a GameController with the given exchange and player.
     *
     * @param exchange 
     * @param player 
     */
    public GameController(Exchange exchange, Player player) {
        this(exchange, player, null);
    }


    /**
     * Constructs a GameController with the given exchange, player, and save file name.
     *
     * @param exchange
     * @param player 
     * @param saveFileName
     */
    public GameController(Exchange exchange, Player player, String saveFileName, GameProgress progress) {
        this.exchange = exchange;
        this.player = player;
        this.saveFileName = saveFileName;
        this.progress = progress != null
            ? progress
            : new GameProgress(baseRequirement, player.getStartingMoney(), exchange.getWeek());
    }
    public GameController(Exchange exchange, Player player, String saveFileName) {
        this(exchange, player, saveFileName, null);
    }

    /**
     * Sets a callback to be run when the game is over.
     *
     * @param onGameOver the Runnable to execute on game over
     */
    public void setOnGameOver(Runnable onGameOver) {
        this.onGameOver = onGameOver;
    }

    /**
     * Advances the game by one week, updating player and progress state.
     * Handles quarter advancement, requirements, and game over logic.
     *
     * @return the result of the week advancement
     */
    public WeekAdvanceResult nextWeek() {
        if (lost) {
            return WeekAdvanceResult.gameOverResult();
        }
        player.setLastWeekNetWorth(player.getNetWorth());
        player.getPortfolio().addNetWorthValue(player.getNetWorth());
        progress.nextWeek();

        boolean deadlineReached = progress.isQuarterComplete();
        boolean requirementMet = progress.meetsRequirement(player.getNetWorth());
        WeekAdvanceResult result = WeekAdvanceResult.noChange();

        if (deadlineReached) {
            if (requirementMet) {
                int completedQuarter = progress.getCheckpointLevel();
                BigDecimal clearedNetWorth = player.getNetWorth();
                BigDecimal clearedTarget = progress.getCurrentTarget();
                progress.advanceCheckpoint();
                result = new WeekAdvanceResult(
                    false,
                    true,
                    completedQuarter,
                    progress.getCheckpointLevel(),
                    clearedNetWorth,
                    clearedTarget,
                    progress.getCurrentTarget()
                );
            } else {
                lost = true;
                saveGame();
                if (onGameOver != null) {
                    onGameOver.run();
                }
                return WeekAdvanceResult.gameOverResult();
            }
        }

        exchange.advance();
        saveGame();
        return result;
    }

    /**
     * Gets the current game progress.
     *
     * @return the GameProgress instance
     */
    public GameProgress getProgress() {
        return progress;
    }

    /**
     * Saves the current game state to file.
     */
    public void saveGame() {
        SaveManager.save(new GameState(player, exchange, player.getNetWorth(), exchange.getWeek(), player.getDifficulty(),lost, progress), saveFileName);
    }
    
    public boolean isLost(){
        return lost;
    }

    /**
     * Gets the current week number.
     *
     * @return the current week
     */
    public int getWeek() {
        return exchange.getWeek();
    }

    /**
     * Gets the player's current cash balance.
     *
     * @return the player's money
     */
    public BigDecimal getMoney() {
        return player.getMoney();
    }

    /**
     * Gets the player's current net worth.
     *
     * @return the player's net worth
     */
    public BigDecimal getNetWorth() {
        return player.getNetWorth();
    }

    /**
     * Gets the player's net worth from the previous week.
     *
     * @return the last week's net worth
     */
    public BigDecimal getLastWeekNetWorth() {
        return player.getLastWeekNetWorth();
    }

    /**
     * Gets the player's status for the current week.
     *
     * @return the PlayerStatus
     */
    public PlayerStatus getStatus() {
        return player.getStatus(exchange.getWeek());
    }

    /**
     * Gets the player instance.
     *
     * @return the Player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets a list of all stocks in the exchange.
     *
     * @return a list of all Stock objects
     */
    public List<Stock> getAllStocks() {
        return exchange.getStockMap().values().stream().toList();
    }

    /**
     * Gets a stock by its symbol.
     *
     * @param symbol the stock symbol
     * @return the Stock object, or null if not found
     */
    public Stock getStock(String symbol) {
        return exchange.getStock(symbol);
    }

    /**
     * Gets a list of stocks that have gained value.
     *
     * @return a list of gaining Stock objects
     */
    public List<Stock> getGainers() {
        return exchange.getGainers();
    }

    /**
     * Gets a list of stocks that have lost value.
     *
     * @return a list of losing Stock objects
     */
    public List<Stock> getLosers() {
        return exchange.getLosers();
    }

    /**
     * Gets the player's portfolio.
     *
     * @return the Portfolio
     */
    public Portfolio getPortfolio() {
        return player.getPortfolio();
    }

    /**
     * Gets the latest market news headline, or "No news" if none is available.
     *
     * @return the latest news headline
     */
    public String getLatestNews() {
        MarketNews news = exchange.getPendingNews();
        return news != null ? news.getHeadline() : "No news";
    }

    /**
     * Gets the pending newspaper with market news.
     *
     * @return the NewsPaper object
     */
    public NewsPaper getPendingNewsPaper() {
        return exchange.getPendingNewsPaper();
    }
    
    /**
     * Gets the exchange instance.
     *
     * @return the Exchange
     */
    public Exchange getExchange() {
        return exchange;
    }

    /**
     * Sells all shares in the player's portfolio and credits their value to cash.
     * Also saves the game state after selling.
     */
    public void sellAllShares() {
        Portfolio portfolio = getPortfolio();
        List<Share> sharesToSell = new ArrayList<>(portfolio.getShares());
        for (Share share : sharesToSell) {
            BigDecimal value = share.getStock().getSalesPrice().multiply(share.getQuantity());
            player.addMoney(value);
            portfolio.removeShare(share);
        }
        saveGame();
    }
}
