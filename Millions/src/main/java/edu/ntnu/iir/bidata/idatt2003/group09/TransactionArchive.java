package edu.ntnu.iir.bidata.idatt2003.group09;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class TransactionArchive {
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
        List<Transaction> result = new ArrayList<>();
        for (Transaction t : transactions) {
            if (t.getWeek() == week) {
                result.add(t);
            }
        }
        return Collections.unmodifiableList(result);
    }

    //public List<Purchase> getPurchases(int week) {
        //fix
    //}

    //public List<Sale> getSales(int week) {
        //fix
    //}

}