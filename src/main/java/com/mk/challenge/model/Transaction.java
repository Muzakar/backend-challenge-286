package com.mk.challenge.model;

import java.util.concurrent.atomic.AtomicInteger;

public class Transaction {

    private double amount;
    private long timestamp;
    private final AtomicInteger count = new AtomicInteger(1);

    public Transaction() {
    }

    public Transaction(final double amount, final long timestamp) {
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public double getAmount() {
        return amount;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getCount() {
        return count.get();
    }

    public void updateAmount(final double updateAmount) {
        this.amount = this.amount + updateAmount;
        count.incrementAndGet();
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "amount=" + amount +
                ", timestamp=" + timestamp +
                '}';
    }
}
