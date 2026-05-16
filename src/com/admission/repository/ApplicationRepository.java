package com.admission.repository;

import com.admission.model.Application;
import com.admission.model.ApplicationStatus;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Application entity persistence.
 *
 * <p>The most query-rich repository — supports lookups by student, cycle,
 * and status, all essential for the application and selection workflows.</p>
 *
 * <p><b>ISP:</b> Each method serves a distinct consumer
 * (ApplicationService, SelectionService, StudentController).</p>
 *
 * <p><b>SRP:</b> Responsible solely for Application data access.</p>
 */
public interface ApplicationRepository extends Repository<Application> {

    /**
     * Finds a student's application to a specific admission cycle.
     *
     * <p>Used to enforce the uniqueness constraint: one application
     * per (studentId + admissionCycleId) pair.</p>
     *
     * @param studentId the student's ID
     * @param cycleId   the admission cycle's ID
     * @return an Optional containing the application if found, or empty
     */
    Optional<Application> findByStudentAndCycle(String studentId, String cycleId);

    /**
     * Finds all applications submitted to a specific admission cycle.
     *
     * <p>Used by admin to view applicants and by SelectionService to
     * run the selection algorithm.</p>
     *
     * @param cycleId the admission cycle's ID
     * @return list of applications for the cycle; empty if none
     */
    List<Application> findByCycle(String cycleId);

    /**
     * Finds all applications for a cycle that have a specific status.
     *
     * <p>Used to retrieve only APPLIED applications for selection,
     * or only SELECTED applications for enrollment reports.</p>
     *
     * @param cycleId the admission cycle's ID
     * @param status  the application status to filter by
     * @return list of matching applications; empty if none
     */
    List<Application> findByStatus(String cycleId, ApplicationStatus status);
}
