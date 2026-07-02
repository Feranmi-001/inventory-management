package com.inventory_management.controller;

import com.inventory_management.model.User;
import com.inventory_management.service.StaffService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/staff")
@RequiredArgsConstructor
public class StaffController {

    private final StaffService staffService;

    // Add a new staff member
    @PostMapping
    public ResponseEntity<User> addStaff(@Valid @RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(staffService.addStaff(user));
    }

    // Get all staff
    @GetMapping
    public ResponseEntity<List<User>> getAllStaff() {
        return ResponseEntity.ok(staffService.getAllStaff());
    }

    // Get staff by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getStaffById(@PathVariable String id) {
        return staffService.getStaffById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update staff role
    @PutMapping("/{id}/role")
    public ResponseEntity<User> updateStaffRole(@PathVariable String id,
                                                @RequestBody Map<String, String> body) {
        User.Role role = User.Role.valueOf(body.get("role"));
        return ResponseEntity.ok(staffService.updateStaffRole(id, role));
    }

    // Deactivate staff member
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<User> deactivateStaff(@PathVariable String id) {
        return ResponseEntity.ok(staffService.deactivateStaff(id));
    }

    // Reactivate staff member
    @PutMapping("/{id}/reactivate")
    public ResponseEntity<User> reactivateStaff(@PathVariable String id) {
        return ResponseEntity.ok(staffService.reactivateStaff(id));
    }

    // Delete staff member
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStaff(@PathVariable String id) {
        staffService.deleteStaff(id);
        return ResponseEntity.noContent().build();
    }
}