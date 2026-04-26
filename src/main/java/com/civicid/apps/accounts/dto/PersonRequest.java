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
    
}
