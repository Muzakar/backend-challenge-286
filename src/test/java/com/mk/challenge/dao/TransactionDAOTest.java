package com.mk.challenge.dao;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.mk.challenge.expiry.DelayElement;
import com.mk.challenge.model.Transaction;
import com.mk.challenge.util.StatisticsUpdaterSignal;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
public class TransactionDAOTest {

    @TestConfiguration
    static class TransactionDAOTestConfigs {
        @Bean
        public TransactionDAO transactionDAO() {
            return new TransactionDAO();
        }

        @Bean
        public Multimap<Long, Transaction> transactionMap() {
            return ArrayListMultimap.create();
        }

        @Bean
        public int expiryTransactionTimeInSecs() {
            return 2;
        }

        @Bean
        public BlockingQueue<DelayElement> expiryTransactionQueue() {
            return new DelayQueue<>();
        }
    }

    @Autowired
    private TransactionDAO transactionDAO;

    @MockBean
    private StatisticsUpdaterSignal statisticsUpdaterSignal;

    @Autowired
    private Multimap<Long, Transaction> transactionMap;

    @Autowired
    private BlockingQueue<DelayElement> expiryTransactionQueue;

    @Before
    public void setUp() throws Exception {
        transactionMap.clear();
        expiryTransactionQueue.clear();
    }

    @Test
    public void testTransactionMapPut() throws InterruptedException {
        transactionDAO.updateTransaction(new Transaction(10.9, 100L));
        assertTrue(transactionMap.size() == 1);
        verify(statisticsUpdaterSignal, times(1)).sendSignal();
        Thread.sleep(2000);
        DelayElement delayElement = expiryTransactionQueue.poll();
        assertEquals(2100, delayElement.getExpiryTime());
    }

    @Test
    public void testRemoveTransaction() {
        transactionMap.put(100L, new Transaction(10.3, 100L));
        transactionMap.put(100L, new Transaction(11.5, 100L));
        transactionMap.put(200L, new Transaction(13.6, 200L));
        transactionDAO.removeTransaction(100L);
        assertEquals(1, transactionMap.size());
        assertEquals(13.6, transactionMap.get(200L).iterator().next().getAmount(), 0);
    }

    @Test
    public void testGetAllTransaction() {
        transactionMap.put(100L, new Transaction(10.3, 100L));
        transactionMap.put(100L, new Transaction(11.5, 100L));
        transactionMap.put(200L, new Transaction(13.6, 200L));
        Collection<Transaction> transactions = transactionDAO.getAllTransactions();
        assertEquals(3, transactionMap.size());
    }

}