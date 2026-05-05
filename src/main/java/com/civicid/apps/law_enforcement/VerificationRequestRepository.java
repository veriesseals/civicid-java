package com.civicid.apps.law_enforcement;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

// Three queries that answer three very different oversight questions:
// findByRequestedByOrderByRequestedAtDesc — "Show me every lookup Officer Johnson has ever run." This is how you investigate an officer who might be abusing their access.
// findByPersonIdOrderByRequestedAtDesc — "Show me every officer who has ever looked up this specific person." This is how you protect a person's privacy — you can see exactly who accessed their record and why.
// findAllByOrderByRequestedAtDesc — The full system-wide lookup history for supervisors and auditors. Every single LE lookup ever made, newest first.
// All three are sorted newest-first automatically — the most recent activity is always at the top.

public class VerificationRequestRepository {

    // VerificationRequestRepository gives us database access
    // for all law enforcement lookup records.
    // -------------------------------------------------------
    public interface VerificationRequestRepository extends JpaRepository<VerificationRequest, Long> {

        // Get all lookups performed by a specific officer.
        // Officers can see their own history.
        // Supervisors and auditors can see anyone's history.
        // -------------------------------------------------------
        List<VerificationRequest> findByRequestedByOrderByRequestedAtDesc(String requestedBy);

        // Get all lookups that targeted a specific person.
        // Answers the question: "Who has looked up this person and why?"
        // -------------------------------------------------------
        List<VerificationRequest> findByPersonIdOrderByRequestedAtDesc(Long personId);

        // Get all lookups in the system, newest first.
        // SUPER_ADMIN and AUDITOR use this for oversight.
        // -------------------------------------------------------
        List<VerificationRequest> findAllByOrderByRequestedAtDesc();
    }
}
