package com.civicid.apps.law_enforcement;

import java.time.LocalDate;

// The lookup is logged BEFORE data is returned. The verificationRequestRepository.save(request) happens before the
// VerificationResult is built and returned. That means even if something crashes after the save, the lookup is still
// permanently on record. You can never look someone up without it being recorded — there's no gap in the audit trail.
// VerificationResult has 5 fields. PersonResponse has 14. Law enforcement gets name, date of birth, and a deceased flag.
// That's it. No SSN, no address, no timestamps, no department info. They get exactly what they need to verify an
// identity — nothing more. That's minimum necessary access in code.

// VerificationResult is what law enforcement actually receives
// after a lookup. Five fields only — nothing sensitive.
// This is privacy-first design in action.
// Compare this to PersonResponse which has 14 fields —
// LE gets less than half of that, and no SSN, no address.
// -------------------------------------------------------
public class VerificationResult {

    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private boolean isDeceased;

    public VerificationResult(Long personId, String firstName, String lastName, LocalDate dateOfBirth, Boolean isDeceased) {
        this.id = personId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.isDeceased = isDeceased;
    }

    // --- Getters ---

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public boolean isDeceased() {
        return isDeceased;
    }

}
