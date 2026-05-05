package com.civicid.apps.law_enforcement;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// LawEnforcementController exposes the identity verification API.
// This is the most restricted controller in the system.
// LAW_ENFORCEMENT can only verify and see their own history.
// SUPER_ADMIN and AUDITOR can see all history.
// Nobody can delete or modify anything here.
// -------------------------------------------------------
@RestController
@RequestMapping("/api/law_enforcement")
public class LawEnforcementController {

    private final LawEnforcementService lawEnforcementService;

    public LawEnforcementController(LawEnforcementService lawEnforcementService) {
        this.lawEnforcementService = lawEnforcementService;
    }

    // POST /api/law-enforcement/verify/{personId}
    // The core lookup endpoint — requires a reason in the body.
    // Returns minimal data only.
    // -------------------------------------------------------
    @PostMapping("/verify/{personId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'LAW_ENFORCEMENT')")
    public ResponseEntity<VerificationResult> verifyPerson(
            @PathVariable Long personId,
            @RequestBody LookupRequest request) {

        VerificationResult result = lawEnforcementService
                .verifyPerson(personId, request.getReason());

        return ResponseEntity.ok(result);
    }

    // GET /api/law-enforcement/history/me
    // An officer views their own lookup history.
    // Uses the JWT token to identify who's asking —
    // they can only ever see their own history this way.
    // -------------------------------------------------------

    @GetMapping("/history/me")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'LAW_ENFORCEMENT')")
    public ResponseEntity<List<VerificationRequest>> getHistory(
            Authentication authentication) {

        return ResponseEntity.ok(
                lawEnforcementService.getHistoryByOfficer(
                        authentication.getName()
                )
        );
    }

    // GET /api/law-enforcement/history/person/{personId}
    // See all lookups that targeted a specific person.
    // Answers: "Who has accessed this person and why?"
    // -------------------------------------------------------
    @GetMapping("/history/person/{personId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'AUDITOR')")
    public ResponseEntity<List<VerificationRequest>> getHistoryByPerson(
            @PathVariable Long personId) {

        return ResponseEntity.ok(
                lawEnforcementService.getHistoryByPerson(personId)
        );
    }

    // GET /api/law-enforcement/history
    // Full system-wide lookup history.
    // SUPER_ADMIN and AUDITOR only.
    // -------------------------------------------------------
    @GetMapping("/history")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'AUDITOR')")
    public ResponseEntity<List<VerificationRequest>> getAllHistory() {

        return ResponseEntity.ok(
                lawEnforcementService.getAllHistory()
        );
    }
}
