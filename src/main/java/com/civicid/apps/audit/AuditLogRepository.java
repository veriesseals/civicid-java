package com.civicid.apps.audit;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

// Four query methods, each sorted newest-first (OrderByTimestampDesc). This gives auditors four ways to slice the data:
//
// By user — "Show me everything jsmith did"
// By person — "Show me everyone who touched Person #42"
// By action type — "Show me every law enforcement lookup ever"
// By role — "Show me everything the DMV role has done"
//
// That's real compliance tooling. Auditors in real government systems need exactly these kinds of queries.

// AuditLogRepository gives us database access for audit records.
// Like PersonRepository, Spring generates all the SQL automatically
// from the method names — no queries needed.
// -------------------------------------------------------
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    // Get all actions performed by a specific user.
    // Useful for investigating a specific employee's activity.
    // -------------------------------------------------------
    List<AuditLog> findByPerformedByOrderByTimestampDesc(String performedBy);

    // Get all actions that affected a specific person record.
    // Useful for seeing the full history of a person's file.
    // -------------------------------------------------------
    List<AuditLog> findByPersonIdOrderByTimestampDesc(Long personId);

    // Get all actions of a specific type across the system.
    // Example: find every LAW_ENFORCEMENT_LOOKUP ever made.
    // -------------------------------------------------------
    List<AuditLog> findByActionOrderByTimestampDesc(String action);

    // Get all actions performed under a specific role.
    // Useful for role-based compliance reports.
    // -------------------------------------------------------
    List<AuditLog> findByRoleOrderByTimestampDesc(String role);

}
