package edu.ntnu.iir.bidata.idatt2003.group09.io;

import java.io.Serializable;
import java.math.BigDecimal;

import edu.ntnu.iir.bidata.idatt2003.group09.model.Exchange;
import edu.ntnu.iir.bidata.idatt2003.group09.model.Player;

/**
 * Represents the serializable state of the game for saving and loading.
 */
public class GameState implements Serializable {

    private final Player player;
    private final Exchange exchange;
    private final BigDecimal netWorth;
    private final int week;
    private final String difficulty;
    private final boolean lost;

    /**
     * Constructs a GameState with the given parameters.
     *
     * @param player
     * @param exchange
     * @param netWorth
     * @param week
     * @param difficulty
     * @param lost
     */
    public GameState(Player player, Exchange exchange, BigDecimal netWorth, int week, String difficulty, boolean lost) {
        this.player = player;
        this.exchange = exchange;
        this.netWorth = netWorth;
        this.week = week;
        this.difficulty = difficulty;
        this.lost = lost;
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
    public boolean getLost(){
        return lost;
    }
}