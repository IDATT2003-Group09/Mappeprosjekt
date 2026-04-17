
package edu.ntnu.iir.bidata.idatt2003.group09.model.Game;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class GameProgress {

    public int getCheckpointLevel() {
        return checkpointLevel;
    }

    private int checkpointLevel = 1;
    private final BigDecimal baseRequirement;
    private int currentWeek = 0;
    private final int weeksPerQuarter = 13;

    private int checkpointWeek = 13;
    private int lastCalculatedLevel = 1;
    private BigDecimal currentTarget;

    public GameProgress(BigDecimal baseRequirement, BigDecimal startingMoney) {
        this.baseRequirement = baseRequirement;
        this.currentTarget = startingMoney.multiply(BigDecimal.ONE.add(baseRequirement));
    }

    public BigDecimal getBaseRequirement() {
        return baseRequirement;
    }

    public int getCurrentLevelNumber(BigDecimal netWorth, BigDecimal startingMoney) {
        return calculateLevel(netWorth, startingMoney);
    }

    public void nextWeek() {
        currentWeek++;
    }

    public boolean isQuarterComplete() {
        return currentWeek >= checkpointWeek;
    }

    public int getCurrentWeek() {
        return currentWeek;
    }

    public boolean meetsRequirement(BigDecimal netWorth) {
        return netWorth.compareTo(currentTarget) >= 0;
    }

    public int getWeeksUntilDeadline() {
        return Math.max(0, checkpointWeek - currentWeek);
    }

    public void advanceCheckpoint() {

        currentTarget = currentTarget.multiply(
                BigDecimal.ONE.add(baseRequirement)
        );

        checkpointWeek += weeksPerQuarter;
    }

    public BigDecimal getCurrentTarget() {
        return currentTarget;
    }

    public int calculateLevel(BigDecimal netWorth, BigDecimal startingMoney) {

        BigDecimal growth = netWorth.subtract(startingMoney)
                .divide(startingMoney, 4, RoundingMode.HALF_UP);

        if (growth.compareTo(BigDecimal.ZERO) <= 0) {
            return 1;
        }

        return growth.divide(baseRequirement, 0, RoundingMode.DOWN).intValue() + 1;
    }

    public int getLastCalculatedLevel() {
        return lastCalculatedLevel;
    }

    public void setLastCalculatedLevel(int level) {
        this.lastCalculatedLevel = level;
    }
}
