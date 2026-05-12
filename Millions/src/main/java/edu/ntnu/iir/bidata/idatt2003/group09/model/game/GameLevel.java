package edu.ntnu.iir.bidata.idatt2003.group09.model.game;

import java.math.BigDecimal;

public class GameLevel {

    private final int levelNumber;
    private final BigDecimal baseRequirement;

    public GameLevel(int levelNumber, BigDecimal baseRequirement) {
        this.levelNumber = levelNumber;
        this.baseRequirement = baseRequirement;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public BigDecimal getBaseRequirment() {
        return baseRequirement;
    }

    public BigDecimal getRequiredGrowth() {
        return baseRequirement.multiply(BigDecimal.valueOf(levelNumber*levelNumber/2));
    }
}
