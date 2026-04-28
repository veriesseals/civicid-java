package com.civicid.apps.audit;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// AuditLog is the accountability engine of CivicID.
// Every sensitive action in the system gets recorded here —
// who did it, what they did, which person it affected, and when.
// This cannot be deleted or modified — it's a permanent record.
// -------------------------------------------------------

@Entity
@Table(name = "audit_logs")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The username of whoever performed the action.
    // Stored as a plain string — not a foreign key —
    // so the log survives even if the user is deleted.
    // -------------------------------------------------------
    @Column(nullable = false)
    private String performedBy;

    // The role of the user at the time of the action.
    // Stored separately so role changes don't alter history.
    // -------------------------------------------------------
    @Column(nullable = false)
    private String role;

    // What action was performed.
    // Examples: "CREATE_PERSON", "VIEW_PERSON", "LAW_ENFORCEMENT_LOOKUP"
    // -------------------------------------------------------
    @Column(nullable = false)
    private String action;


}
