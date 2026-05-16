package com.admission.service.impl;

import com.admission.model.AdmissionCycle;
import com.admission.model.College;
import com.admission.model.Department;
import com.admission.model.Program;
import com.admission.model.Subject;
import com.admission.repository.AdmissionCycleRepository;
import com.admission.repository.CollegeRepository;
import com.admission.repository.DepartmentRepository;
import com.admission.repository.ProgramRepository;
import com.admission.repository.SubjectRepository;
import com.admission.service.AdminService;

import java.util.List;

public class AdminServiceImpl implements AdminService {

    private final CollegeRepository collegeRepository;
    private final DepartmentRepository departmentRepository;
    private final ProgramRepository programRepository;
    private final SubjectRepository subjectRepository;
    private final AdmissionCycleRepository cycleRepository;

    public AdminServiceImpl(CollegeRepository collegeRepository,
                            DepartmentRepository departmentRepository,
                            ProgramRepository programRepository,
                            SubjectRepository subjectRepository,
                            AdmissionCycleRepository cycleRepository) {
        this.collegeRepository = collegeRepository;
        this.departmentRepository = departmentRepository;
        this.programRepository = programRepository;
        this.subjectRepository = subjectRepository;
        this.cycleRepository = cycleRepository;
    }

    @Override
    public College addCollege(String name, String code, String address) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Department addDepartment(String collegeId, String name, String code) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Program addProgram(String departmentId, String name, int durationYears, int totalSeats) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Subject addSubject(String programId, String name, String code, int semester, int credits) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public AdmissionCycle createAdmissionCycle(String programId, int year, int seatCount) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public AdmissionCycle setCycleActive(String cycleId, boolean active) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<College> getAllColleges() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<Department> getDepartmentsByCollege(String collegeId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<Program> getProgramsByDepartment(String departmentId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<Subject> getSubjectsByProgram(String programId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<AdmissionCycle> getCyclesByProgram(String programId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
