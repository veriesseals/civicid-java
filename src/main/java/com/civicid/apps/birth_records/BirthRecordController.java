package com.civicid.apps.birth_records;

import com.civicid.apps.birth_records.dto.BirthRecordRequest;
import com.civicid.apps.birth_records.dto.BirthRecordResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

// BirthRecordController handles all HTTP requests for birth records.
// REGISTRAR is the primary role here — they file and view records.
// SUPER_ADMIN has full access. AUDITOR can read but not write.
// -------------------------------------------------------

@RestController
@RequestMapping("/api/birth_records")
public class BirthRecordController {

    private final BirthRecordService birthRecordService;

    public BirthRecordController(BirthRecordService birthRecordService) {
        this.birthRecordService = birthRecordService;
    }

    // POST /api/birth-records/person/{personId}
    // File a new birth record for an existing person.
    // Only SUPER_ADMIN and REGISTRAR can file birth records.
    // -------------------------------------------------------
    @PostMapping("/person/{personId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'REGISTRAR')")
    public ResponseEntity<BirthRecordResponse> createBirthRecord(
            @PathVariable Long personId,
            @Valid @RequestBody com.civicid.apps.birth_records.dto.BirthRecordRequest request) {
        BirthRecord record = mapRequestToRecord(request);
        BirthRecord saved = birthRecordService.createBirthRecord(personId, record);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(BirthRecordResponse.from(saved));
    }

    // GET /api/birth-records
    // Retrieve all birth records in the system.
    // SUPER_ADMIN and AUDITOR only.
    // -------------------------------------------------------

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'REGISTRAR', 'AUDITOR')")
    public ResponseEntity<List<BirthRecordResponse>> getAllBirthRecords() {

        List<BirthRecordResponse> responses = birthRecordService.getAllBirthRecords()
                .stream()
                .map(BirthRecordResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    // GET /api/birth-records/{id}
    // Retrieve a single birth record by its ID.
    // -------------------------------------------------------
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'REGISTRAR', 'AUDITOR')")
    public ResponseEntity<BirthRecordResponse> getBirthRecordById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                BirthRecordResponse.from(birthRecordService.getBirthRecordById(id))
        );
    }

    // GET /api/birth-records/person/{personId}
    // Retrieve the birth record for a specific person.
    // Also logs a VIEW_BIRTH_RECORD audit entry automatically.
    // -------------------------------------------------------
    @GetMapping("/person/{personId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'REGISTRAR', 'AUDITOR')")
    public ResponseEntity<List<BirthRecordResponse>> getBirthRecordsByPerson(
            @PathVariable Long personId) {

        List<BirthRecordResponse> responses = birthRecordService
                .getBirthRecordsByPersonId(personId)
                .stream()
                .map(BirthRecordResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    // Private helper — maps a BirthRecordRequest DTO to a BirthRecord entity.
    // -------------------------------------------------------
    private BirthRecord mapRequestToRecord(com.civicid.apps.birth_records.dto.BirthRecordRequest request) {
        BirthRecord record = new BirthRecord();
        record.setCertificateNumber(request.getCertificateNumber());
        record.setDateOfBirth(request.getDateOfBirth());
        record.setCityOfBirth(request.getCityOfBirth());
        record.setStateOfBirth(request.getStateOfBirth());
        record.setCountryOfBirth(request.getCountryOfBirth());
        record.setMotherFirstName(request.getMotherFirstName());
        record.setMotherLastName(request.getMotherLastName());
        record.setFatherFirstName(request.getFatherFirstName());
        record.setFatherLastName(request.getFatherLastName());
        record.setBirthFacility(request.getBirthFacility());
        return record;
    }


}
