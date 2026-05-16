package com.admission.repository;

import com.admission.model.Enrollment;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Enrollment entity persistence.
 *
 * <p>Extends the base CRUD with a student-and-cycle lookup needed to
 * enforce the constraint: a student can enroll in at most one program
 * per admission cycle.</p>
 *
 * <p><b>SRP:</b> Responsible solely for Enrollment data access.</p>
 */
public interface EnrollmentRepository extends Repository<Enrollment> {

    /**
     * Checks whether a student is already enrolled via any application
     * belonging to a specific admission cycle.
     *
     * <p>The implementation resolves the cycle relationship through the
     * associated application's admissionCycleId.</p>
     *
     * @param studentId the student's ID
     * @param cycleId   the admission cycle's ID
     * @return an Optional containing the enrollment if found, or empty
     */
    Optional<Enrollment> findByStudentAndCycle(String studentId, String cycleId);

    /**
     * Finds all enrollments for a specific program.
     *
     * <p>Used by admin reports ("View Enrollments for Program").</p>
     *
     * @param programId the program's ID
     * @return list of enrollments for the program; empty if none
     */
    List<Enrollment> findByProgram(String programId);
}
