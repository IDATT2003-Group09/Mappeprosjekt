package edu.ntnu.iir.bidata.idatt2003.group09.io;

import java.io.Serializable;
import java.math.BigDecimal;

import edu.ntnu.iir.bidata.idatt2003.group09.model.Exchange;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Player;
import edu.ntnu.iir.bidata.idatt2003.group09.model.game.GameProgress;

/**
 * Represents the serializable state of the game for saving and loading.
 */
public class GameState implements Serializable {

    private static final long serialVersionUID = 6427867207189561378L;

    private final Player player;
    private final Exchange exchange;
    private final BigDecimal netWorth;
    private final int week;
    private final String difficulty;
    private final boolean lost;
    private final GameProgress gameProgress;
    private final String initialCsvPath;

    /**
     * Constructs a GameState with the given parameters.
     *
     * @param player
     * @param exchange
     * @param netWorth
     * @param week
     * @param difficulty
     * @param lost
     * @param gameProgress
     */
    public GameState(Player player, Exchange exchange, BigDecimal netWorth, int week, String difficulty, boolean lost, GameProgress gameProgress) {
        this(player, exchange, netWorth, week, difficulty, lost, gameProgress, null);
    }

    /**
     * Constructs a GameState with an optional path to the initial generated CSV
     * that defines the exchange's starting prices/sectors.
     */
    public GameState(Player player, Exchange exchange, BigDecimal netWorth, int week, String difficulty, boolean lost, GameProgress gameProgress, String initialCsvPath) {
        this.player = player;
        this.exchange = exchange;
        this.netWorth = netWorth;
        this.week = week;
        this.difficulty = difficulty;
        this.lost = lost;
        this.gameProgress = gameProgress;
        this.initialCsvPath = initialCsvPath;
    }

    /**
     * Gets the saved Player.
     *
     * @return the Player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the saved game difficulty.
     *
     * @return the difficulty string
     */
    public String getDifficulty() {
        return difficulty;
    }

    /**
     * Gets the saved Exchange.
     *
     * @return the Exchange
     */
    public Exchange getExchange() {
        return exchange;
    }

    /**
     * Gets the saved net worth.
     *
     * @return the net worth
     */
    public BigDecimal getNetWorth() {
        return netWorth;
    }

    /**
     * Gets the saved week number.
     *
     * @return the week number
     */
    public int getWeek() {
        return week;
    }

    /**
     * gets if game has been lost
     * 
     * @return the lso state
     */
    public boolean isLost(){
        return lost;
    }

    /**
     * returns the gameProgress
     * 
     * @return the gameprogress object
     */
    public GameProgress getProgress(){
        return gameProgress;
    }

    /**
     * Path to the CSV that was generated when this save was created, if any.
     *
     * @return absolute path or null
     */
    public String getInitialCsvPath() {
        return initialCsvPath;
    }
}