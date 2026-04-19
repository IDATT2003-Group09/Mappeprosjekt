package edu.ntnu.iir.bidata.idatt2003.group09.model.transaction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class TransactionArchive implements Serializable {
    private final List<Transaction> transactions;

    public TransactionArchive() {
        this.transactions = new ArrayList<>();
    }

    public boolean add(Transaction transaction) {
        Objects.requireNonNull(transaction, "transaction cannot be null");
        return transactions.add(transaction);
    }

    public boolean isEmpty() {
        return transactions.isEmpty();
    }

    public List<Transaction> getTransactions(int week) {
        validateWeek(week);

        List<Transaction> result = new ArrayList<>();
        for (Transaction t : transactions) {
            if (t.getWeek() == week) {
                result.add(t);
            }
        }
        return Collections.unmodifiableList(result);
    }

    public List<Purchase> getPurchases(int week) {
        validateWeek(week);

        List<Purchase> result = new ArrayList<>();
        for (Transaction t : transactions) {
            if (t.getWeek() == week && t instanceof Purchase) {
                result.add((Purchase) t);
            }
        }
    return Collections.unmodifiableList(result);
    }

    public List<Sale> getSales(int week) {
        validateWeek(week);

        List<Sale> result = new ArrayList<>();
        for (Transaction t : transactions) {
            if (t.getWeek() == week && t instanceof Sale) {
                result.add((Sale) t);
            }
        }
        return Collections.unmodifiableList(result);
    }

    public int countDistinctWeeks() {
        List<Integer> weeks = new ArrayList<>();

        for (Transaction t : transactions) {
            int week = t.getWeek();
            if (!weeks.contains(week)) {
                weeks.add(week);
            }
        }
        return weeks.size();
    }

    public List<Transaction> getAllTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    private static void validateWeek(int week) {
        if (week < 1) {
            throw new IllegalArgumentException("Week must be greater than 0," +
                    " the week was: " + week);
        }
    }
}