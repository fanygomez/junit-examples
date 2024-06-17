package org.sgomez.test.springboot.app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sgomez.test.springboot.app.models.Account;
import org.sgomez.test.springboot.app.services.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.sgomez.test.springboot.app.services.Data.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IAccountService accountService;
    ObjectMapper objectMapper;
    @BeforeEach
    void setUp(){
        objectMapper = new ObjectMapper();
    }
    @Test
    void list() throws Exception {
        // Given
        when(accountService.findAll()).thenReturn(mockCreateAccountsList());

        // When
        mockMvc.perform(get("/api/v1/accounts").contentType(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].person").value("Fanny"))
                .andExpect(jsonPath("$[0].balance").value("850"))
                .andExpect(jsonPath("$[1].person").value("Ale"))
                .andExpect(jsonPath("$[1].balance").value("2000"))
                .andExpect(content().json(objectMapper.writeValueAsString(mockCreateAccountsList())));

        verify(accountService,times(1)).findAll();
    }

    @Test
    void detail() throws Exception {
        // Given - context
        when(accountService.findById(1L)).thenReturn(mockCreateAccount001().orElseThrow());
        // When
        mockMvc.perform(get("/api/v1/accounts/1").contentType(MediaType.APPLICATION_JSON))
       // Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.person").value("Fanny"))
                .andExpect(jsonPath("$.balance").value("850"));

        verify(accountService,times(1)).findById(anyLong());
    }

    @Test
    void save() throws Exception {
        // Given
        when(accountService.save(any())).then(invocation ->{
            Account c = invocation.getArgument(0);
            c.setId(3L);
            return c;
        });

        // when
        mockMvc.perform(post("/api/v1/accounts").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockCreateAccount003().orElseThrow())))
                // Then
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.person", is("Hector")))
                .andExpect(jsonPath("$.balance", is(1500)));

        verify(accountService).save(any());
    }

    @Test
    void transfer() throws Exception {
        //When
        mockMvc.perform(
                post("/api/v1/accounts/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockTransactionReqDto()))
                )
                // Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.date").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.transaction.accountOriginId").value(mockTransactionReqDto().getAccountOriginId()))
                .andExpect(content().json(objectMapper.writeValueAsString(mockTransferResponseDto())));
    }
}