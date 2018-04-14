package com.mk.challenge.service;

import com.mk.challenge.dao.TransactionDAO;
import com.mk.challenge.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@Component
public class TransactionService {

    private final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    @Autowired
    private TransactionDAO transactionDAO;

    @Autowired
    private int expiryTransactionTimeInSecs;

    public HttpStatus processTransaction(final Transaction transaction) {
        if (isTransactionValid(transaction)) {
            transactionDAO.updateTransaction(transaction);
            logger.info("Transaction is now updated - " + transaction);
            return CREATED;
        }
        logger.info("Transaction is invalid. Timestamp is either greater than current time or more than " + expiryTransactionTimeInSecs + " seconds old");
        return NO_CONTENT;
    }

    private boolean isTransactionValid(Transaction transaction) {
        return System.currentTimeMillis() > transaction.getTimestamp() && System.currentTimeMillis() - transaction.getTimestamp() <= expiryTransactionTimeInSecs * 1000;
    }

}
