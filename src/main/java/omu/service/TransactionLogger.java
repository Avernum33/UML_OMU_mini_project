package omu.service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import omu.domain.Transaction;

public final class TransactionLogger {
    private final CopyOnWriteArrayList<Transaction> transactions = new CopyOnWriteArrayList<>();

    public void log(Transaction transaction) {
        transactions.add(transaction);
    }

    public List<Transaction> entries() {
        return List.copyOf(transactions);
    }
}

