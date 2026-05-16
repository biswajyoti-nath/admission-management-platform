package com.admission.service;

import com.admission.model.Enrollment;

/**
 * Service interface for managing student enrollment.
 *
 * <p>Encapsulates the business rules for enrolling a selected student:
 * application must be SELECTED, student must not be already enrolled
 * in another program for the same cycle.</p>
 *
 * <p>On successful enrollment, triggers semester-1 subject assignment
 * via StudentSubject records.</p>
 *
 * <p><b>SRP:</b> Responsible solely for enrollment and subject assignment —
 * separated from application and selection concerns.</p>
 */
public interface EnrollmentService {

    /**
     * Enrolls a student based on a selected application.
     *
     * <p>Business rules enforced:</p>
     * <ul>
     *   <li>Application must exist and have status SELECTED</li>
     *   <li>Student must not be already enrolled for the same admission cycle</li>
     * </ul>
     *
     * <p>Side effects on success:</p>
     * <ul>
     *   <li>Creates an Enrollment record</li>
     *   <li>Assigns all semester-1 subjects of the program as StudentSubject records</li>
     * </ul>
     *
     * @param studentId     the student's ID
     * @param applicationId the SELECTED application's ID
     * @return the created Enrollment
     * @throws IllegalArgumentException if any business rule is violated
     */
    Enrollment enroll(String studentId, String applicationId);
}
