package com.civicid.apps.birth_records;

import com.civicid.apps.persons.Person;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

// This is the first entity that creates a relationship between two tables. The @ManyToOne annotation on person means each birth record points to one person in the persons table. When Hibernate creates the database, it automatically adds a person_id foreign key column to birth_records.
// FetchType.LAZY means the Person data isn't loaded from the database until you actually access it — more efficient than loading everything eagerly every time.
// certificateNumber is unique — no two birth records can share the same certificate number, just like in real life.
// filedBy stores the registrar's username as a string — same pattern as AuditLog — so the record survives even if that user account is later deleted.

// BirthRecord is the official record of a person's birth.
// It links directly to a Person — every birth record
// must belong to exactly one person in the system.
// REGISTRAR is the primary role that creates and manages these.
// -------------------------------------------------------
@Entity
@Table(name = "birth_records")
public class BirthRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The person this birth record belongs to.
    // ManyToOne because one person has one birth record,
    // but the join column lives on this table.
    // -------------------------------------------------------
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    // Official birth certificate number — unique per record.
    // -------------------------------------------------------
    @Column(nullable = false, unique = true)
    private String certificateNumber;


    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    private String cityOfBirth;

    @Column(nullable = false)
    private String stateOfBirth;

    @Column(nullable = false)
    private String countryOfBirth;

    // Parent information
    // -------------------------------------------------------
    private String motherFirstName;
    private String motherLastName;
    private String fatherFirstName;
    private String fatherLastName;

    // The hospital or location where the birth occurred.
    // -------------------------------------------------------
    private String birthFacility;

    // The registrar who filed this record.
    // Stored as username string — same pattern as AuditLog.
    // -------------------------------------------------------
    @Column(nullable = false)
    private String filedBy;

    @Column(nullable = false)
    private String getFiledBy;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public BirthRecord() {
    }

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

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getCityOfBirth() {
        return cityOfBirth;
    }

    public void setCityOfBirth(String cityOfBirth) {
        this.cityOfBirth = cityOfBirth;
    }

    public String getStateOfBirth() {
        return stateOfBirth;
    }

    public void setStateOfBirth(String stateOfBirth) {
        this.stateOfBirth = stateOfBirth;
    }

    public String getCountryOfBirth() {
        return countryOfBirth;
    }

    public void setCountryOfBirth(String countryOfBirth) {
        this.countryOfBirth = countryOfBirth;
    }

    public String getMotherFirstName() {
        return motherFirstName;
    }

    public void setMotherFirstName(String motherFirstName) {
        this.motherFirstName = motherFirstName;
    }

    public String getMotherLastName() {
        return motherLastName;
    }

    public void setMotherLastName(String motherLastName) {
        this.motherLastName = motherLastName;
    }

    public String getFatherFirstName() {
        return fatherFirstName;
    }

    public void setFatherFirstName(String fatherFirstName) {
        this.fatherFirstName = fatherFirstName;
    }

    public String getFatherLastName() {
        return fatherLastName;
    }

    public void setFatherLastName(String fatherLastName) {
        this.fatherLastName = fatherLastName;
    }

    public String getBirthFacility() {
        return birthFacility;
    }

    public void setBirthFacility(String birthFacility) {
        this.birthFacility = birthFacility;
    }

    public String getFiledBy() {
        return filedBy;
    }

    public void setFiledBy(String filedBy) {
        this.filedBy = filedBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

}
