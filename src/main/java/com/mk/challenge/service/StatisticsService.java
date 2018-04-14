package com.mk.challenge.service;

import com.mk.challenge.model.Statistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicReference;

@Component
public class StatisticsService {

    @Autowired
    private AtomicReference<Statistics> currentStatistics;

    public Statistics getCurrentStatistics() {
        return currentStatistics.get();
    }

}
