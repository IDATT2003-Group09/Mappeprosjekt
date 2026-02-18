package edu.ntnu.iir.bidata.idatt2003.group09;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Represents a player in the game.
 * The player has a name, starting amount, current money balance, a portfolio,
 * and an archive storing completed transactions
 */
public class Player {

    private final String name;
    private final BigDecimal startingMoney;
    private BigDecimal money;
    private final Portfolio portfolio;
    private final TransactionArchive transactionArchive;

    /**
     * Creates a new player
     * @param name the name of the player
     * @param startingMoney the amount of money the player starts with
     * @throws NullPointerException if name or starting money is null
     * @throws IllegalArgumentException if name is blank or starting money is less
     *                                  or equal to zero
     */
    public Player(String name, BigDecimal startingMoney) {
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        if (name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }

        this.startingMoney = Objects.requireNonNull(startingMoney,
                "Starting money cannot be null");
        if (startingMoney.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Starting money cant be negative or zero");
        }

        this.money = this.startingMoney;
        this.portfolio = new Portfolio();
        this.transactionArchive = new TransactionArchive();
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
     * Get method for the current money balance of the player
     *
     * @return the current amount of money
     */
    public BigDecimal getMoney() {
        return money;
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
}