package com.mk.challenge.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class StatisticsUpdaterSignal {

    private final Logger logger = LoggerFactory.getLogger(StatisticsUpdaterSignal.class);

    public void waitForSignal() throws InterruptedException {
        synchronized (this) {
            logger.info("Waiting for signal to update statistics");
            this.wait();
            logger.info("Signal received.");
        }
    }

    public void sendSignal() {
        synchronized (this) {
            this.notifyAll();
            logger.info("Signal sent to update statistics.");
        }
    }

}
