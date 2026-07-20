package com.marina.bankingapi.transaction.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marina.bankingapi.auth.security.JwtAuthenticationFilter;
import com.marina.bankingapi.transaction.dto.DepositRequest;
import com.marina.bankingapi.transaction.dto.TransactionResponse;
import com.marina.bankingapi.transaction.dto.TransferRequest;
import com.marina.bankingapi.transaction.dto.WithdrawalRequest;
import com.marina.bankingapi.transaction.enums.TransactionType;
import com.marina.bankingapi.transaction.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
@AutoConfigureMockMvc(addFilters = false)
class TransactionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void shouldDepositSuccessfully() throws Exception {
        //Arrange
        UUID accountId = UUID.randomUUID();
        UUID transactionId = UUID.randomUUID();
        DepositRequest request = new DepositRequest(
                accountId,
                BigDecimal.valueOf(500),
                "Gehalt"

                // Werte passend zu deinem Record
        );

        TransactionResponse response = new TransactionResponse(
                transactionId,
                BigDecimal.valueOf(500),
                TransactionType.DEPOSIT,
                "Gehalt",
                LocalDateTime.now()

                // Werte passend zu deinem Record
        );

        when(transactionService.deposit(any(DepositRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/transactions/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldWithdrawSuccessfully() throws Exception {
        // Arrange
        UUID accountId = UUID.randomUUID();
        UUID transactionId = UUID.randomUUID();

        WithdrawalRequest request = new WithdrawalRequest(
                accountId,
                BigDecimal.valueOf(250),
                "Geldautomat"

        );

        TransactionResponse response = new TransactionResponse(
                transactionId,
                BigDecimal.valueOf(250),
                TransactionType.WITHDRAWAL,
                "Geldautomat",
                LocalDateTime.now()
        );

        when(transactionService.withdraw(any(WithdrawalRequest.class)))
                .thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/transactions/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(transactionId.toString()))
                .andExpect(jsonPath("$.amount").value(250))
                .andExpect(jsonPath("$.type").value("WITHDRAWAL"))
                .andExpect(jsonPath("$.description").value("Geldautomat"));
    }

    @Test
    void shouldTransferSuccessfully() throws Exception {
        // Arrange
        UUID sourceAccountId = UUID.randomUUID();
        UUID targetAccountId = UUID.randomUUID();
        UUID transactionId = UUID.randomUUID();
        TransferRequest request = new TransferRequest(
                sourceAccountId,
                targetAccountId,
                BigDecimal.valueOf(100),
                "Miete"
        );

        TransactionResponse response = new TransactionResponse(
                transactionId,
                BigDecimal.valueOf(100),
                TransactionType.TRANSFER,
                "Miete",
                LocalDateTime.now()
        );

        when(transactionService.transfer(any(TransferRequest.class)))
                .thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/transactions/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(transactionId.toString()))
                .andExpect(jsonPath("$.amount").value(100))
                .andExpect(jsonPath("$.type").value("TRANSFER"))
                .andExpect(jsonPath("$.description").value("Miete"));


    }
    @Test
    void shouldReturnBadRequestWhenDepositAmountIsZero() throws Exception {
        UUID accountId = UUID.randomUUID();

        DepositRequest request = new DepositRequest(
                accountId,
                BigDecimal.ZERO,
                "Test"
        );

        mockMvc.perform(post("/api/transactions/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

}