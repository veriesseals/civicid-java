package com.civicid.apps.persons;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.civicid.apps.persons.dto.PersonRequest;
import com.civicid.apps.persons.dto.PersonResponse;

import jakarta.validation.Valid;

// @PreAuthorize is role enforcement at the method level. Each endpoint declares exactly which roles are allowed —
// anyone else gets a 403 Forbidden automatically. No if-statements, no manual role checks in the code.
// Spring handles it all from that one annotation.
// The search endpoint deliberately returns an empty list when no parameters are given instead of dumping the entire
// database. That's a privacy-first decision — you have to ask for something specific to get results.
// mapRequestToPerson() is a private helper that converts the incoming DTO to an entity. You'll see this pattern in
// every app — request comes in as a DTO, gets mapped to an entity for the service, comes back as a response DTO.
// Request → Entity → Response. That's the flow every time.

// PersonController handles all HTTP requests for Person records.
// It delegates all real work to PersonService —
// the controller's only job is routing and HTTP responses.
// -------------------------------------------------------
@RestController
@RequestMapping("/api/persons")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    // POST /api/persons
    // Create a new person record.
    // Only SUPER_ADMIN and REGISTRAR can create persons.
    // -------------------------------------------------------
    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'REGISTRAR')")
    public ResponseEntity<PersonResponse> createPerson(
            @Valid @RequestBody PersonRequest request) {

        // Map the incoming request DTO to a Person entity
        Person person = mapRequestToPerson(request);

        Person saved = personService.createPerson(person);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(PersonResponse.from(saved));
    }

    // GET /api/persons
    // Retrieve all persons.
    // SUPER_ADMIN, REGISTRAR, and AUDITOR can see the full list.
    // -------------------------------------------------------
    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'REGISTRAR', 'AUDITOR')")
    public ResponseEntity<List<PersonResponse>> getAllPersons() {

        List<Person> persons = personService.getAllPersons();
        return ResponseEntity.ok(persons          // ← returns here
                .stream()
                .map(PersonResponse::from)
                .collect(Collectors.toList()));

    }

    // GET /api/persons/{id}
    // Retrieve a single person by ID.
    // Most roles can look up a specific person by ID.
    // -------------------------------------------------------
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'REGISTRAR', 'AUDITOR', 'DMV', 'LAW_ENFORCEMENT', 'ELECTIONS', 'SSA')")
    public ResponseEntity<PersonResponse> getPersonById(@PathVariable Long id) {

        return ResponseEntity.ok(
                PersonResponse.from(personService.getPersonById(id))
            );
    }
    // GET /api/persons/search?lastName=smith
    // GET /api/persons/search?firstName=john&lastName=smith
    // Search persons by name.
    // -------------------------------------------------------
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'REGISTRAR', 'AUDITOR', 'DMV', 'SSA')")
    public ResponseEntity<List<PersonResponse>> searchPersons(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName) {

        if (firstName == null && lastName == null) {
            return ResponseEntity.ok(List.of());
        }

        List<Person> results;

        if (firstName != null && lastName != null) {
            results = personService.searchByName(firstName, lastName);
        } else {
            results = personService.searchByLastName(lastName);
        }

        return ResponseEntity.ok(
                results.stream()
                        .map(PersonResponse::from)
                        .collect(Collectors.toList())
        );
    }

    // PUT /api/persons/{id}
    // Update an existing person's information.
    // Only SUPER_ADMIN and REGISTRAR can update persons.
    // -------------------------------------------------------
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'REGISTRAR')")
    public ResponseEntity<PersonResponse> updatePerson(
            @PathVariable Long id,
            @Valid @RequestBody PersonRequest request) {

        Person updates = mapRequestToPerson(request);
        Person updated = personService.updatePerson(id, updates);

        return ResponseEntity.ok(PersonResponse.from(updated));
    }

    // PUT /api/persons/{id}/deceased
    // Mark a person as deceased.
    // Called by REGISTRAR when a death record is filed.
    // -------------------------------------------------------
    @PutMapping("/{id}/deceased")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'REGISTRAR')")
    public ResponseEntity<PersonResponse> markAsDeceased(
            @PathVariable Long id) {

        return ResponseEntity.ok(PersonResponse.from(personService.markAsDeceased(id))
        );
    }

    // Private helper — maps a PersonRequest DTO to a Person entity.
    // Keeps the mapping logic out of the endpoint methods.
    // -------------------------------------------------------
    private Person mapRequestToPerson(PersonRequest request) {
        Person person = new Person();
        person.setFirstName(request.getFirstName());
        person.setLastName(request.getLastName());
        person.setDateOfBirth(request.getDateOfBirth());
        person.setSsn(request.getSsn());
        person.setGender(request.getGender());
        person.setAddress(request.getAddress());
        person.setCity(request.getCity());
        person.setState(request.getState());
        person.setZipCode(request.getZipCode());
        return person;
    }

}
