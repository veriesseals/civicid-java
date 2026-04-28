package com.civicid.apps.audit;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// This is the permanent record table.
// performedBy stores the username as a plain string instead of a foreign key to the users table. That's intentional
// — if a user account gets deleted, the audit history must still show who did what. Foreign keys would break that.
// role is stored at the time of the action for the same reason — if someone's role changes later, history should
// reflect what role they had when they acted.
// timestamp has updatable = false — once written it can never be changed, not even by the application itself.
// There's no updatedAt field — audit logs are write-once. No updates, no deletes. Ever.


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

    // Which Person record was affected (if any).
    // Nullable — some actions don't target a specific person.
    // -------------------------------------------------------
    @Column(nullable = true)
    private Long personId;

    // Free-text field for extra context about the action.
    // Example: "Reason: Ongoing investigation case #1234"
    // -------------------------------------------------------
    @Column(length = 1000)
    private String details;

    // The exact timestamp the action occurred.
    // Set once at creation — never updated.
    // -------------------------------------------------------
    @Column(nullable = false, updatable = false)
    private LocalDateTime timestamp;

    public AuditLog() {}

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }

    // --- Getters & Setters ---

    public Long getId() {
        return id;
    }

    public String getPerformedBy() {
        return performedBy;
    }

    public void setPerformedBy(String performedBy) {
        this.performedBy = performedBy;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

}
