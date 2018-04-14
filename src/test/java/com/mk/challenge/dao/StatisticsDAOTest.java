package com.mk.challenge.dao;

import com.mk.challenge.model.Statistics;
import com.mk.challenge.model.Transaction;
import com.mk.challenge.util.StatisticsUpdaterSignal;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class StatisticsDAOTest {

    @TestConfiguration
    static class TransactionDAOTestConfigs {

        @Bean
        public StatisticsDAO statisticsDAO() {
            return new StatisticsDAO();
        }

        @Bean
        public AtomicReference<Statistics> currentStatistics() {
            return new AtomicReference<>(new Statistics());
        }

    }

    @Autowired
    private StatisticsDAO statisticsDAO;

    @MockBean
    private TransactionDAO transactionDAO;

    @MockBean
    private StatisticsUpdaterSignal statisticsUpdaterSignal;

    @Autowired
    private AtomicReference<Statistics> currentStatistics;

    @Test
    public void testUpdateTransaction() {
        Mockito.when(transactionDAO.getAllTransactions()).thenReturn(new ArrayList<Transaction>() {{
            add(new Transaction(10.3, 100L));
            add(new Transaction(20.5987, 200L));
            add(new Transaction(10.30, 100L));
            add(new Transaction(14.56, 300L));
        }});
        statisticsDAO.updateCurrentStatistics();
        assertEquals(55.76, currentStatistics.get().sum, 0);
        assertEquals(13.94, currentStatistics.get().average, 0);
        assertEquals(20.5987, currentStatistics.get().maximum, 0);
        assertEquals(10.3, currentStatistics.get().minimum, 0);
        assertEquals(4, currentStatistics.get().count);
    }

}