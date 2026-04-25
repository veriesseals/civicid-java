package com.civicid.apps.persons;

import jakarta.persistence.*;

// Person is the central entity of the entire CivicID system.
// Every other app (birth records, DMV, law enforcement, etc.)
// will have a foreign key pointing back to a Person record.
// Think of this as the "hub" — everything else is a "spoke".
// -------------------------------------------------------

@Entity
@Table(name = "persons")
public class Person {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    // SSN must be unique per person.
    // In a real system this would be encrypted at rest —
    // we'll add that in a later phase.
    // -------------------------------------------------------
    @Colomn(umique = true)

    private String ssn;

    private String gender;

    private String address;

    private String city;

    private String state;

    private String zipCode;


    // Tracks whether this person has a death record on file.
    // Set to true when a death record is created for them.
    // -------------------------------------------------------





    
}
