package com.admission.service;

import com.admission.model.Admin;
import com.admission.model.Student;

import java.util.Optional;

/**
 * Service interface for user authentication and registration.
 *
 * <p>Handles student registration (with email uniqueness validation)
 * and credential-based login for both students and admins.</p>
 *
 * <p><b>SRP:</b> Responsible solely for authentication concerns —
 * registration, login, credential validation.</p>
 *
 * <p><b>ISP:</b> Separated from AdminService and ApplicationService
 * so controllers only depend on the auth methods they need.</p>
 */
public interface AuthService {

    /**
     * Registers a new student after validating email uniqueness.
     *
     * @param name     the student's full name
     * @param email    the student's email (must be unique)
     * @param password the student's password
     * @param phone    the student's contact number
     * @return the created Student with a generated ID
     * @throws IllegalArgumentException if the email is already registered
     */
    Student registerStudent(String name, String email, String password, String phone);

    /**
     * Authenticates a student by email and password.
     *
     * @param email    the student's email
     * @param password the student's password
     * @return an Optional containing the Student if credentials match, or empty
     */
    Optional<Student> loginStudent(String email, String password);

    /**
     * Authenticates an admin by email and password.
     *
     * @param email    the admin's email
     * @param password the admin's password
     * @return an Optional containing the Admin if credentials match, or empty
     */
    Optional<Admin> loginAdmin(String email, String password);
}
