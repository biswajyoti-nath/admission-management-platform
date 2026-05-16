package com.admission.repository;

import com.admission.model.Program;

import java.util.List;

/**
 * Repository interface for Program entity persistence.
 *
 * <p>Extends the base CRUD with a department-scoped query, essential for
 * the admin flow ("list programs of a department") and student browsing.</p>
 *
 * <p><b>SRP:</b> Responsible solely for Program data access.</p>
 */
public interface ProgramRepository extends Repository<Program> {

    /**
     * Finds all programs belonging to a specific department.
     *
     * @param departmentId the parent department's ID
     * @return list of programs under the department; empty if none
     */
    List<Program> findByDepartment(String departmentId);
}
