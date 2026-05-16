package com.admission.service;

import com.admission.model.Application;

import java.util.List;

/**
 * Service interface for managing student applications.
 *
 * <p>Encapsulates the business rules for applying to admission cycles:
 * cycle must be active, no duplicate applications, score must be valid.</p>
 *
 * <p><b>SRP:</b> Responsible solely for application submission and retrieval —
 * selection logic belongs to {@link SelectionService}.</p>
 */
public interface ApplicationService {

    /**
     * Submits a new application for a student to an admission cycle.
     *
     * <p>Business rules enforced:</p>
     * <ul>
     *   <li>Admission cycle must exist and be active</li>
     *   <li>Student must not have an existing application for this cycle</li>
     *   <li>Score must be non-negative</li>
     * </ul>
     *
     * @param studentId the student's ID
     * @param cycleId   the admission cycle's ID
     * @param score     the student's merit score / percentage
     * @return the created Application with status APPLIED
     * @throws IllegalArgumentException if any business rule is violated
     */
    Application apply(String studentId, String cycleId, double score);

    /**
     * Retrieves all applications submitted by a specific student.
     *
     * @param studentId the student's ID
     * @return list of the student's applications; empty if none
     */
    List<Application> getApplicationsByStudent(String studentId);

    /**
     * Retrieves all applications for a specific admission cycle.
     *
     * @param cycleId the admission cycle's ID
     * @return list of applications for the cycle; empty if none
     */
    List<Application> getApplicationsByCycle(String cycleId);
}
