package com.mk.challenge.dao;

import com.mk.challenge.model.Statistics;
import com.mk.challenge.util.StatisticsUpdaterSignal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class StatisticsDAO implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(StatisticsDAO.class);

    @Autowired
    private AtomicReference<Statistics> currentStatistics;

    @Autowired
    private TransactionDAO transactionDAO;

    @Autowired
    private StatisticsUpdaterSignal statisticsUpdaterSignal;

    @Override
    public void run() {
        while (true) {
            try {
                statisticsUpdaterSignal.waitForSignal();
                updateCurrentStatistics();
            } catch (InterruptedException e) {
                logger.error("Exception in statistics updater thread.", e);
            }
        }
    }

    protected void updateCurrentStatistics() {
        Statistics statistics = new Statistics();
        transactionDAO.getAllTransactions().forEach(transaction -> {
            statistics.count = statistics.count + transaction.getCount();
            statistics.sum = round(statistics.sum + transaction.getAmount());
            statistics.maximum = statistics.maximum > transaction.getAmount() ? statistics.maximum : transaction.getAmount();
            statistics.minimum = (statistics.minimum == 0 && transaction.getAmount() != 0) ?
                    transaction.getAmount() :
                    (statistics.minimum < transaction.getAmount() ? statistics.minimum : transaction.getAmount());
        });
        if (statistics.count > 0) {
            statistics.average = round(statistics.sum / statistics.count);
        }
        currentStatistics.set(statistics);
        logger.info("Statistics set to - " + statistics);
    }

    private double round(double value) {
        return new BigDecimal(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

}
