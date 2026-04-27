package com.civicid.apps.accounts.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;

// PersonRequest is the shape of data we ACCEPT from the caller.
// Validation annotations enforce rules before the data ever
// reaches the service layer — bad data is rejected immediately.
// -------------------------------------------------------

public class PersonRequest {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    // SSN format: 3 digits, dash, 2 digits, dash, 4 digits
    // Example: 123-45-6789
    // -------------------------------------------------------
    @Pattern(
            regexp = "^\\d{3}-\\d{2}-\\d{4}$",
            message = "SSN must be in format 123-45-6789"
    )
    private String ssn;

    private String gender;

    private String address;

    private String city;

    private String state;

    @Pattern(
            regexp = "^\\d{5}(-\\d{4})?$",
            message = "Zip code must be 5 digits or 5+4 format (e.g. 12345 or 12345-6789)"
    )
    private String zipCode;

    // --- Getters & Setters ---

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
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

}
