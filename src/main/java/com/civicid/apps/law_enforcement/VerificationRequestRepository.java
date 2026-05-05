package com.civicid.apps.law_enforcement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

// Three queries that answer three very different oversight questions:
// findByRequestedByOrderByRequestedAtDesc — "Show me every lookup Officer Johnson has ever run." This is how you investigate an officer who might be abusing their access.
// findByPersonIdOrderByRequestedAtDesc — "Show me every officer who has ever looked up this specific person." This is how you protect a person's privacy — you can see exactly who accessed their record and why.
// findAllByOrderByRequestedAtDesc — The full system-wide lookup history for supervisors and auditors. Every single LE lookup ever made, newest first.
// All three are sorted newest-first automatically — the most recent activity is always at the top.

public interface VerificationRequestRepository extends JpaRepository<VerificationRequest, Long> {

    List<VerificationRequest> findByRequestedByOrderByRequestedAtDesc(String requestedBy);

    @Query("SELECT vr FROM VerificationRequest vr WHERE vr.person.id = :personId ORDER BY vr.requestedAt DESC")
    List<VerificationRequest> findByPersonIdOrderByRequestedAtDesc(@Param("personId") Long personId);

    List<VerificationRequest> findAllByOrderByRequestedAtDesc();
}
