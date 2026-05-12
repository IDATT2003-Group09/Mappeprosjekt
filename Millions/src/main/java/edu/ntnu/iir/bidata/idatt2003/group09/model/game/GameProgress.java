package edu.ntnu.iir.bidata.idatt2003.group09.model.game;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.io.Serializable;
/**
 * class responible for level generation and requirement checking
 */
public class GameProgress implements Serializable {


    private int checkpointLevel = 1;
    private BigDecimal baseRequirement;
    private int currentWeek = 0;
    private final int weeksPerQuarter = 13;
    private int checkpointWeek = 13;
    private int lastCalculatedLevel = 1;
    private BigDecimal currentTarget;

    /**
     * sets the starting percentage the player must meet every quarter and their startingmoney
     * @param baseRequirement percentage increase per q
     * @param startingMoney the money you start with
     */
    public GameProgress(BigDecimal baseRequirement, BigDecimal startingMoney) {
        this(baseRequirement, startingMoney, 0);
    }

    /**
     * calculates gameprogress for input values
     * @param baseRequirement percentage increse per q
     * @param startingMoney starting money
     * @param currentWeek what week you are in
     */
    public GameProgress(BigDecimal baseRequirement, BigDecimal startingMoney, int currentWeek) {
        this.baseRequirement = baseRequirement;
        this.currentWeek = Math.max(0, currentWeek);

        int completedQuarters = this.currentWeek / weeksPerQuarter;
        this.checkpointLevel = completedQuarters + 1;
        this.checkpointWeek = this.checkpointLevel * weeksPerQuarter;
        this.lastCalculatedLevel = this.checkpointLevel;
        this.currentTarget = startingMoney.multiply(
            BigDecimal.ONE.add(baseRequirement).pow(this.checkpointLevel)
        );
    }

    public BigDecimal getBaseRequirement() {
        return baseRequirement;
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

    public int getCheckpointLevel() {
        return checkpointLevel;
    }

    public boolean meetsRequirement(BigDecimal netWorth) {
        return netWorth.compareTo(currentTarget) >= 0;
    }

    public int getWeeksUntilDeadline() {
        return Math.max(0, checkpointWeek - currentWeek);
    }

    /**
     * move the goalpost
     */
    public void advanceCheckpoint() {
        baseRequirement = baseRequirement.multiply(BigDecimal.valueOf(2));
        currentTarget = currentTarget.multiply(
                BigDecimal.ONE.add(baseRequirement)
        );
        checkpointLevel++;
        checkpointWeek += weeksPerQuarter;
    }

    public BigDecimal getCurrentTarget() {
        return currentTarget;
    }
    public int getLastCalculatedLevel() {
        return lastCalculatedLevel;
    }

    public void setLastCalculatedLevel(int level) {
        this.lastCalculatedLevel = level;
    }
}
