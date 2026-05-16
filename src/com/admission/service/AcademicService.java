package com.admission.service;

import com.admission.model.Subject;

import java.util.List;

/**
 * Service interface for academic subject queries.
 *
 * <p>Provides a read-only view of a student's assigned subjects by
 * resolving the StudentSubject → Subject relationship.</p>
 *
 * <p><b>ISP:</b> Separated from EnrollmentService — controllers that
 * only need to display subjects don't depend on enrollment logic.</p>
 */
public interface AcademicService {

    /**
     * Retrieves all subjects assigned to a student.
     *
     * <p>Resolves StudentSubject records to their full Subject details
     * (name, code, semester, credits).</p>
     *
     * @param studentId the student's ID
     * @return list of subjects assigned to the student; empty if none
     */
    List<Subject> getSubjectsByStudent(String studentId);
}
