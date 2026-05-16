package com.admission.repository;

import com.admission.model.Subject;

import java.util.List;

/**
 * Repository interface for Subject entity persistence.
 *
 * <p>Extends the base CRUD with program-scoped and semester-scoped queries
 * needed for subject assignment during enrollment.</p>
 *
 * <p><b>SRP:</b> Responsible solely for Subject data access.</p>
 */
public interface SubjectRepository extends Repository<Subject> {

    /**
     * Finds all subjects belonging to a specific program.
     *
     * @param programId the program's ID
     * @return list of subjects for the program; empty if none
     */
    List<Subject> findByProgram(String programId);

    /**
     * Finds subjects for a program in a specific semester.
     *
     * <p>Used during enrollment to assign only semester-1 subjects
     * to newly enrolled students.</p>
     *
     * @param programId the program's ID
     * @param semester  the semester number (1-indexed)
     * @return list of matching subjects; empty if none
     */
    List<Subject> findByProgramAndSemester(String programId, int semester);
}
