package com.mk.challenge.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfigurations {
    @Bean
    public int expiryTransactionTimeInSecs() {
        return 5;
    }
}
