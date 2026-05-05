package com.civicid.apps.law_enforcement;

import com.civicid.apps.persons.Person;
import jakarta.persistence.*;
import java.time.LocalDateTime;

// This entity is different from AuditLog — it's a dedicated table specifically for law enforcement lookups. While AuditLog tracks everything system-wide, VerificationRequest is the official record of every identity check law enforcement has ever run. It has a direct foreign key to the Person table so you can always trace exactly who was looked up.

// VerificationRequest records every single law enforcement lookup.
// This is both the action AND the audit record in one —
// every lookup creates a permanent entry here automatically.
// -------------------------------------------------------
@Entity
@Table(name = "verification_requests")
public class VerificationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The officer who performed the lookup.
    // Stored as username string — survives user deletion.
    // -------------------------------------------------------
    @Column(nullable = false)
    private String performedBy;

    // The person they looked up.
    // -------------------------------------------------------
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    // The reason the officer gave for this lookup.
    // Required — no reason means no lookup.
    // -------------------------------------------------------
    @Column(nullable = false, updatable = false)
    private String reason;

    // The timestamp this lookup occurred.
    // Write-once — never updated.
    // -------------------------------------------------------
    @Column(nullable = false, updatable = false)
    private LocalDateTime requestedAt;

    public VerificationRequest() {}

    @PrePersist
    protected void onCreate() {
        requestedAt = LocalDateTime.now();
    }

    // --- Getters & Setters ---

    public Long getId() {

        return id;
    }

    public String getRequestedBy() {

        return requestedBy;
    }

    public void setRequestedBy(String requestedBy) {

        this.getRequestedBy() = requestedBy;
    }

    public Person getPerson() {

        return person;
    }

    public void setPerson(Person person) {

        this.person = person;
    }

    public String getReason() {

        return reason;
    }

    public void setReason(String reason) {

        this.reason = reason;
    }

    public LocalDateTime getRequestedAt() {

        return requestedAt;
    }

}
