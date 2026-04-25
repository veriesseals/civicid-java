package com.civicid.apps.persons;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

// Person is the central entity of the entire CivicID system.
// Every other app (birth records, DMV, law enforcement, etc.)
// will have a foreign key pointing back to a Person record.
// Think of this as the "hub" — everything else is a "spoke".
// -------------------------------------------------------

@Entity
@Table(name = "persons")
public class Person {
    
}
