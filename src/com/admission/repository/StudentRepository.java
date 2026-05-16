package com.admission.repository;

import com.admission.model.Student;

import java.util.Optional;

/**
 * Repository interface for Student entity persistence.
 *
 * <p>Extends the base CRUD with an email-based lookup, required for
 * login authentication and duplicate-email validation during registration.</p>
 *
 * <p><b>SRP:</b> Responsible solely for Student data access.</p>
 */
public interface StudentRepository extends Repository<Student> {

    /**
     * Finds a student by their email address.
     *
     * <p>Used for login and uniqueness checks during registration.</p>
     *
     * @param email the student's email address
     * @return an Optional containing the student if found, or empty
     */
    Optional<Student> findByEmail(String email);
}
