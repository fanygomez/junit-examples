package org.sgomez.test.springboot.app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.sgomez.test.springboot.app.models.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.sgomez.test.springboot.app.services.Data.*;

@Tag("integration_rest_template")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountControllerTestRestTemplateTest {
    @Autowired
    private TestRestTemplate restTemplate;
    private ObjectMapper objectMapper;
    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @Order(3)
    void list() {
        ResponseEntity<Account[]> response = restTemplate
                .getForEntity("/api/v1/accounts" , Account[].class);
        List<Account> accountList = Arrays.asList(response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertNotNull(accountList);
        assertEquals(2, accountList.size());
        assertEquals("Fanny", accountList.get(0).getPerson());
        assertEquals("750.00", accountList.get(0).getBalance().toPlainString());
    }

    @Test
    @Order(2)
    void detail() {
        ResponseEntity<Account> response = restTemplate
                .getForEntity("/api/v1/accounts/1" , Account.class);
        Account account = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertNotNull(account);
        assertEquals("Fanny", account.getPerson());
        assertEquals("750.00", account.getBalance().toPlainString());
    }

    @Test
    @Order(4)
    void save() {
        ResponseEntity<Account> response = restTemplate
                .postForEntity("/api/v1/accounts",mockCreateAccount003().orElseThrow(), Account.class);
        var accountResp = response.getBody();

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertNotNull(accountResp);
        assertEquals(3L, accountResp.getId());
        assertEquals("Hector", accountResp.getPerson());
        assertEquals("1500", accountResp.getBalance().toPlainString());
    }

    @Test
    @Order(1)
    void transfer() {
        var response = restTemplate
                .postForEntity("/api/v1/accounts/transfer",mockTransactionReqDto(), String.class);
        var jsonStrResp = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertNotNull(jsonStrResp);
        assertTrue(jsonStrResp.contains("Success"));
    }

    @Test
    @Order(5)
    void delete() {
        // validate: before delete
        ResponseEntity<Account[]> response = restTemplate
                .getForEntity("/api/v1/accounts" , Account[].class);
        List<Account> accountList = Arrays.asList(response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertNotNull(accountList);
        assertEquals(3, accountList.size());

        // Delete
//        restTemplate.delete("/api/v1/accounts/3");

        var deleteResp = restTemplate.exchange("/api/v1/accounts/3", HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatus.NO_CONTENT, deleteResp.getStatusCode());
        assertNotNull(deleteResp);
        assertFalse(deleteResp.hasBody());

        // validate: After delete
        ResponseEntity<Account> responseDetail = restTemplate
                .getForEntity("/api/v1/accounts/3" , Account.class);

        assertEquals(HttpStatus.NOT_FOUND, responseDetail.getStatusCode());
        assertNotNull(responseDetail);
        assertFalse(responseDetail.hasBody());
    }
}