package com.wellness.backend.controller;

import com.wellness.backend.dto.UserProfileResponse;
import com.wellness.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    // -----------------------------
    // ADMIN: LIST ALL USERS
    // -----------------------------
    @GetMapping
    public ResponseEntity<List<UserProfileResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // -----------------------------
    // ADMIN: GET USER BY ID
    // -----------------------------
    @GetMapping("/{id}")
    public ResponseEntity<UserProfileResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserProfileById(id));
    }

    // -----------------------------
    // ADMIN: DELETE USER
    // -----------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.adminDeleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}
