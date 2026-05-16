package com.admission.repository;

import com.admission.model.Department;

import java.util.List;

/**
 * Repository interface for Department entity persistence.
 *
 * <p>Extends the base CRUD with a college-scoped query, essential for
 * the admin flow ("list departments of a college").</p>
 *
 * <p><b>SRP:</b> Responsible solely for Department data access.</p>
 */
public interface DepartmentRepository extends Repository<Department> {

    /**
     * Finds all departments belonging to a specific college.
     *
     * @param collegeId the parent college's ID
     * @return list of departments under the college; empty if none
     */
    List<Department> findByCollege(String collegeId);
}
