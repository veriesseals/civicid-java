package com.civicid.apps.persons;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

// PersonRepository is your database access layer for Person records.
// By extending JpaRepository, Spring automatically gives you:
//   save(), findById(), findAll(), deleteById(), count(), exists()
// — all for free, no SQL needed.
// -------------------------------------------------------

//This is the database query layer. We are not writing any SQL — Spring reads the method names and figures out the queries automatically. For example, findByLastNameContainingIgnoreCase("smith") becomes SELECT * FROM persons WHERE LOWER(last_name) LIKE '%smith%' behind the scenes. Spring just knows. That's the magic of Spring Data JPA.
// -------------------------------------------------------
public interface PersonRepository extends JpaRepository<Person, Long> {

    // Find a person by their SSN.
    // Used during creation to prevent duplicate SSN entries.
    // -------------------------------------------------------
    Optional<Person> findBySsn(String ssn);

    // Search by last name — useful for registrar lookups.
    // Returns a list because multiple people can share a last name.
    // -------------------------------------------------------
    List<Person> findByLastNameContainingIgnoreCase(String lastName);

    // Search by first AND last name together.
    // -------------------------------------------------------
    List<Person> findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(String firstName, String lastName);

    // Filter living vs deceased persons.
    // -------------------------------------------------------
    List<Person> findByIsDeceased(Boolean isDeceased);

}
