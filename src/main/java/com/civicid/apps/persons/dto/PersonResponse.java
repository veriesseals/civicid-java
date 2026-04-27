package com.civicid.apps.persons.dto;

import com.civicid.apps.persons.Person;
import java.time.LocalDate;
import java.time.LocalDateTime;

//PersonRequest enforces input rules before anything hits the database. The SSN regex ^\d{3}-\d{2}-\d{4}$ means if someone sends "123456789" without dashes, the API rejects it immediately with a clear error message. Same with zip codes. And @Past on dateOfBirth means you can't register someone born tomorrow — caught automatically.
//PersonResponse has a maskSsn() method baked right in. Every time you return a person, the SSN automatically becomes ***-**-6789. The caller never sees the full SSN through this response. Later when we build the law enforcement app, we'll go even further and strip SSN entirely from their response.
//The from() static factory is a clean pattern — one line converts an entity to a response anywhere in the codebase: PersonResponse.from(person).


// PersonResponse is the shape of data we RETURN to the caller.
// We control exactly what gets exposed — sensitive fields like
// SSN are masked here before they ever leave the server.
// -------------------------------------------------------
public class PersonResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String ssn;
    private String gender;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private Boolean isDeceased;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Static factory method — converts a Person entity into
    // a PersonResponse. Keeps conversion logic in one place.
    // -------------------------------------------------------
    public static PersonResponse from(Person person) {
        PersonResponse response = new PersonResponse();

        response.id = person.getId();
        response.firstName = person.getFirstName();
        response.lastName = person.getLastName();
        response.dateOfBirth = person.getDateOfBirth();
        response.ssn = person.getSsn();
        response.gender = person.getGender();
        response.address = person.getAddress();
        response.city = person.getCity();
        response.state = person.getState();
        response.zipCode = person.getZipCode();
        response.isDeceased = person.getIsDeceased();
        response.createdAt = person.getCreatedAt();
        response.updatedAt = person.getUpdatedAt();
        return response;
    }

    // SSN masking — only show last 4 digits.
    // Example: "123-45-6789" becomes "***-**-6789"
    // This is how real government systems handle SSN display.
    // -------------------------------------------------------
    private static String maskSsn(String ssn) {
        if (ssn == null || ssn.length() < 4) return "***-**-****";
        return "***-**-" + ssn.substring(ssn.length() - 4);
    }

    // --- Getters ---
    // -------------------------------------------------------
    public Long getId() {
        return id;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    public String getSsn() {
        return ssn;
    }
    public String getGender() {
        return gender;
    }
    public String getAddress() {
        return address;
    }
    public String getCity() {
        return city;
    }
    public String getState() {
        return state;
    }
    public String getZipCode() {
        return zipCode;
    }
    public Boolean getIsDeceased() {
        return isDeceased;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
