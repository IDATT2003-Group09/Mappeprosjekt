package edu.ntnu.iir.bidata.idatt2003.group09.base.Game;

import java.math.BigDecimal;

public class GameProgress {

    private int currentLevel = 1;
    private final BigDecimal baseRequirement;

    public GameProgress(BigDecimal baseRequirement) {
        this.baseRequirement = baseRequirement;
    }

    public GameLevel getCurrentLevel() {
        return new GameLevel(currentLevel, baseRequirement);
    }

    public void advanceLevel() {
        currentLevel++;
    }

    public int getCurrentLevelNumber() {
        return currentLevel;
    }
}
