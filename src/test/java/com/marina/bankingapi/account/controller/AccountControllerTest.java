package com.marina.bankingapi.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marina.bankingapi.account.dto.AccountResponse;
import com.marina.bankingapi.account.dto.CreateAccountRequest;
import com.marina.bankingapi.account.enums.AccountType;
import com.marina.bankingapi.account.service.AccountService;
import com.marina.bankingapi.auth.security.JwtAuthenticationFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
@AutoConfigureMockMvc(addFilters = false)
class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AccountService accountService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void shouldCreateAccountSuccessfully() throws Exception {
        // Arrange
        UUID userId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();

        CreateAccountRequest request = new CreateAccountRequest(
                userId,
                AccountType.CHECKING
        );

        AccountResponse response = new AccountResponse(
                accountId,
                "DE12345678901234567890",
                BigDecimal.valueOf(1000),
                AccountType.CHECKING,
                true
        );

        when(accountService.createAccount(any(CreateAccountRequest.class)))
                .thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(accountId.toString()))
                .andExpect(jsonPath("$.iban").value("DE12345678901234567890"))
                .andExpect(jsonPath("$.balance").value(1000))
                .andExpect(jsonPath("$.accountType").value("CHECKING"))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void shouldGetAccountByIdSuccessfully() throws Exception {
        // Arrange
        UUID accountId = UUID.randomUUID();

        AccountResponse response = new AccountResponse(
                accountId,
                "DE12345678901234567890",
                BigDecimal.valueOf(1000.00),
                AccountType.CHECKING,
                true
        );

        when(accountService.getAccountById(accountId))
                .thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/accounts/{id}", accountId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(accountId.toString()))
                .andExpect(jsonPath("$.iban").value("DE12345678901234567890"))
                .andExpect(jsonPath("$.balance").value(1000.00))
                .andExpect(jsonPath("$.accountType").value("CHECKING"))
                .andExpect(jsonPath("$.active").value(true));
    }

}