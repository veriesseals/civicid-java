package com.civicid.apps.birth_records;

import com.civicid.apps.birth_records.BirthRecordRequest;
import com.civicid.apps.birth_records.dto.BirthRecordResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

// BirthRecordController handles all HTTP requests for birth records.
// REGISTRAR is the primary role here — they file and view records.
// SUPER_ADMIN has full access. AUDITOR can read but not write.
// -------------------------------------------------------

@RestController
@RequestMapping("/api/birth_records")
public class BirthRecordController {



}
