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
        while (true) {
            System.out.println("\n--- Student Menu ---");
            System.out.println("1. View Programs");
            System.out.println("2. Apply for Program");
            System.out.println("3. View My Applications");
            System.out.println("4. Enroll");
            System.out.println("5. View My Subjects");
            System.out.println("6. Logout");
            System.out.print("Select an option: ");
            String choice = scanner.nextLine();
            try {
                switch (choice) {
                    case "1": handleViewPrograms(); break;
                    case "2": handleApplyForProgram(student); break;
                    case "3": handleViewApplications(student); break;
                    case "4": handleEnroll(student); break;
                    case "5": handleViewAcademics(student); break;
                    case "6": System.out.println("Logging out..."); return;
                    default: System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void handleViewPrograms() {
        System.out.print("Enter Department ID to view its programs: ");
        String deptId = scanner.nextLine();
        var programs = adminService.getProgramsByDepartment(deptId);
        if (programs.isEmpty()) {
            System.out.println("No programs found for this department.");
        } else {
            programs.forEach(p -> System.out.println("Program ID: " + p.getId() + " | Name: " + p.getName()));
        }
    }

    private void handleApplyForProgram(Student student) {
        System.out.print("Enter Admission Cycle ID: ");
        String cycleId = scanner.nextLine();
        System.out.print("Enter your score: ");
        double score = Double.parseDouble(scanner.nextLine());
        var application = applicationService.apply(student.getId(), cycleId, score);
        System.out.println("Application submitted successfully! Application ID: " + application.getId());
    }

    private void handleViewApplications(Student student) {
        var apps = applicationService.getApplicationsByStudent(student.getId());
        if (apps.isEmpty()) {
            System.out.println("No applications found.");
        } else {
            apps.forEach(a -> System.out.println("App ID: " + a.getId() + " | Cycle ID: " + a.getAdmissionCycleId() + " | Status: " + a.getStatus()));
        }
    }

    private void handleEnroll(Student student) {
        System.out.print("Enter Application ID to enroll: ");
        String appId = scanner.nextLine();
        var enrollment = enrollmentService.enroll(student.getId(), appId);
        System.out.println("Successfully enrolled! Enrollment ID: " + enrollment.getId());
    }

    private void handleViewAcademics(Student student) {
        var subjects = academicService.getSubjectsByStudent(student.getId());
        if (subjects.isEmpty()) {
            System.out.println("No subjects assigned yet.");
        } else {
            subjects.forEach(s -> System.out.println("Subject ID: " + s.getId() + " | Code: " + s.getCode() + " | Name: " + s.getName()));
        }
    }
}
