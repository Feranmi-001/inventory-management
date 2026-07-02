package com.inventory_management.service;

import com.inventory_management.model.User;
import com.inventory_management.repository.UserRepository;
import com.inventory_management.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StaffService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Add a new staff member
    public User addStaff(User user) {
        String businessId = SecurityUtils.getCurrentBusinessId();

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("User with this email already exists");
        }

        user.setBusinessId(businessId);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        // Default role is STAFF if not specified
        if (user.getRole() == null) {
            user.setRole(User.Role.STAFF);
        }

        return userRepository.save(user);
    }

    // Get all staff for a business
    public List<User> getAllStaff() {
        String businessId = SecurityUtils.getCurrentBusinessId();
        return userRepository.findByBusinessId(businessId);
    }

    // Get staff by ID
    public Optional<User> getStaffById(String id) {
        return userRepository.findById(id);
    }

    // Update staff role
    public User updateStaffRole(String id, User.Role role) {
        return userRepository.findById(id).map(existing -> {
            existing.setRole(role);
            existing.setUpdatedAt(LocalDateTime.now());
            return userRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Staff member not found"));
    }

    // Deactivate staff member
    public User deactivateStaff(String id) {
        return userRepository.findById(id).map(existing -> {
            existing.setActive(false);
            existing.setUpdatedAt(LocalDateTime.now());
            return userRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Staff member not found"));
    }

    // Reactivate staff member
    public User reactivateStaff(String id) {
        return userRepository.findById(id).map(existing -> {
            existing.setActive(true);
            existing.setUpdatedAt(LocalDateTime.now());
            return userRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Staff member not found"));
    }

    // Delete staff member
    public void deleteStaff(String id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Staff member not found");
        }
        userRepository.deleteById(id);
    }
}