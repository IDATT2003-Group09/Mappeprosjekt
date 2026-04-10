package edu.ntnu.iir.bidata.idatt2003.group09.base.Game;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class GameProgress {

    private int currentLevel = 1;
    private final BigDecimal baseRequirement;
    private int currentWeek = 0;
    private final int weeksPerQuarter = 13;

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

    public void nextWeek() {
        currentWeek++;
    }

    public boolean isQuarterComplete() {
        return currentWeek % weeksPerQuarter == 0;
    }

    public int getCurrentWeek() {
        return currentWeek;
    }

    public boolean meetsRequirement(BigDecimal netWorth, BigDecimal startingMoney) {

        BigDecimal growth = netWorth.subtract(startingMoney)
                .divide(startingMoney, 4, RoundingMode.HALF_UP);

        return growth.compareTo(getCurrentLevel().getRequiredGrowth()) >= 0;
    }
}
