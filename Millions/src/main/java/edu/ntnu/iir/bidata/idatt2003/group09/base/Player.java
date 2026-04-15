package edu.ntnu.iir.bidata.idatt2003.group09.base;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import edu.ntnu.iir.bidata.idatt2003.group09.base.transaction.TransactionArchive;

/**
 * Represents a player in the game.
 * The player has a name, starting amount, current money balance, a portfolio,
 * and an archive storing completed transactions
 */
public class Player implements Serializable {

    private final String name;
    private final BigDecimal startingMoney;
    private BigDecimal money;
    private BigDecimal lastWeekNetWorth;
    private final Portfolio portfolio;
    private final TransactionArchive transactionArchive;
    private final String difficulty;

    /**
     * Creates a new player
     * @param name the name of the player
     * @param startingMoney the amount of money the player starts with
     * @param difficulty the difficulty level of the player
     * @throws NullPointerException if name or starting money is null
     * @throws IllegalArgumentException if name is blank or starting money is less
     *                                  or equal to zero
     */
    public Player(String name, BigDecimal startingMoney, String difficulty) {
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        if (name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }

        this.startingMoney = Objects.requireNonNull(startingMoney,
                "Starting money cannot be null");
        if (startingMoney.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Starting money cant be negative or zero");
        }

        this.difficulty = Objects.requireNonNull(difficulty, "Difficulty cannot be null");

        this.money = this.startingMoney;
        this.portfolio = new Portfolio();
        this.transactionArchive = new TransactionArchive();
        this.lastWeekNetWorth = this.startingMoney;

        // Add initial net worth to portfolio values for graph (week 1)
        this.portfolio.addNetWorthValue(getNetWorth());
    }


    /**
     * Get method for the name of the player
     *
     * @return the players name
     */
    public String getName() {
        return name;
    }

    /**
     * Get method for starting money
     *
     * @return the starting money
     */
    public BigDecimal getStartingMoney() {
        return startingMoney;
    }

    /**
     * Get method for the current money balance of the player
     *
     * @return the current amount of money
     */
    public BigDecimal getMoney() {
        return money;
    }

    /**
     * Get method for last weeks net worth
     *
     * @return last weeks net worth
     */
    public BigDecimal getLastWeekNetWorth() {
        return lastWeekNetWorth;
    }

    /**
     * Adds money to the player's balance
     *
     * @param amount the amount of money to add
     * @throws NullPointerException if the amount is null
     * @throws IllegalArgumentException if the amount is negative or equal to zero
     */
    public void addMoney (BigDecimal amount) {
        Objects.requireNonNull(amount, "Amount cannot be null");
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
        money = money.add(amount);
    }

    /**
     * Withdraws money from the player's balance
     *
     * @param amount the amount of money to withdraw
     * @throws NullPointerException if the amount is null
     * @throws IllegalArgumentException if the amount is negative or equal to zero
     * @throws IllegalStateException if the player does not have sufficient funds
     */
    public void withdrawMoney (BigDecimal amount) {
        Objects.requireNonNull(amount, "Amount cannot be null");
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
        if (money.compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient funds");
        }
        money = money.subtract(amount);
    }

    /**
     * Get method for the player's portfolio
     *
     * @return the portfolio which contains the player's owned shares
     */
    public Portfolio getPortfolio() {
        return portfolio;
    }

    /**
     * Get method for the player's transaction archive
     *
     * @return the transaction archive
     */
    public TransactionArchive getTransactionArchive() {
        return transactionArchive;
    }

    /**
     * Calculates the players total networth including the current money
     *
     * @return the total networth of the player
     */
    public BigDecimal getNetWorth() {
        return money.add(portfolio.getNetWorth());
    }

    public String getDifficulty() {
        return difficulty;
    }

    /**
     * Method to get the status of a player
     * The status is based of how many weeks a player has played, and how much money they have made
     *
     * @param weeksPlayed how many weeks this player has played
     * @return Novice as default,
     * investor if the player has played for 10 or more weeks and made a 20% gain on his portfolio,
     * speculator if the player has played for more than 20 weeks and made a 100% gain on his portfolio
     */
    public PlayerStatus getStatus(int weeksPlayed) {
        if (weeksPlayed < 0) {
            throw new IllegalArgumentException("Weeks played cannot be negative");
        }

        BigDecimal percentageChange = getNetWorth()
                .subtract(startingMoney)
                .divide(startingMoney, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));

        if (percentageChange.compareTo(BigDecimal.valueOf(100)) >= 0 && weeksPlayed >= 20) {
            return PlayerStatus.SPECULATOR;
        }

        if (percentageChange.compareTo(BigDecimal.valueOf(20)) >= 0 && weeksPlayed >= 10) {
            return PlayerStatus.INVESTOR;
        }

        return PlayerStatus.NOVICE;
    }

    public void setLastWeekNetWorth(BigDecimal value) {
        this.lastWeekNetWorth = Objects.requireNonNull(value, "Net worth cannot be null");
    }
}