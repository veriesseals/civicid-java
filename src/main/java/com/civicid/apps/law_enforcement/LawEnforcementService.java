package com.civicid.apps.law_enforcement;

import com.civicid.apps.audit.AuditService;
import com.civicid.apps.persons.Person;
import com.civicid.apps.persons.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

// LawEnforcementService is the most security-conscious service
// in the entire system. Every method here is designed around
// the principle of minimum necessary access.
// No browsing. No bulk data. Reason required. Everything logged.
// -------------------------------------------------------
public class LawEnforcementService {

    private final VerificationRequestRepository verificationRequestRepository;
    private final PersonService personService;
    private final AuditService auditService;

    public LawEnforcementService(VerificationRequestRepository verificationRequestRepository,
                                 PersonService personService, AuditService auditService) {
        this.verificationRequestRepository = verificationRequestRepository;
        this.personService = personService;
        this.auditService = auditService;
    }
    // The core lookup method — the heart of the LE app.
    // An officer provides a person ID and a reason.
    // The system logs it, records it, and returns minimal data.
    // -------------------------------------------------------
    public VerificationResult verifyPerson(Long personId, String reason) {

        // Reason is mandatory — blank reason means no lookup.
        // -------------------------------------------------------
        if (reason == null || reason.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A reason must be provided for all identity lookups"
            );
        }

        // Verify the person exists — throws 404 if not.
        // -------------------------------------------------------
        Person person = personService.getPersonById(personId);

        // Get the currently authenticated officer.
        // -------------------------------------------------------
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String officerUsername = auth != null ? auth.getName() : "UNKNOWN";

        // Create and save the verification request record.
        // This happens BEFORE we return any data —
        // the lookup is logged even if something fails after this.
        // -------------------------------------------------------
        VerificationRequest request = new VerificationRequest();
        request.setRequestedBy(officerUsername);
        request.setPerson(person);
        request.setReason(reason);
        verificationRequestRepository.save(request);

        // Also log to the system-wide audit trail.
        // -------------------------------------------------------
        auditService.log(
                "LAW_ENFORCEMENT_LOOKUP",
                personId,
                "Officer: " + officerUsername + " | Reason: " + reason
        );

        // Return only the minimum data the officer needs.
        // No SSN. No full address. No sensitive fields.
        // -------------------------------------------------------
        return new VerificationResult(
                person.getId(),
                person.getFirstName(),
                person.getLastName(),
                person.getDateOfBirth(),
                person.getIsDeceased()
        );
    }

    // Get lookup history for a specific officer.
    // Officers can view their own history.
    // SUPER_ADMIN and AUDITOR can view anyone's.
    // -------------------------------------------------------
    public List<VerificationRequest> getHistoryByPerson(Long personId) {

        // Verify person exists first.
        personService.getPersonById(personId);

        auditService.log(
                "VIEW_VERIFICATION_HISTORY",
                personId,
                "Verification history viewed for person id: " + personId
        );

        return verificationRequestRepository
                .findByPersonIdOrderByRequestedAtDesc(personId);
    }

    // Get the full system-wide lookup history.
    // SUPER_ADMIN and AUDITOR only.
    // -------------------------------------------------------
    public List<VerificationRequest> getAllHistory() {
        return verificationRequestRepository.findAllByOrderByRequestedAtDesc();
    }

    
}

