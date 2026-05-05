package com.civicid.apps.birth_records;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

// findByCertificateNumber — before creating a new record, the service will call this to make sure that certificate number doesn't already exist. Duplicate certificate numbers would be a serious data integrity problem.
// findByPersonId — lets you pull up all birth records tied to a specific person. Should only ever return one, but returning a list protects against edge cases.
// findByFiledBy — lets a supervisor or auditor pull up every record a specific registrar has filed. Real accountability tooling.
// existsByPersonId — a fast boolean check. Instead of fetching the full record just to see if one exists, this returns true/false instantly. Much more efficient when you just need to know "does this person already have a birth record?"

// BirthRecordRepository gives us database access for birth records.
// Spring generates all SQL automatically from the method names.
// -------------------------------------------------------
public interface BirthRecordRepository extends JpaRepository<BirthRecord, Long> {

    // Find a birth record by its official certificate number.
    // Used to prevent duplicate certificate numbers at creation.
    // -------------------------------------------------------
    Optional<BirthRecord> findByCertificateNumber(String certificateNumber);

    // Find all birth records for a specific person.
    // A person should only have one, but we return a list
    // to handle edge cases and data integrity checks.
    // -------------------------------------------------------
    List<BirthRecord> findByPersonId(Long personId);

    // Find all records filed by a specific registrar.
    // Useful for auditing a registrar's work history.
    // -------------------------------------------------------
    List<BirthRecord> findByFiledBy(String filedBy);

    // Check if a birth record already exists for a person.
    // Used to prevent duplicate birth records for the same person.
    // -------------------------------------------------------
    boolean existsByPersonId(Long personId);

    // JOIN FETCH overrides used by getAllBirthRecords and getBirthRecordById
    // to eagerly load the Person association and avoid LazyInitializationException.
    @Query("SELECT br FROM BirthRecord br JOIN FETCH br.person")
    List<BirthRecord> findAllWithPerson();

    @Query("SELECT br FROM BirthRecord br JOIN FETCH br.person WHERE br.id = :id")
    Optional<BirthRecord> findByIdWithPerson(@Param("id") Long id);
}

