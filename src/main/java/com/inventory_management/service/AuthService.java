package com.inventory_management.service;

import com.inventory_management.model.Business;
import com.inventory_management.model.User;
import com.inventory_management.repository.BusinessRepository;
import com.inventory_management.repository.UserRepository;
import com.inventory_management.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final BusinessRepository businessRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    // Register a new business
    public Map<String, Object> registerBusiness(Business business) {
        if (businessRepository.existsByEmail(business.getEmail())) {
            throw new RuntimeException("Business with this email already exists");
        }

        business.setPassword(passwordEncoder.encode(business.getPassword()));
        business.setCreatedAt(LocalDateTime.now());
        business.setUpdatedAt(LocalDateTime.now());
        Business saved = businessRepository.save(business);

        // Create owner user automatically
        User owner = User.builder()
                .name(business.getName())
                .email(business.getEmail())
                .password(saved.getPassword())
                .businessId(saved.getId())
                .role(User.Role.OWNER)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        userRepository.save(owner);

        String token = jwtUtil.generateToken(
                saved.getEmail(),
                saved.getId(),
                User.Role.OWNER.name()
        );

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("businessId", saved.getId());
        response.put("businessName", saved.getName());
        response.put("role", "OWNER");
        return response;
    }
    public Map<String, Object> login(String email, String password) {
        // Check if it's a staff member logging in
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        if (!user.isActive()) {
            throw new RuntimeException("Your account has been deactivated. Contact your manager.");
        }

        // Get business name
        String businessName = businessRepository.findById(user.getBusinessId())
                .map(b -> b.getName())
                .orElse("Unknown Business");

        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getBusinessId(),
                user.getRole().name()
        );

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("businessId", user.getBusinessId());
        response.put("businessName", businessName);
        response.put("role", user.getRole().name());
        response.put("name", user.getName());
        return response;
    }
}