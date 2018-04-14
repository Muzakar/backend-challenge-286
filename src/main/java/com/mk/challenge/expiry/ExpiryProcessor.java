package com.mk.challenge.expiry;

import com.mk.challenge.dao.TransactionDAO;
import com.mk.challenge.util.StatisticsUpdaterSignal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

@Component
public class ExpiryProcessor implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(ExpiryProcessor.class);

    @Autowired
    private BlockingQueue<DelayElement> expiryTransactionQueue;

    @Autowired
    private TransactionDAO transactionDAO;

    @Autowired
    private StatisticsUpdaterSignal statisticsUpdaterSignal;

    @Override
    public void run() {
        while (true) {
            try {
                DelayElement delayElement = expiryTransactionQueue.take();
                logger.info("Delay element received - " + delayElement);
                transactionDAO.removeTransaction(delayElement.getTransactionTimestamp());
                logger.info("Removed transaction from cache");
                statisticsUpdaterSignal.sendSignal();
            } catch (InterruptedException e) {
                logger.info("Exception while processing delay elements", e);
                Thread.currentThread().interrupt();
            }
        }
    }

}
