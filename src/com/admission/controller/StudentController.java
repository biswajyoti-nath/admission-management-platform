package com.admission.controller;

import com.admission.model.Student;
import com.admission.service.ApplicationService;
import com.admission.service.EnrollmentService;
import com.admission.service.AcademicService;
import com.admission.service.AdminService;

import java.util.Scanner;

public class StudentController {

    private final ApplicationService applicationService;
    private final EnrollmentService enrollmentService;
    private final AcademicService academicService;
    private final AdminService adminService;
    private final Scanner scanner;

    public StudentController(ApplicationService applicationService,
                             EnrollmentService enrollmentService,
                             AcademicService academicService,
                             AdminService adminService,
                             Scanner scanner) {
        this.applicationService = applicationService;
        this.enrollmentService = enrollmentService;
        this.academicService = academicService;
        this.adminService = adminService;
        this.scanner = scanner;
    }

    public void showMenu(Student student) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private void handleViewPrograms() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private void handleApplyForProgram(Student student) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private void handleViewApplications(Student student) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private void handleEnroll(Student student) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private void handleViewAcademics(Student student) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
