package com.marina.bankingapi.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marina.bankingapi.auth.dto.RegisterRequest;
import com.marina.bankingapi.auth.dto.RegisterResponse;
import com.marina.bankingapi.auth.security.JwtAuthenticationFilter;
import com.marina.bankingapi.auth.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.marina.bankingapi.auth.dto.LoginRequest;
import com.marina.bankingapi.auth.dto.LoginResponse;


@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;


    @Test
    void shouldRegisterUserSuccessfully() throws Exception {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setFirstName("Marina");
        request.setLastName("Mustermann");
        request.setEmail("marina@test.de");
        request.setPassword("password123");
        request.setDateOfBirth(LocalDate.of(1995, 5, 20));

        UUID userId = UUID.randomUUID();

        RegisterResponse response = new RegisterResponse(
                userId,
                "CUST-12345678",
                "marina@test.de"
        );

        when(authService.register(any(RegisterRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.customerNumber").value("CUST-12345678"))
                .andExpect(jsonPath("$.email").value("marina@test.de"));
    }

    @Test
    void shouldLoginSuccessfully() throws Exception {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("marina@test.de");
        request.setPassword("password123");

        LoginResponse response = new LoginResponse(
                "jwt-token",
                "Bearer"
        );

        when(authService.login(any(LoginRequest.class)))
                .thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.type").value("Bearer"));
    }

}