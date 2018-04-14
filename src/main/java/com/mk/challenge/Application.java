package com.mk.challenge;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.mk.challenge.dao.StatisticsDAO;
import com.mk.challenge.expiry.DelayElement;
import com.mk.challenge.expiry.ExpiryProcessor;
import com.mk.challenge.model.Statistics;
import com.mk.challenge.model.Transaction;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public ExecutorService executorService() {
        return Executors.newFixedThreadPool(2);
    }

    @Bean
    public CommandLineRunner startThreads(final ExecutorService executor, final StatisticsDAO statisticsDAO, final ExpiryProcessor expiryProcessor) {
        return args -> {
            executor.execute(statisticsDAO);
            executor.execute(expiryProcessor);
        };
    }

    @Bean
    public BlockingQueue<DelayElement> expiryTransactionQueue() {
        return new DelayQueue<>();
    }

    @Bean
    public AtomicReference<Statistics> currentStatistics() {
        return new AtomicReference<>(new Statistics());
    }

    @Bean
    public Multimap<Long, Transaction> transactionMap() {
        return ArrayListMultimap.create();
    }

    @Bean
    public int expiryTransactionTimeInSecs() {
        return 60;
    }

}
