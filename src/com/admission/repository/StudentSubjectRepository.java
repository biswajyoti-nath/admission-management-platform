package com.admission.repository;

import com.admission.model.StudentSubject;

import java.util.List;

/**
 * Repository interface for StudentSubject entity persistence.
 *
 * <p>Extends the base CRUD with a student-scoped query needed for the
 * "View My Enrolled Subjects" student menu option.</p>
 *
 * <p><b>SRP:</b> Responsible solely for StudentSubject data access.</p>
 */
public interface StudentSubjectRepository extends Repository<StudentSubject> {

    /**
     * Finds all subject assignments for a specific student.
     *
     * @param studentId the student's ID
     * @return list of student-subject records; empty if none
     */
    List<StudentSubject> findByStudent(String studentId);
}
