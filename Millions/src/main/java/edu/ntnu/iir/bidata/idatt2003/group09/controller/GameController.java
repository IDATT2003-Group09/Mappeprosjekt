package edu.ntnu.iir.bidata.idatt2003.group09.controller;

import edu.ntnu.iir.bidata.idatt2003.group09.base.*;

import java.math.BigDecimal;

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
        exchange.Advance();
    }

    //player info
    public BigDecimal getMoney() {
        return player.getMoney();
    }

    public BigDecimal getNetworth() {
        return player.getNetWorth();
    }

    //get title metode

}
