package com.civicid.apps.birth_records;

import com.civicid.apps.audit.AuditService;
import com.civicid.apps.persons.Person;
import com.civicid.apps.persons.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

// This service does something none of the previous ones did — it coordinates multiple apps together. When a birth record is created it talks to three different systems in one transaction:
// PersonService confirms the person exists before anything else happens. You can't create a birth record for a ghost.
// Three conflict checks run before any data is saved — person must exist, person can't already have a birth record, and the certificate number must be unique. All three throw clean HTTP errors if they fail.
// AuditService.log() is called at the end of both createBirthRecord and getBirthRecordsByPersonId. This means even viewing a birth record gets logged — not just creating one. That's real government-level accountability. Every access is tracked.
// filedBy is automatically stamped from Spring Security's context — the registrar doesn't pass their own username in. The system records it from the authenticated token. This prevents anyone from faking who filed a record.

// BirthRecordService handles all business logic for birth records.
// It coordinates between three things:
//   1. BirthRecordRepository — database access
//   2. PersonService — verifying the person exists
//   3. AuditService — logging every action
// -------------------------------------------------------
@Service
public class BirthRecordService {

    private final BirthRecordRepository birthRecordRepository;
    private final PersonService personService;
    private final AuditService auditService;

    public BirthRecordService(BirthRecordRepository birthRecordRepository, PersonService personService, AuditService auditService) {
        this.birthRecordRepository = birthRecordRepository;
        this.personService = personService;
        this.auditService = auditService;
    }

    // Create a new birth record for a person.
    // Three checks before saving:
    //   1. Person must exist
    //   2. Person must not already have a birth record
    //   3. Certificate number must be unique
    // -------------------------------------------------------
    public BirthRecord createBirthRecord(Long personId, BirthRecord record) {

        // Check 1 — does the person exist?
        // -------------------------------------------------------
        Person person = personService.getPersonById(personId);

        // Check 2 — does this person already have a birth record?
        if (birthRecordRepository.existsByPersonId(personId)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "A birth record already exists for this person"
            );
        }

        // Check 3 — is the certificate number already taken?
        // -------------------------------------------------------
        if (record.getCertificateNumber() != null) {
            birthRecordRepository.findByCertificateNumber(record.getCertificateNumber())
                    .ifPresent(existing -> {
                        throw new ResponseStatusException(
                                HttpStatus.CONFLICT,
                                "Certificate number already exists"
                        );
                    });
        }

        // Link the person and stamp who filed this record.
        // -------------------------------------------------------
        record.setPerson(person);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        record.setFiledBy(auth != null ? auth.getName() : "SYSTEM");

        BirthRecord saved = birthRecordRepository.save(record);

        // Log the action to the audit trail.
        // -------------------------------------------------------
        auditService.log(
                "CREATE_BIRTH_RECORD",
                personId,
                "Certificate: " + saved.getCertificateNumber() +
                        ", Filed by: " + saved.getFiledBy()
        );

        return saved;
    }

    // Get all birth records in the system.
    // SUPER_ADMIN and AUDITOR use this.
    // -------------------------------------------------------
    @Transactional(readOnly = true)
    public List<BirthRecord> getAllBirthRecords() {
        return birthRecordRepository.findAllWithPerson();
    }

    // Get a single birth record by its ID.
    // Throws 404 if not found.
    // -------------------------------------------------------
    @Transactional(readOnly = true)
    public BirthRecord getBirthRecordById(Long id) {
        return birthRecordRepository.findByIdWithPerson(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Birth record not found with id: " + id
                ));
    }

    // Get the birth record for a specific person.
    // -------------------------------------------------------
    @Transactional(readOnly = true)
    public List<BirthRecord> getBirthRecordsByPersonId(Long personId) {

        // Verify the person exists first — throws 404 if not.
        personService.getPersonById(personId);

        List<BirthRecord> records = birthRecordRepository.findByPersonId(personId);

        // Log that this person's birth record was accessed.
        // -------------------------------------------------------
        auditService.log(
                "VIEW_BIRTH_RECORD",
                personId,
                "Birth record viewed for person id: " + personId

        );

        return records;
    }


}
