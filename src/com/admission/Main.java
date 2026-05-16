package com.admission;
import com.admission.controller.MainController;
import com.admission.repository.csv.*;
import com.admission.service.impl.*;

public class Main {
    public static void main(String[] args) {
        String dataDir = "data";
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

        var authService = new AuthServiceImpl(studentRepo, adminRepo);
        var adminService = new AdminServiceImpl(collegeRepo, deptRepo, progRepo, subjectRepo, cycleRepo);
        var appService = new ApplicationServiceImpl(appRepo, cycleRepo);
        var selService = new SelectionServiceImpl(appRepo, cycleRepo);
        var enrollService = new EnrollmentServiceImpl(enrollRepo, appRepo, subjectRepo, studentSubRepo, cycleRepo);
        var acadService = new AcademicServiceImpl(subjectRepo, studentSubRepo);
        var analService = new AnalyticsServiceImpl(appRepo, enrollRepo, cycleRepo);

        var mainController = new MainController(authService, adminService, appService, selService, enrollService, acadService, analService);
        
        // Ensure an admin exists
        if (adminRepo.findAll().isEmpty()) {
            adminRepo.save(new com.admission.model.Admin(null, "System Admin", "admin@test.com", "admin123"));
        }
        
        mainController.start();
    }
}
