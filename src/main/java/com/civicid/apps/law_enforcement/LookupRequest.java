package com.civicid.apps.law_enforcement;

import jakarta.validation.constraints.NotBlank;

// LookupRequest is the body an officer sends when performing
// an identity verification. The reason field is mandatory —
// no reason, no lookup. Period.
// -------------------------------------------------------
public class LookupRequest {

    @NotBlank(message = "A reason must be provided for all identity lookups")
    private String reason;

    public LookupRequest() {}

    public String getReason() {
        return reason;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }

}
