package com.admission;

import com.admission.model.*;
import com.admission.repository.csv.*;
import com.admission.service.impl.*;
import com.admission.strategy.impl.MeritBasedSelectionStrategy;

import java.io.File;
import java.util.List;

public class QATest {
    public static void main(String[] args) {
        System.out.println("--- Starting QA End-to-End Test ---");
        
        // Clean data directory for a fresh test
        String dataDir = "qa_data";
        File dir = new File(dataDir);
        if (dir.exists()) {
            for (File f : dir.listFiles()) f.delete();
        } else {
            dir.mkdir();
        }

        // Initialize Repositories
        var studentRepo = new CsvStudentRepository(dataDir + "/students.csv");
        var adminRepo = new CsvAdminRepository(dataDir + "/admins.csv");
        var collegeRepo = new CsvCollegeRepository(dataDir + "/colleges.csv");
        var deptRepo = new CsvDepartmentRepository(dataDir + "/departments.csv");
        var progRepo = new CsvProgramRepository(dataDir + "/programs.csv");
        var subjectRepo = new CsvSubjectRepository(dataDir + "/subjects.csv");
        var cycleRepo = new CsvAdmissionCycleRepository(dataDir + "/cycles.csv");
        var appRepo = new CsvApplicationRepository(dataDir + "/applications.csv");
        var enrollRepo = new CsvEnrollmentRepository(dataDir + "/enrollments.csv");
        var studentSubRepo = new CsvStudentSubjectRepository(dataDir + "/student_subjects.csv");

        // Initialize Services
        var authService = new AuthServiceImpl(studentRepo, adminRepo);
        var adminService = new AdminServiceImpl(collegeRepo, deptRepo, progRepo, subjectRepo, cycleRepo);
        var appService = new ApplicationServiceImpl(appRepo, cycleRepo);
        var selService = new SelectionServiceImpl(appRepo, cycleRepo);
        var enrollService = new EnrollmentServiceImpl(enrollRepo, appRepo, subjectRepo, studentSubRepo, cycleRepo);
        var acadService = new AcademicServiceImpl(subjectRepo, studentSubRepo);
        var analService = new AnalyticsServiceImpl(appRepo, enrollRepo, cycleRepo);

        int passed = 0;
        int failed = 0;

        try {
            // PHASE 1: System Setup
            System.out.println("\n[PHASE 1] System Setup (Admin)");
            College tc = adminService.addCollege("Test College", "TC01", "Address");
            Department dept = adminService.addDepartment(tc.getId(), "Computer Science", "CSE");
            Program prog = adminService.addProgram(dept.getId(), "BTech CSE", 4, 2);
            adminService.addSubject(prog.getId(), "Mathematics I", "M1", 1, 4);
            adminService.addSubject(prog.getId(), "Programming in C", "PC", 1, 3);
            adminService.addSubject(prog.getId(), "Physics", "PH", 1, 3);
            
            AdmissionCycle cycle = adminService.createAdmissionCycle(prog.getId(), 2026, 2);
            adminService.setCycleActive(cycle.getId(), true);
            System.out.println("✔ System Setup Completed");
            passed++;

            // PHASE 2: Student Registration
            System.out.println("\n[PHASE 2] Student Registration");
            Student alice = authService.registerStudent("Alice", "alice@test.com", "pass123", "111");
            Student bob = authService.registerStudent("Bob", "bob@test.com", "pass123", "222");
            Student charlie = authService.registerStudent("Charlie", "charlie@test.com", "pass123", "333");
            
            try {
                authService.registerStudent("Duplicate", "alice@test.com", "pass", "444");
                System.out.println("✖ Duplicate email registration should have failed");
                failed++;
            } catch (RuntimeException e) {
                System.out.println("✔ Duplicate email prevented successfully");
                passed++;
            }

            // PHASE 3: Application Flow
            System.out.println("\n[PHASE 3] Application Flow");
            Application appA = appService.apply(alice.getId(), cycle.getId(), 90.0);
            Application appB = appService.apply(bob.getId(), cycle.getId(), 80.0);
            Application appC = appService.apply(charlie.getId(), cycle.getId(), 70.0);
            
            try {
                appService.apply(alice.getId(), cycle.getId(), 95.0);
                System.out.println("✖ Duplicate application should have failed");
                failed++;
            } catch (RuntimeException e) {
                System.out.println("✔ Duplicate application prevented successfully");
                passed++;
            }

            adminService.setCycleActive(cycle.getId(), false);
            try {
                Student d = authService.registerStudent("Dave", "dave@test.com", "pass123", "444");
                appService.apply(d.getId(), cycle.getId(), 60.0);
                System.out.println("✖ Application to inactive cycle should have failed");
                failed++;
            } catch (RuntimeException e) {
                System.out.println("✔ Application to inactive cycle prevented successfully");
                passed++;
            }
            // re-activate
            adminService.setCycleActive(cycle.getId(), true);

            // PHASE 4: Selection
            System.out.println("\n[PHASE 4] Selection");
            List<Application> selectedApps = selService.runSelection(cycle.getId(), new MeritBasedSelectionStrategy());
            
            appA = appRepo.findById(appA.getId()).get();
            appB = appRepo.findById(appB.getId()).get();
            appC = appRepo.findById(appC.getId()).get();

            if (appA.getStatus() == ApplicationStatus.SELECTED && 
                appB.getStatus() == ApplicationStatus.SELECTED && 
                appC.getStatus() == ApplicationStatus.REJECTED) {
                System.out.println("✔ Selection logic worked correctly");
                passed++;
            } else {
                System.out.println("✖ Selection logic failed! Status A: " + appA.getStatus() + ", B: " + appB.getStatus() + ", C: " + appC.getStatus());
                failed++;
            }

            // PHASE 5: Enrollment
            System.out.println("\n[PHASE 5] Enrollment");
            Enrollment enrA = enrollService.enroll(alice.getId(), appA.getId());
            Enrollment enrB = enrollService.enroll(bob.getId(), appB.getId());
            System.out.println("✔ Alice and Bob enrolled successfully");
            passed++;

            try {
                enrollService.enroll(charlie.getId(), appC.getId());
                System.out.println("✖ Charlie should not be able to enroll (REJECTED)");
                failed++;
            } catch (RuntimeException e) {
                System.out.println("✔ Unauthorized enrollment prevented successfully");
                passed++;
            }

            try {
                enrollService.enroll(alice.getId(), appA.getId());
                System.out.println("✖ Alice double enrollment should have failed");
                failed++;
            } catch (RuntimeException e) {
                System.out.println("✔ Double enrollment prevented successfully");
                passed++;
            }

            // PHASE 6: Subject Assignment
            System.out.println("\n[PHASE 6] Subject Assignment");
            List<Subject> aSubjects = acadService.getSubjectsByStudent(alice.getId());
            if (aSubjects.size() == 3) {
                System.out.println("✔ Alice was correctly assigned 3 subjects");
                passed++;
            } else {
                System.out.println("✖ Alice has wrong number of subjects: " + aSubjects.size());
                failed++;
            }

            // PHASE 7: Analytics
            System.out.println("\n[PHASE 7] Analytics");
            int appCount = analService.getApplicationCountByProgram(prog.getId());
            if (appCount == 3) {
                System.out.println("✔ Analytics application count correct: " + appCount);
                passed++;
            } else {
                System.out.println("✖ Analytics application count incorrect: " + appCount);
                failed++;
            }

            int enrCount = analService.getEnrollmentStats(prog.getId()).get("Total Enrollments");
            if (enrCount == 2) {
                System.out.println("✔ Analytics enrollment stats correct: " + enrCount);
                passed++;
            } else {
                System.out.println("✖ Analytics enrollment stats incorrect: " + enrCount);
                failed++;
            }

            // PHASE 8: Error Handling Tests
            System.out.println("\n[PHASE 8] Additional Error Handling");
            try {
                authService.loginStudent("alice@test.com", "wrongpass").orElseThrow(() -> new RuntimeException("Invalid credentials"));
                System.out.println("✖ Login with wrong password should fail");
                failed++;
            } catch (RuntimeException e) {
                System.out.println("✔ Invalid login prevented");
                passed++;
            }

            try {
                enrollService.enroll(alice.getId(), "INVALID_APP_ID");
                System.out.println("✖ Enroll with invalid app ID should fail");
                failed++;
            } catch (RuntimeException e) {
                System.out.println("✔ Invalid enrollment APP ID prevented");
                passed++;
            }

            try {
                adminService.addDepartment("INVALID_COL_ID", "Test", "T");
                System.out.println("✖ Add department with invalid col ID should fail");
                failed++;
            } catch (RuntimeException e) {
                System.out.println("✔ Invalid department foreign key prevented");
                passed++;
            }

            System.out.println("\n--- FINAL RESULTS ---");
            System.out.println("Total Tests Run: " + (passed + failed));
            System.out.println("Passed: " + passed);
            System.out.println("Failed: " + failed);

            if (failed == 0) {
                System.out.println("STATUS: SUCCESS! The system is solid.");
            } else {
                System.out.println("STATUS: FAILED. Check the logs above.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("✖ Critical System Crash: " + e.getMessage());
        }
    }
}
