package com.mk.challenge.controller;

import com.mk.challenge.Application;
import com.mk.challenge.model.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class, TestConfigurations.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ServiceControllerTest {

    @LocalServerPort
    private int port;

    private TestRestTemplate restTemplate;
    private HttpHeaders httpHeaders;

    @Before
    public void setUp() throws Exception {
        restTemplate = new TestRestTemplate();
        httpHeaders = new HttpHeaders();
    }

    /**
     * Testing end to end scenario
     *
     * @throws InterruptedException
     */
    @Test
    public void testStatisticsForMultipleTransactionsAndTimestamp() throws InterruptedException {
        HttpEntity<Transaction> entity1 = new HttpEntity<>(new Transaction(12.3, System.currentTimeMillis()), httpHeaders);
        ResponseEntity<String> responseEntity1 = restTemplate.exchange(createUrl("/transactions"), HttpMethod.POST, entity1, String.class);
        assertEquals(HttpStatus.CREATED, responseEntity1.getStatusCode());

        ResponseEntity<String> responseEntity2 = restTemplate.exchange(createUrl("/statistics"), HttpMethod.GET, new HttpEntity<>(null, httpHeaders), String.class);
        assertEquals("{\"sum\":12.3,\"average\":12.3,\"maximum\":12.3,\"minimum\":12.3,\"count\":1}", responseEntity2.getBody());

        ResponseEntity<String> responseEntity3 = restTemplate.exchange(createUrl("/transactions"), HttpMethod.POST, entity1, String.class);
        assertEquals(HttpStatus.CREATED, responseEntity3.getStatusCode());

        ResponseEntity<String> responseEntity4 = restTemplate.exchange(createUrl("/statistics"), HttpMethod.GET, new HttpEntity<>(null, httpHeaders), String.class);
        assertEquals("{\"sum\":24.6,\"average\":12.3,\"maximum\":12.3,\"minimum\":12.3,\"count\":2}", responseEntity4.getBody());

        Thread.sleep(2000);

        HttpEntity<Transaction> entity2 = new HttpEntity<>(new Transaction(13.3, System.currentTimeMillis() - 6000), httpHeaders);
        ResponseEntity<String> responseEntity5 = restTemplate.exchange(createUrl("/transactions"), HttpMethod.POST, entity2, String.class);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity5.getStatusCode());

        HttpEntity<Transaction> entity3 = new HttpEntity<>(new Transaction(13.3, System.currentTimeMillis()), httpHeaders);
        ResponseEntity<String> responseEntity6 = restTemplate.exchange(createUrl("/transactions"), HttpMethod.POST, entity3, String.class);
        assertEquals(HttpStatus.CREATED, responseEntity6.getStatusCode());

        ResponseEntity<String> responseEntity7 = restTemplate.exchange(createUrl("/statistics"), HttpMethod.GET, new HttpEntity<>(null, httpHeaders), String.class);
        assertEquals("{\"sum\":37.9,\"average\":12.63,\"maximum\":13.3,\"minimum\":12.3,\"count\":3}", responseEntity7.getBody());

        // This sleep would make first two transactions to expire and statistics would change.
        Thread.sleep(3000);

        ResponseEntity<String> responseEntity8 = restTemplate.exchange(createUrl("/statistics"), HttpMethod.GET, new HttpEntity<>(null, httpHeaders), String.class);
        assertEquals("{\"sum\":13.3,\"average\":13.3,\"maximum\":13.3,\"minimum\":13.3,\"count\":1}", responseEntity8.getBody());

        // This sleep would make third transactions to expire and statistics would change.
        Thread.sleep(5000);

        ResponseEntity<String> responseEntity9 = restTemplate.exchange(createUrl("/statistics"), HttpMethod.GET, new HttpEntity<>(null, httpHeaders), String.class);
        assertEquals("{\"sum\":0.0,\"average\":0.0,\"maximum\":0.0,\"minimum\":0.0,\"count\":0}", responseEntity9.getBody());

    }

    private String createUrl(final String uri) {
        return "http://localhost:" + port + uri;
    }

}