package com.civicid.apps.persons;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

// Person is the central entity of the entire CivicID system.
// Every other app (birth records, DMV, law enforcement, etc.)
// will have a foreign key pointing back to a Person record.
// Think of this as the "hub" — everything else is a "spoke".
// -------------------------------------------------------

//This is a database table called persons. Every row is one real human being in the system. The isDeceased flag will
// automatically flip to true later when a death record is filed — no manual updates needed. The ssn field is unique
// so no two people can share one. The createdAt and updatedAt timestamps are managed automatically by JPA —
// you never touch them manually.
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
    @Column(unique = true)

    private String ssn;

    private String gender;

    private String address;

    private String city;

    private String state;

    private String zipCode;

    // Tracks whether this person has a death record on file.
    // Set to true when a death record is created for them.
    // -------------------------------------------------------
    @Column(nullable = false)
    private Boolean isDeceased = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public Person() {}

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // --- Getters & Setters ---

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {

        this.ssn = ssn;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {

        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
    public Boolean getIsDeceased() {
        return isDeceased;
    }

    public void setIsDeceased(Boolean isDeceased) {
        this.isDeceased = isDeceased;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

}
