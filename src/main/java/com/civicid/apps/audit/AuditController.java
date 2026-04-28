package com.civicid.apps.audit;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Five read-only endpoints. Notice there is no POST, PUT, or DELETE anywhere in this controller — that's intentional.
// The only way audit logs get created is through AuditService.log() being called internally by other apps. Nobody can
// write to the audit log directly through the API, and nobody can delete or modify entries. Ever.

// AuditController exposes read-only endpoints for querying audit logs.
// Only SUPER_ADMIN and AUDITOR can access these —
// no other role has any business reading the audit trail.
// These endpoints are intentionally READ ONLY — no POST, PUT, or DELETE.
// Audit logs are permanent. They cannot be created, modified,
// or deleted through the API. Ever.
// -------------------------------------------------------

@RestController
@RequestMapping("/api/audit")
public class AuditController {

    private final AuditLogRepository auditLogRepository;

    public AuditController(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    // GET /api/audit
    // Returns every audit log in the system, newest first.
    // -------------------------------------------------------
    @GetMapping("/logs")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('AUDITOR')")
    public ResponseEntity<List<AuditLog>> getAllLogs() {
        return ResponseEntity.ok(
                auditLogRepository.findAll()
        );
    }

    // GET /api/audit/user/{username}
    // Returns all actions performed by a specific user.
    // Example: /api/audit/user/jsmith
    // -------------------------------------------------------
    @GetMapping("/user/{username}")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('AUDITOR')")
    public ResponseEntity<List<AuditLog>> getLogsByUser(
            @PathVariable String username) {
        return ResponseEntity.ok(
                auditLogRepository.findByPerformedByOrderByTimestampDesc(username)
        );
    }

    // GET /api/audit/person/{personId}
    // Returns all actions that touched a specific person's record.
    // Example: /api/audit/person/42
    // -------------------------------------------------------
    @GetMapping("/person/{personId}")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('AUDITOR')")
    public ResponseEntity<List<AuditLog>> getLogsByPerson(
            @PathVariable Long personId) {
        return ResponseEntity.ok(
                auditLogRepository.findByPersonIdOrderByTimestampDesc(personId)
        );
    }

    // GET /api/audit/action/{action}
    // Returns all logs of a specific action type.
    // Example: /api/audit/action/LAW_ENFORCEMENT_LOOKUP
    // -------------------------------------------------------
    @GetMapping("/action/{action}")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('AUDITOR')")
    public ResponseEntity<List<AuditLog>> getLogsByAction(
            @PathVariable String action) {
        return ResponseEntity.ok(
                auditLogRepository.findByActionOrderByTimestampDesc(action)
        );
    }

    // GET /api/audit/role/{role}
    // Returns all logs from a specific role.
    // Example: /api/audit/role/LAW_ENFORCEMENT
    // -------------------------------------------------------
    @GetMapping("/role/{role}")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('AUDITOR')")
    public ResponseEntity<List<AuditLog>> getLogsByRole(
            @PathVariable String role) {
        return ResponseEntity.ok(
                auditLogRepository.findByRoleOrderByTimestampDesc(role)
        );
    }

}
