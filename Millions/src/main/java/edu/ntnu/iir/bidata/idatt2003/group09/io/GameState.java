package edu.ntnu.iir.bidata.idatt2003.group09.io;

import edu.ntnu.iir.bidata.idatt2003.group09.base.Exchange;
import edu.ntnu.iir.bidata.idatt2003.group09.base.Player;

import java.io.Serializable;
import java.math.BigDecimal;

public class GameState implements Serializable {

    private final Player player;
    private final Exchange exchange;
    private final BigDecimal netWorth;
    private final int week;
    private final String difficulty;

    public GameState(Player player, Exchange exchange, BigDecimal netWorth, int week, String difficulty) {
        this.player = player;
        this.exchange = exchange;
        this.netWorth = netWorth;
        this.week = week;
        this.difficulty = difficulty;
    }

    public Player getPlayer() {
        return player;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public Exchange getExchange() {
        return exchange;
    }

    public BigDecimal getNetWorth() {
        return netWorth;
    }

    public int getWeek() {
        return week;
    }
}