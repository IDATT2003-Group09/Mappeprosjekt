package edu.ntnu.iir.bidata.idatt2003.group09.io;

import edu.ntnu.iir.bidata.idatt2003.group09.base.Exchange;
import edu.ntnu.iir.bidata.idatt2003.group09.base.Player;

import java.io.Serializable;

public class GameState implements Serializable {

    private final Player player;
    private final Exchange exchange;

    public GameState(Player player, Exchange exchange) {
        this.player = player;
        this.exchange = exchange;
    }

    public Player getPlayer() {
        return player;
    }

    public Exchange getExchange() {
        return exchange;
    }
}