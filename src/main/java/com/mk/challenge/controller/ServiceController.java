package com.mk.challenge.controller;

import com.mk.challenge.model.Statistics;
import com.mk.challenge.model.Transaction;
import com.mk.challenge.service.StatisticsService;
import com.mk.challenge.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for all the Rest API calls.
 */
@RestController
public class ServiceController {

    private final Logger logger = LoggerFactory.getLogger(ServiceController.class);

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private StatisticsService statisticsService;

    @RequestMapping(value = "/transactions", method = RequestMethod.POST)
    public ResponseEntity<Integer> updateTransaction(@RequestBody Transaction transaction) {
        logger.info("Received transaction - " + transaction);
        HttpStatus status = transactionService.processTransaction(transaction);
        return new ResponseEntity<>(status.value(), status);
    }

    @RequestMapping(value = "/statistics", method = RequestMethod.GET)
    public Statistics getTransactionStatistics() {
        Statistics statistics = statisticsService.getCurrentStatistics();
        logger.info("Current statistics - " + statistics);
        return statistics;
    }

}
