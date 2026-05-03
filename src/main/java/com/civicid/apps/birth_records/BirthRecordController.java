package com.civicid.apps.birth_records;

import com.civicid.apps.birth_records.BirthRecordRequest;
import com.civicid.apps.birth_records.dto.BirthRecordRequest;
import com.civicid.apps.birth_records.dto.BirthRecordResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation;
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
        BirthRecord record = new BirthRecord();
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


}
