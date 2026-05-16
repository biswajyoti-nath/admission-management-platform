package com.admission.service;

import com.admission.model.AdmissionCycle;
import com.admission.model.College;
import com.admission.model.Department;
import com.admission.model.Program;
import com.admission.model.Subject;

import java.util.List;

/**
 * Service interface for administrative configuration operations.
 *
 * <p>Manages the organizational hierarchy (College → Department → Program →
 * Subject) and admission cycle lifecycle. All configuration is performed
 * by admin users through the CLI.</p>
 *
 * <p><b>SRP:</b> Responsible solely for college/program configuration —
 * selection and enrollment logic belong to their respective services.</p>
 */
public interface AdminService {

    /**
     * Creates a new college.
     *
     * @param name    the college name
     * @param code    the short code (e.g., "NIT-S")
     * @param address the physical address
     * @return the created College with a generated ID
     */
    College addCollege(String name, String code, String address);

    /**
     * Creates a new department under a college.
     *
     * @param collegeId the parent college's ID
     * @param name      the department name
     * @param code      the short code (e.g., "CSE")
     * @return the created Department with a generated ID
     * @throws IllegalArgumentException if the college does not exist
     */
    Department addDepartment(String collegeId, String name, String code);

    /**
     * Creates a new program under a department.
     *
     * @param departmentId  the parent department's ID
     * @param name          the program name (e.g., "B.Tech CSE")
     * @param durationYears the duration in years
     * @param totalSeats    the maximum seat capacity
     * @return the created Program with a generated ID
     * @throws IllegalArgumentException if the department does not exist
     */
    Program addProgram(String departmentId, String name, int durationYears, int totalSeats);

    /**
     * Adds a subject to a program.
     *
     * @param programId the program's ID
     * @param name      the subject name (e.g., "Data Structures")
     * @param code      the subject code (e.g., "CS201")
     * @param semester  the semester number (1-indexed)
     * @param credits   the credit hours
     * @return the created Subject with a generated ID
     * @throws IllegalArgumentException if the program does not exist
     */
    Subject addSubject(String programId, String name, String code, int semester, int credits);

    /**
     * Creates a new admission cycle for a program.
     *
     * @param programId the program's ID
     * @param year      the academic year (e.g., 2026)
     * @param seatCount the number of seats available for this cycle
     * @return the created AdmissionCycle with a generated ID
     * @throws IllegalArgumentException if the program does not exist
     */
    AdmissionCycle createAdmissionCycle(String programId, int year, int seatCount);

    /**
     * Opens or closes an admission cycle.
     *
     * @param cycleId the admission cycle's ID
     * @param active  true to open (accept applications), false to close
     * @return the updated AdmissionCycle
     * @throws IllegalArgumentException if the cycle does not exist
     */
    AdmissionCycle setCycleActive(String cycleId, boolean active);

    /**
     * Retrieves all colleges in the system.
     *
     * @return list of all colleges; empty if none
     */
    List<College> getAllColleges();

    /**
     * Retrieves all departments for a college.
     *
     * @param collegeId the college's ID
     * @return list of departments; empty if none
     */
    List<Department> getDepartmentsByCollege(String collegeId);

    /**
     * Retrieves all programs for a department.
     *
     * @param departmentId the department's ID
     * @return list of programs; empty if none
     */
    List<Program> getProgramsByDepartment(String departmentId);

    /**
     * Retrieves all subjects for a program.
     *
     * @param programId the program's ID
     * @return list of subjects; empty if none
     */
    List<Subject> getSubjectsByProgram(String programId);

    /**
     * Retrieves all admission cycles for a program.
     *
     * @param programId the program's ID
     * @return list of admission cycles; empty if none
     */
    List<AdmissionCycle> getCyclesByProgram(String programId);
}
