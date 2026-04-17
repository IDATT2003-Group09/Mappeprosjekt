package edu.ntnu.iir.bidata.idatt2003.group09.model.Game;

import java.math.BigDecimal;

public class GameLevel {

    private final int levelNumber;
    private final BigDecimal baseRequirment;

    public GameLevel(int levelNumber, BigDecimal baseRequirment) {
        this.levelNumber = levelNumber;
        this.baseRequirment = baseRequirment;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public BigDecimal getBaseRequirment() {
        return baseRequirment;
    }

    public BigDecimal getRequiredGrowth() {
        return baseRequirment.multiply(BigDecimal.valueOf(levelNumber));
    }
}
