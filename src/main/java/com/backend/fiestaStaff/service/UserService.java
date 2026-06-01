package com.backend.fiestaStaff.service;

import com.backend.fiestaStaff.dto.UpdateUserRequest;
import com.backend.fiestaStaff.dto.UserResponse;
import com.backend.fiestaStaff.exception.BadRequestException;
import com.backend.fiestaStaff.exception.ResourceNotFoundException;
import com.backend.fiestaStaff.model.User;
import com.backend.fiestaStaff.repository.UserRepository;
import com.backend.fiestaStaff.security.CurrentUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CurrentUser currentUser;

    public UserService(UserRepository userRepository, CurrentUser currentUser) {
        this.userRepository = userRepository;
        this.currentUser = currentUser;
    }

    public UserResponse getCurrentUser() {
        User user = currentUser.get();
        return new UserResponse(user);
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return new UserResponse(user);
    }

    @Transactional
    public UserResponse updateCurrentUser(UpdateUserRequest request) {
        User user = currentUser.get();

        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }
        if (request.getDescription() != null) {
            user.setDescription(request.getDescription());
        }

        user = userRepository.save(user);
        return new UserResponse(user);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Transactional
    public UserResponse updateUserRole(Long id, String role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        try {
            User.Role newRole = User.Role.valueOf(role.toUpperCase());
            user.setRole(newRole);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid role: " + role + ". Must be ADMIN or USER.");
        }

        user = userRepository.save(user);
        return new UserResponse(user);
    }
}
