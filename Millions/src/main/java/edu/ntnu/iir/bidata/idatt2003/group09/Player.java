package edu.ntnu.iir.bidata.idatt2003.group09;

import java.math.BigDecimal;
import java.util.Objects;

public class Player {

    private String name;
    private BigDecimal startingMoney;
    private BigDecimal money;
    private Portfolio portfolio;
    private TransactionArchive transactionArchive;

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

    public String getName() {
        return name;
    }

    public BigDecimal getMoney() {
        return money;
    }



    public void addMoney (BigDecimal amount) {
        //
    }

    public void withdrawMoney (BigDecimal amount) {
        //
    }



    public Portfolio getPortfolio() {
        return portfolio;
    }

    public TransactionArchive getTransactionArchive() {
        return transactionArchive;
    }
}