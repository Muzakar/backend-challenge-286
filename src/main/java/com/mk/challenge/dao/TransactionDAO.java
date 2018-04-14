package com.mk.challenge.dao;

import com.google.common.collect.Multimap;
import com.mk.challenge.expiry.DelayElement;
import com.mk.challenge.model.Transaction;
import com.mk.challenge.util.StatisticsUpdaterSignal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.concurrent.BlockingQueue;

@Component
public class TransactionDAO {

    private final Logger logger = LoggerFactory.getLogger(TransactionDAO.class);

    @Autowired
    private Multimap<Long, Transaction> transactionMap;

    @Autowired
    private StatisticsUpdaterSignal statisticsUpdaterSignal;

    @Autowired
    private BlockingQueue<DelayElement> expiryTransactionQueue;

    @Autowired
    private int expiryTransactionTimeInSecs;

    public void updateTransaction(Transaction transaction) {
        transactionMap.put(transaction.getTimestamp(), transaction);
        logger.info("Transaction updated in transaction map - " + transaction);
        DelayElement delayElement = new DelayElement(transaction.getTimestamp(), expiryTransactionTimeInSecs);
        expiryTransactionQueue.add(delayElement);
        logger.info("Delay element added to expiration queue - " + delayElement + " having expiry time of - " + expiryTransactionTimeInSecs + " secs.");
        statisticsUpdaterSignal.sendSignal();
    }

    public void removeTransaction(final long transactionTimeStamp) {
        transactionMap.removeAll(transactionTimeStamp);
    }

    public Collection<Transaction> getAllTransactions() {
        return transactionMap.values();
    }

}
