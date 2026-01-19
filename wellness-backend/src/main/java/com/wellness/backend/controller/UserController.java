package com.wellness.backend.controller;

import com.wellness.backend.security.CustomUserDetails;
import com.wellness.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.wellness.backend.dto.UserProfileResponse;
import org.springframework.security.core.userdetails.UserDetails;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService s) { this.userService = s; }

    // logged in user's own profile
    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> me(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        return ResponseEntity.ok(userService.getUserProfileByEmail(email));
    }

    // get other user's profile (admin or owner; add checks later)
    @GetMapping("/{id}")
    public ResponseEntity<UserProfileResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserProfileById(id));
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<String> history(@PathVariable Long id) {
        // return a simple stub message for now
        return ResponseEntity.ok("History endpoint not implemented yet to be filled in Milestone 3.");
    }
    @DeleteMapping("/me")
    public ResponseEntity<String> deleteMyAccount(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        userService.deleteMyProfile(userDetails.getUser());
        return ResponseEntity.ok("Account deleted successfully");
    }

    // -----------------------------
    // ADMIN: DELETE ANY USER
    // -----------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<String> adminDeleteUser(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails.getUser().getRole() != com.wellness.backend.model.Role.ADMIN) {
            return ResponseEntity.status(403).body("Only admin can delete users");
        }

        userService.adminDeleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}

