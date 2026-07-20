package com.marina.bankingapi.auth.service;

import com.marina.bankingapi.auth.dto.LoginRequest;
import com.marina.bankingapi.auth.dto.LoginResponse;
import com.marina.bankingapi.auth.dto.RegisterRequest;
import com.marina.bankingapi.auth.dto.RegisterResponse;
import com.marina.bankingapi.auth.entity.User;
import com.marina.bankingapi.auth.repository.UserRepository;
import com.marina.bankingapi.common.exception.EmailAlreadyExistsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;


class AuthServiceImplTest {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        jwtService = Mockito.mock(JwtService.class);

        authService = new AuthServiceImpl(
                userRepository,
                passwordEncoder,
                jwtService
        );
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        // Arrange
        RegisterRequest request = new RegisterRequest();

        request.setFirstName("Marina");
        request.setLastName("Mustermann");
        request.setEmail("marina@test.de");
        request.setPassword("password123");
        request.setDateOfBirth(LocalDate.of(1995, 5, 20));

        User savedUser = User.builder()
                .id(UUID.randomUUID())
                .customerNumber("CUST-12345678")
                .email("marina@test.de")
                .build();

        Mockito.when(userRepository.existsByEmail(request.getEmail()))
                .thenReturn(false);

        Mockito.when(passwordEncoder.encode(request.getPassword()))
                .thenReturn("encodedPassword");

        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(savedUser);

        // Act
        RegisterResponse response = authService.register(request);

        // Assert
        Assertions.assertNotNull(response);

        Assertions.assertEquals(
                savedUser.getId(),
                response.id()
        );

        Assertions.assertEquals(
                savedUser.getCustomerNumber(),
                response.customerNumber()
        );

        Assertions.assertEquals(
                savedUser.getEmail(),
                response.email()
        );

        Mockito.verify(userRepository)
                .save(Mockito.any(User.class));

    }


    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        // Arrange
        RegisterRequest request = new RegisterRequest();

        request.setFirstName("Marina");
        request.setLastName("Mustermann");
        request.setEmail("marina@test.de");
        request.setPassword("password123");
        request.setDateOfBirth(LocalDate.of(1995, 5, 20));

        Mockito.when(userRepository.existsByEmail(request.getEmail()))
                .thenReturn(true);

        // Act & Assert
        EmailAlreadyExistsException exception = Assertions.assertThrows(
                EmailAlreadyExistsException.class,
                () -> authService.register(request)
        );

        Mockito.verify(userRepository, Mockito.never())
                .save(Mockito.any(User.class));

        Mockito.verify(passwordEncoder, Mockito.never())
                .encode(Mockito.anyString());
    }

    @Test
    void shouldLoginSuccessfully() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("marina@test.de");
        request.setPassword("password123");

        User user = User.builder()
                .email("marina@test.de")
                .passwordHash("encodedPassword")
                .build();

        Mockito.when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.of(user));

        Mockito.when(passwordEncoder.matches(
                request.getPassword(),
                user.getPasswordHash()
        )).thenReturn(true);

        Mockito.when(jwtService.generateToken(user.getEmail()))
                .thenReturn("test-jwt-token");

        // Act
        LoginResponse response = authService.login(request);

        // Assert
        Assertions.assertNotNull(response);
        Assertions.assertEquals("test-jwt-token", response.token());
        Assertions.assertEquals("Bearer", response.type());

        Mockito.verify(userRepository)
                .findByEmail("marina@test.de");

        Mockito.verify(passwordEncoder)
                .matches("password123", "encodedPassword");

        Mockito.verify(jwtService)
                .generateToken("marina@test.de");
    }

    @Test
    void shouldThrowExceptionWhenEmailDoesNotExist() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("marina@test.de");
        request.setPassword("password123");

        Mockito.when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = Assertions.assertThrows(
                RuntimeException.class,
                () -> authService.login(request)
        );

        Assertions.assertEquals(
                "Invalid email or password",
                exception.getMessage()
        );

        Mockito.verify(passwordEncoder, Mockito.never())
                .matches(Mockito.anyString(), Mockito.anyString());

        Mockito.verify(jwtService, Mockito.never())
                .generateToken(Mockito.anyString());
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsIncorrect() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("marina@test.de");
        request.setPassword("wrongPassword");

        User user = User.builder()
                .email("marina@test.de")
                .passwordHash("encodedPassword")
                .build();

        Mockito.when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.of(user));

        Mockito.when(passwordEncoder.matches(
                request.getPassword(),
                user.getPasswordHash()
        )).thenReturn(false);

        // Act & Assert
        RuntimeException exception = Assertions.assertThrows(
                RuntimeException.class,
                () -> authService.login(request)
        );

        Assertions.assertEquals(
                "Invalid email or password",
                exception.getMessage()
        );

        Mockito.verify(jwtService, Mockito.never())
                .generateToken(Mockito.anyString());
    }

}