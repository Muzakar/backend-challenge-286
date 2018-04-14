package com.mk.challenge.expiry;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class DelayElement implements Delayed {

    private final long transactionTimestamp;
    private final long expiryTime;

    public DelayElement(final long transactionTimestamp, final int expiryTime) {
        this.transactionTimestamp = transactionTimestamp;
        this.expiryTime = transactionTimestamp + (expiryTime * 1000);
    }

    public long getTransactionTimestamp() {
        return transactionTimestamp;
    }

    public long getExpiryTime() {
        return expiryTime;
    }

    @Override
    public long getDelay(final TimeUnit unit) {
        long diff = expiryTime - System.currentTimeMillis();
        return unit.convert(diff, TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        if (this.expiryTime < ((DelayElement) o).expiryTime) {
            return -1;
        }
        if (this.expiryTime > ((DelayElement) o).expiryTime) {
            return 1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "DelayElement{" +
                "transactionTimestamp=" + transactionTimestamp +
                ", expiryTime=" + expiryTime +
                '}';
    }
}
