package com.backend.fiestaStaff.service;

import com.backend.fiestaStaff.dto.*;
import com.backend.fiestaStaff.exception.ConflictException;
import com.backend.fiestaStaff.exception.ResourceNotFoundException;
import com.backend.fiestaStaff.model.User;
import com.backend.fiestaStaff.model.Worker;
import com.backend.fiestaStaff.repository.UserRepository;
import com.backend.fiestaStaff.repository.WorkerRepository;
import com.backend.fiestaStaff.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final WorkerRepository workerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, WorkerRepository workerRepository,
                       PasswordEncoder passwordEncoder, JwtService jwtService,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.workerRepository = workerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Email already registered");
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(User.Role.USER);
        user.setDescription(request.getDescription());

        user = userRepository.save(user);

        Long workerId = null;
        Worker worker = workerRepository.findByUserId(user.getId()).orElse(null);
        if (worker != null && worker.getStatus() == Worker.Status.ACTIVE) {
            workerId = worker.getId();
        }

        String token = jwtService.generateToken(user.getId(), user.getRole().name(), workerId);

        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setId(user.getId());
        response.setRole(user.getRole().name());
        response.setWorkerId(workerId);
        return response;
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Long workerId = null;
        String effectiveRole = user.getRole().name(); // default: ADMIN or USER

        Worker worker = workerRepository.findByUserId(user.getId()).orElse(null);
        if (worker != null && worker.getStatus() == Worker.Status.ACTIVE) {
            workerId = worker.getId();
            effectiveRole = "WORKER"; // override: active worker takes precedence
        }

        String token = jwtService.generateToken(user.getId(), effectiveRole, workerId);

        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setId(user.getId());
        response.setRole(effectiveRole);
        response.setWorkerId(workerId);
        return response;
    }
}
