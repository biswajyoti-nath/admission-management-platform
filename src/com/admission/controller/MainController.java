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
        System.out.println("Welcome to the Configurable Multi-College Admission & Academic Management System");
        showMainMenu();
    }

    private void showMainMenu() {
        while (true) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. Login");
            System.out.println("2. Register (Student)");
            System.out.println("3. Exit");
            System.out.print("Select an option: ");
            String choice = scanner.nextLine();
            try {
                switch (choice) {
                    case "1":
                        handleLogin();
                        break;
                    case "2":
                        handleRegistration();
                        break;
                    case "3":
                        System.out.println("Exiting... Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void handleLogin() {
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Login as (1) Student or (2) Admin? ");
        String role = scanner.nextLine();
        
        if ("1".equals(role)) {
            var student = authService.loginStudent(email, password)
                    .orElseThrow(() -> new RuntimeException("Student not found"));
            System.out.println("Login successful! Welcome " + student.getName());
            studentController.showMenu(student);
        } else if ("2".equals(role)) {
            var admin = authService.loginAdmin(email, password)
                    .orElseThrow(() -> new RuntimeException("Admin not found"));
            System.out.println("Login successful! Welcome " + admin.getName());
            adminController.showMenu(admin);
        } else {
            System.out.println("Invalid role selection.");
        }
    }

    private void handleRegistration() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter phone: ");
        String phone = scanner.nextLine();
        
        authService.registerStudent(name, email, password, phone);
        System.out.println("Registration successful! You can now log in.");
    }
}
