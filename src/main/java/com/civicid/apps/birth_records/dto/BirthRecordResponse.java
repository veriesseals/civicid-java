package com.civicid.apps.birth_records.dto;

import com.civicid.apps.birth_records.BirthRecord;
import java.time.LocalDate;
import java.time.LocalDateTime;

// BirthRecordResponse is the shape of data we RETURN to the caller.
// Notice we include personId and the person's name for context,
// but we don't dump the entire Person object — just what's needed.
// -------------------------------------------------------
public class BirthRecordResponse {

    private Long id;
    private Long personId;
    private String personFirstName;
    private String personLastName;
    private String certificateNumber;
    private LocalDate dateOfBirth;
    private String cityOfBirth;
    private String stateOfBirth;
    private String countryOfBirth;
    private String motherFirstName;
    private String motherLastName;
    private String fatherFirstName;
    private String fatherLastName;
    private String birthFacility;
    private String filedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Static factory — converts a BirthRecord entity into
    // a BirthRecordResponse. Same clean pattern as PersonResponse.
    // -------------------------------------------------------
    public static BirthRecordResponse from(BirthRecord birthRecord) {
        BirthRecordResponse response = new BirthRecordResponse();

        response.id = birthRecord.getId();
        response.personId = birthRecord.getPerson().getId();
        response.personFirstName = birthRecord.getPerson().getFirstName();
        response.personLastName = birthRecord.getPerson().getLastName();
        response.certificateNumber = birthRecord.getCertificateNumber();
        response.dateOfBirth = birthRecord.getDateOfBirth();
        response.cityOfBirth = birthRecord.getCityOfBirth();
        response.stateOfBirth = birthRecord.getStateOfBirth();
        response.countryOfBirth = birthRecord.getCountryOfBirth();
        response.motherFirstName = birthRecord.getMotherFirstName();
        response.motherLastName = birthRecord.getMotherLastName();
        response.fatherFirstName = birthRecord.getFatherFirstName();
        response.fatherLastName = birthRecord.getFatherLastName();
        response.birthFacility = birthRecord.getBirthFacility();
        response.filedBy = birthRecord.getFiledBy();
        response.createdAt = birthRecord.getCreatedAt();
        response.updatedAt = birthRecord.getUpdatedAt();

        return response;
    }

    // --- Getters ---

    public Long getId() {
        return id;
    }

    public Long getPersonId() {
        return personId;
    }
    public String getPersonFirstName() {
        return personFirstName;
    }
    public String getPersonLastName() {
        return personLastName;
    }
    public String getCertificateNumber() {
        return certificateNumber;
    }
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    public String getCityOfBirth() {
        return cityOfBirth;
    }
    public String getStateOfBirth() {
        return stateOfBirth;
    }
    public String getCountryOfBirth() {
        return countryOfBirth;
    }
    public String getMotherFirstName() {
        return motherFirstName;
    }
    public String getMotherLastName() {
        return motherLastName;
    }
    public String getFatherFirstName() {
        return fatherFirstName;
    }
    public String getFatherLastName() {
        return fatherLastName;
    }
    public String getBirthFacility() {
        return birthFacility;
    }
    public String getFiledBy() {
        return filedBy;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

// The response includes personId, personFirstName, and personLastName pulled directly from the related Person entity. This is the @ManyToOne relationship paying off — one call to record.getPerson().getFirstName() and you get the person's name without a separate query.
// The caller gets everything they need to understand the record in context — who it belongs to, who filed it, where and when they were born — without getting a raw database dump.

}
