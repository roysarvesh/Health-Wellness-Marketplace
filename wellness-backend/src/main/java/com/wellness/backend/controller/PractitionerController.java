package com.wellness.backend.controller;

import com.wellness.backend.dto.*;
import com.wellness.backend.service.PractitionerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/practitioners")
@CrossOrigin(origins = "*")
public class PractitionerController {

    private final PractitionerService service;

    public PractitionerController(PractitionerService service) {
        this.service = service;
    }

    // public listing
    @GetMapping
    public ResponseEntity<List<PractitionerResponse>> listAll() {
        return ResponseEntity.ok(service.listAllPractitioners());
    }
    // get single practitioner profile
    @GetMapping("/{id}")
    public ResponseEntity<PractitionerResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getPractitionerById(id));
    }

    // practitioner updates their own profile
    @PutMapping("/{id}/update")
    public ResponseEntity<PractitionerResponse> updateProfile(
            @PathVariable("id") Long id,
            @RequestBody PractitionerUpdateRequest req,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String email = userDetails.getUsername();
        return ResponseEntity.ok(service.updatePractitionerProfile(id, req, email));
    }

    // admin verifies (role check in annotation)
    @PutMapping("/{id}/verify")
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VerifyResponse> verify(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.verifyPractitioner(id));
    }
}
