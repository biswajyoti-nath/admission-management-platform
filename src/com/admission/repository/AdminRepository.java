package com.admission.repository;

import com.admission.model.Admin;

import java.util.Optional;

/**
 * Repository interface for Admin entity persistence.
 *
 * <p>Extends the base CRUD with an email-based lookup, required for
 * admin login authentication.</p>
 *
 * <p><b>SRP:</b> Responsible solely for Admin data access.</p>
 */
public interface AdminRepository extends Repository<Admin> {

    /**
     * Finds an admin by their email address.
     *
     * <p>Used for login authentication.</p>
     *
     * @param email the admin's email address
     * @return an Optional containing the admin if found, or empty
     */
    Optional<Admin> findByEmail(String email);
}
