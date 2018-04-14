package com.mk.challenge.model;

public class Statistics {
    public double sum;
    public double average;
    public double maximum;
    public double minimum;
    public int count;

    @Override
    public String toString() {
        return "Statistics{" +
                "sum=" + sum +
                ", average=" + average +
                ", maximum=" + maximum +
                ", minimum=" + minimum +
                ", count=" + count +
                '}';
    }
}
