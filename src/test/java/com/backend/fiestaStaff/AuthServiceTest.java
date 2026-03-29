package com.backend.fiestaStaff;

import com.backend.fiestaStaff.dto.AuthResponse;
import com.backend.fiestaStaff.dto.LoginRequest;
import com.backend.fiestaStaff.dto.RegisterRequest;
import com.backend.fiestaStaff.model.User;
import com.backend.fiestaStaff.repository.UserRepository;
import com.backend.fiestaStaff.repository.WorkerRepository;
import com.backend.fiestaStaff.security.JwtService;
import com.backend.fiestaStaff.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void shouldRegisterNewUser() {
        RegisterRequest request = new RegisterRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("john@example.com");
        request.setPassword("password123");

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertNotNull(response.getToken());
        assertEquals("john@example.com", userRepository.findById(response.getId()).get().getEmail());
        assertEquals("USER", response.getRole());
        assertNull(response.getWorkerId());
    }

    @Test
    void shouldLoginExistingUser() {
        RegisterRequest request = new RegisterRequest();
        request.setFirstName("Jane");
        request.setLastName("Doe");
        request.setEmail("jane@example.com");
        request.setPassword("password123");
        authService.register(request);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("jane@example.com");
        loginRequest.setPassword("password123");

        AuthResponse response = authService.login(loginRequest);

        assertNotNull(response);
        assertNotNull(response.getToken());
        assertEquals("jane@example.com", userRepository.findById(response.getId()).get().getEmail());
    }

    @Test
    void shouldFailLoginWithWrongPassword() {
        RegisterRequest request = new RegisterRequest();
        request.setFirstName("Bob");
        request.setLastName("Smith");
        request.setEmail("bob@example.com");
        request.setPassword("password123");
        authService.register(request);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("bob@example.com");
        loginRequest.setPassword("wrongpassword");

        assertThrows(Exception.class, () -> authService.login(loginRequest));
    }
}
