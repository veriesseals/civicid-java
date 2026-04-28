package com.civicid.apps.audit;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

// AuditService pulls the logged-in user automatically from Spring Security's context. That means when any other app calls auditService.log("CREATE_PERSON", 1L, "New birth registration"), it already knows who did it without anyone passing in a username.
// Three overloaded versions of log() keep it flexible:

// AuditService is the logging engine every other app calls.
// Instead of each app writing its own logging code,
// they all call one method here — keeping logging consistent.
// One place to write it. One place to fix it. One place to read it.
// -------------------------------------------------------
@Service
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    public AuditService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    // The main logging method — called by every other app
    // whenever a sensitive action occurs.
    // personId and details are optional (can be null).
    // -------------------------------------------------------
    public void log(String action, Long personId, String details) {

        // Pull the current authenticated user straight from
        // Spring Security's context — no need to pass it in manually.
        // This works because the JwtFilter already set the authentication
        // earlier in the same request.
        // -------------------------------------------------------

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String username = "SYSTEM";
        String role = "SYSTEM";

        if (auth != null && auth.isAuthenticated()) {
            username = auth.getName();

            // Authorities come back as "ROLE_SUPER_ADMIN" —
            // strip the "ROLE_" prefix to store just "SUPER_ADMIN".
            // -------------------------------------------------------
            role = auth.getAuthorities().stream()
                    .findFirst()
                    .map(a -> a.getAuthority().replace("ROLE_", ""))
                    .orElse("UNKNOWN");
        }

        AuditLog log = new AuditLog();
        log.setPerformedBy(username);
        log.setRole(role);
        log.setAction(action);
        log.setPersonId(personId);
        log.setDetails(details);

        auditLogRepository.save(log);

    }

    // Convenience overload — for actions not tied to a specific person.
    // Example: logging a failed login attempt.
    // -------------------------------------------------------
    public void log(String action, String details) {
        log(action, null, details);
    }

    // Convenience overload — for simple actions with no extra details.
    // -------------------------------------------------------
    public void log(String action) {
        log(action, null, null);
    }
}
