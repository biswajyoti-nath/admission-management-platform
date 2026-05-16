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
import java.util.Optional;

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
        College college = new College(null, name, code, address);
        return collegeRepository.save(college);
    }

    @Override
    public Department addDepartment(String collegeId, String name, String code) {
        Optional<College> collegeOpt = collegeRepository.findById(collegeId);
        if (collegeOpt.isEmpty()) {
            throw new RuntimeException("College not found");
        }
        Department department = new Department(null, collegeId, name, code);
        return departmentRepository.save(department);
    }

    @Override
    public Program addProgram(String departmentId, String name, int durationYears, int totalSeats) {
        Optional<Department> deptOpt = departmentRepository.findById(departmentId);
        if (deptOpt.isEmpty()) {
            throw new RuntimeException("Department not found");
        }
        Program program = new Program(null, departmentId, name, durationYears, totalSeats);
        return programRepository.save(program);
    }

    @Override
    public Subject addSubject(String programId, String name, String code, int semester, int credits) {
        Optional<Program> progOpt = programRepository.findById(programId);
        if (progOpt.isEmpty()) {
            throw new RuntimeException("Program not found");
        }
        Subject subject = new Subject(null, programId, name, code, semester, credits);
        return subjectRepository.save(subject);
    }

    @Override
    public AdmissionCycle createAdmissionCycle(String programId, int year, int seatCount) {
        Optional<Program> progOpt = programRepository.findById(programId);
        if (progOpt.isEmpty()) {
            throw new RuntimeException("Program not found");
        }
        AdmissionCycle cycle = new AdmissionCycle(null, programId, year, seatCount, true);
        return cycleRepository.save(cycle);
    }

    @Override
    public AdmissionCycle setCycleActive(String cycleId, boolean active) {
        Optional<AdmissionCycle> cycleOpt = cycleRepository.findById(cycleId);
        if (cycleOpt.isEmpty()) {
            throw new RuntimeException("Admission cycle not found");
        }
        AdmissionCycle cycle = cycleOpt.get();
        cycle.setActive(active);
        
        List<AdmissionCycle> allCycles = cycleRepository.findAll();
        for (int i = 0; i < allCycles.size(); i++) {
            if (allCycles.get(i).getId().equals(cycleId)) {
                allCycles.set(i, cycle);
                break;
            }
        }
        cycleRepository.updateAll(allCycles);
        return cycle;
    }

    @Override
    public List<College> getAllColleges() {
        return collegeRepository.findAll();
    }

    @Override
    public List<Department> getDepartmentsByCollege(String collegeId) {
        return departmentRepository.findByCollege(collegeId);
    }

    @Override
    public List<Program> getProgramsByDepartment(String departmentId) {
        return programRepository.findByDepartment(departmentId);
    }

    @Override
    public List<Subject> getSubjectsByProgram(String programId) {
        return subjectRepository.findByProgram(programId);
    }

    @Override
    public List<AdmissionCycle> getCyclesByProgram(String programId) {
        return cycleRepository.findAll().stream()
                .filter(cycle -> cycle.getProgramId().equals(programId))
                .toList();
    }
}
