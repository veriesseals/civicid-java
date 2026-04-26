package com.civicid.apps.persons;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

//This is the brain of the persons app.
//
//Duplicate SSN check — before saving, it looks up whether that SSN already exists and throws a 409 Conflict if so. You never want two people sharing one SSN.
//SSN is immutable — the update method deliberately skips SSN. Once it's set at creation, it cannot be changed through a normal update. That's intentional.
//markAsDeceased() — this method will be called later by the death records app automatically. When a death record is filed, it calls this instead of you manually updating the person.
//404 is automatic — getPersonById() throws the error itself, so the controller never has to deal with null checks.
// -------------------------------------------------------

// PersonService holds all the business logic for Person records.
// The controller handles HTTP — this class handles the actual work.
// Keeping them separate is a core Spring best practice.
// Controller = traffic cop. Service = the worker.
// -------------------------------------------------------
@Service
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;

    }

    // Create a new Person record.
    // Checks for duplicate SSN before saving —
    // two people cannot share the same SSN in this system.
    // -------------------------------------------------------

    public Person createPerson(Person person) {
        if (person.getSsn() != null && !person.getSsn().isBlank()) {
            personRepository.findBySsn(person.getSsn()).ifPresent( existing -> {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "A person with that SSN already exists"
                );
            });
        }
        return personRepository.save(person);
    }

    // Retrieve all persons in the system.
    // SUPER_ADMIN and AUDITOR will use this.
    // -------------------------------------------------------
    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    // Retrieve a single person by their ID.
    // Throws 404 automatically if not found —
    // no need to handle null checks in the controller.
    // -------------------------------------------------------
    public Person getPersonById(Long id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Person not found with id: " + id
                ));
    }

    // Search persons by last name (case-insensitive partial match).
    // Example: searching "smi" returns "Smith", "Smithson", etc.
    // -------------------------------------------------------
    public List<Person> searchByLastName(String lastName) {
        return personRepository.findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(
                firstName, lastName);
    }

    // Update an existing person's information.
    // Only updates fields that are provided — never blindly
    // overwrites everything (safer for partial updates).
    // -------------------------------------------------------
    public Person updatePerson(Long id, Person updates) {
        Person existing = getPersonById(id);

        if (updates.getFirstName() !=null) existing.setFirstName(updates.getFirstName());
        if (updates.getLastName() !=null) existing.setLastName(updates.getLastName());
        if (updates.getDateOfBirth() != null) existing.setDateOfBirth(updates.getDateOfBirth());
        if (updates.getGender() != null) existing.setGender(updates.getGender());
        if (updates.getAddress() != null) existing.setAddress(updates.getAddress());
        if (updates.getCity() != null) existing.setCity(updates.getCity());
        if (updates.getState() != null) existing.setState(updates.getState());
        if (updates.getZipCode() != null) existing.setZipCode(updates.getZipCode());
        if (updates.getIsDeceased() != null) existing.setIsDeceased(updates.getIsDeceased());

        // SSN updates are intentionally blocked here.
        // SSN is set once at creation and should never change.
        // -------------------------------------------------------
        return personRepository.save(existing);

    }

    // Soft concept — mark a person as deceased.
    // Called automatically when a death record is filed.
    // -------------------------------------------------------
    public Person markAsDeceased(Long id) {
        Person person = getPersonById(id);
        person.setIsDeceased(true);
        return personRepository.save(person);
    }
}
