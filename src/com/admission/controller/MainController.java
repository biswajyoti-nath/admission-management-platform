package com.admission.controller;

import com.admission.service.AuthService;
import com.admission.service.AdminService;
import com.admission.service.ApplicationService;
import com.admission.service.SelectionService;
import com.admission.service.EnrollmentService;
import com.admission.service.AcademicService;
import com.admission.service.AnalyticsService;

import java.util.Scanner;

public class MainController {

    private final AuthService authService;
    private final AdminController adminController;
    private final StudentController studentController;
    private final Scanner scanner;

    public MainController(AuthService authService,
                          AdminService adminService,
                          ApplicationService applicationService,
                          SelectionService selectionService,
                          EnrollmentService enrollmentService,
                          AcademicService academicService,
                          AnalyticsService analyticsService) {
        this.authService = authService;
        this.scanner = new Scanner(System.in);
        this.adminController = new AdminController(adminService, applicationService, selectionService, analyticsService, scanner);
        this.studentController = new StudentController(applicationService, enrollmentService, academicService, adminService, scanner);
    }

    public void start() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private void showMainMenu() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private void handleLogin() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private void handleRegistration() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
