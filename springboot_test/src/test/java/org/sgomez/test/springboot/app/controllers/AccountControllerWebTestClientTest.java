package org.sgomez.test.springboot.app.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.sgomez.test.springboot.app.models.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.sgomez.test.springboot.app.services.Data.*;
@Tag("integration_wc")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountControllerWebTestClientTest {
    @Autowired
    private WebTestClient webTestClient;
    ObjectMapper objectMapper;
    @BeforeEach
    void setUp(){
        objectMapper = new ObjectMapper();
    }
    @Test
    @Order(1)
    void testTransfer() throws JsonProcessingException {
        // when
        webTestClient.post()
//                .uri("http://localhost:8081/api/v1/accounts/transfer")
                .uri("/api/v1/accounts/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(mockTransactionReqDto())
                .exchange()
                // then
                .expectStatus().isOk()
                .expectBody().jsonPath("$.message").isNotEmpty()
                .jsonPath("$.message").value(is("Success"))
                .jsonPath("$.message").value(value -> Assertions.assertEquals("Success", value))
                .jsonPath("$.transaction.accountOriginId").isEqualTo(mockTransactionReqDto().getAccountOriginId())
                .jsonPath("$.date").isEqualTo(LocalDate.now().toString())
                .json(objectMapper.writeValueAsString(mockTransferResponseDto()));

    }
    @Test
    @Order(2)
    void detail() throws Exception {
        // given
        var account001 = mockCreateAccount001().orElseThrow();
        account001.setBalance(BigDecimal.valueOf(750.0));
        // when
        webTestClient.get()
                .uri("/api/v1/accounts/1")
                .exchange()
                // then
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.person").value(is("Fanny"))
                .jsonPath("$.balance").value(is(750.0))
                .json(objectMapper.writeValueAsString(account001));
    }
    @Test
    @Order(3)
    void detail2() {
        // when
        webTestClient.get()
                .uri("/api/v1/accounts/2")
                .exchange()
                // then
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Account.class)
                .consumeWith(respose ->{
                   Account account = respose.getResponseBody();
                    assertEquals("Ale", account.getPerson());
                    assertEquals("2100.00", account.getBalance().toPlainString());
                });
    }
    @Test
    @Order(4)
    void list(){
        webTestClient.get()
                .uri("/api/v1/accounts")
                .exchange()
                // then
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$").value(hasSize(2))
                .jsonPath("$[0].person").isEqualTo("Fanny")
                .jsonPath("$[0].balance").isEqualTo(750.0);

    }
    @Test
    @Order(5)
    void list2() throws Exception {
        webTestClient.get()
                .uri("/api/v1/accounts")
                .exchange()
                // then
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Account.class)
                .consumeWith(response -> {
                   List<Account> accountList = response.getResponseBody();
                   assertNotNull(accountList);
                    assertEquals(2, accountList.size());
                    assertEquals(2L, accountList.get(1).getId());
                    assertEquals("Ale", accountList.get(1).getPerson());
                    assertEquals(BigDecimal.valueOf(2100.0), accountList.get(1).getBalance());

                })
                .hasSize(2)
                .value(hasSize(2));

    }
    @Test
    @Order(6)
    void save(){

        webTestClient.post().uri("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(mockCreateAccount003())
                .exchange()
                // Then
                .expectStatus()
                .isCreated()
                .expectBody()
                .jsonPath("$.person").value(is("Hector"))
                .jsonPath("$.balance").isEqualTo(1500);

    }
    @Test
    @Order(7)
    void save2(){

        webTestClient.post().uri("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(mockCreateAccount004())
                .exchange()
                // Then
                .expectStatus()
                .isCreated()
                .expectBody(Account.class)
                .consumeWith(respose ->{
                    Account account = respose.getResponseBody();
                    assertNotNull(account);
                    assertEquals(4L, account.getId());
                    assertEquals("Cristian", account.getPerson());
                    assertEquals("1000", account.getBalance().toPlainString());
                });

    }
    @Test
    @Order(8)
    void testDelete(){
        webTestClient.get().uri("/api/v1/accounts").exchange()
                        .expectStatus().isOk()
                        .expectBodyList(Account.class)
                        .hasSize(4);

        webTestClient.delete().uri("/api/v1/accounts/3")
                .exchange()
                .expectStatus().isNoContent();
        //validate list after removed
        webTestClient.get().uri("/api/v1/accounts").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Account.class)
                .hasSize(3);

        // valid id was removed
        webTestClient.get()
                .uri("/api/v1/accounts/3")
                .exchange()
                // then
//                .expectStatus().is5xxServerError();
                .expectStatus().isNotFound()
                .expectBody().isEmpty();
    }
}