package com.admission.controller;

import com.admission.model.Admin;
import com.admission.service.AdminService;
import com.admission.service.ApplicationService;
import com.admission.service.SelectionService;
import com.admission.service.AnalyticsService;

import java.util.Scanner;

public class AdminController {

    private final AdminService adminService;
    private final ApplicationService applicationService;
    private final SelectionService selectionService;
    private final AnalyticsService analyticsService;
    private final Scanner scanner;

    public AdminController(AdminService adminService,
                           ApplicationService applicationService,
                           SelectionService selectionService,
                           AnalyticsService analyticsService,
                           Scanner scanner) {
        this.adminService = adminService;
        this.applicationService = applicationService;
        this.selectionService = selectionService;
        this.analyticsService = analyticsService;
        this.scanner = scanner;
    }

    public void showMenu(Admin admin) {
        while (true) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. Configure College");
            System.out.println("2. Configure Program & Subjects");
            System.out.println("3. Manage Admission Cycles");
            System.out.println("4. Run Selection");
            System.out.println("5. View Analytics");
            System.out.println("6. Logout");
            System.out.print("Select an option: ");
            String choice = scanner.nextLine();
            try {
                switch (choice) {
                    case "1": handleConfigureCollege(); break;
                    case "2": handleConfigureProgram(); break;
                    case "3": handleManageCycles(); break;
                    case "4": handleRunSelection(); break;
                    case "5": handleViewAnalytics(); break;
                    case "6": System.out.println("Logging out..."); return;
                    default: System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void handleConfigureCollege() {
        System.out.print("Enter College Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter College Code: ");
        String code = scanner.nextLine();
        System.out.print("Enter College Address: ");
        String address = scanner.nextLine();
        var college = adminService.addCollege(name, code, address);
        System.out.println("College created with ID: " + college.getId());
    }

    private void handleConfigureProgram() {
        System.out.print("Enter College ID for this program: ");
        String colId = scanner.nextLine();
        System.out.print("Enter Department Name: ");
        String dName = scanner.nextLine();
        System.out.print("Enter Department Code: ");
        String dCode = scanner.nextLine();
        var dept = adminService.addDepartment(colId, dName, dCode);
        System.out.println("Department created with ID: " + dept.getId());
        
        System.out.print("Enter Program Name: ");
        String pName = scanner.nextLine();
        System.out.print("Enter Duration (Years): ");
        int duration = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter Total Seats: ");
        int seats = Integer.parseInt(scanner.nextLine());
        var prog = adminService.addProgram(dept.getId(), pName, duration, seats);
        System.out.println("Program created with ID: " + prog.getId());

        System.out.print("Add a Subject to this program? (y/n): ");
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            System.out.print("Enter Subject Name: ");
            String sName = scanner.nextLine();
            System.out.print("Enter Subject Code: ");
            String sCode = scanner.nextLine();
            System.out.print("Enter Semester: ");
            int sem = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter Credits: ");
            int credits = Integer.parseInt(scanner.nextLine());
            var sub = adminService.addSubject(prog.getId(), sName, sCode, sem, credits);
            System.out.println("Subject created with ID: " + sub.getId());
        }
    }

    private void handleManageCycles() {
        System.out.println("1. Create New Admission Cycle");
        System.out.println("2. Change Cycle Status (Active/Inactive)");
        System.out.print("Select: ");
        String choice = scanner.nextLine();
        if ("1".equals(choice)) {
            System.out.print("Enter Program ID: ");
            String progId = scanner.nextLine();
            System.out.print("Enter Year: ");
            int year = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter Seat Count: ");
            int seats = Integer.parseInt(scanner.nextLine());
            var cycle = adminService.createAdmissionCycle(progId, year, seats);
            System.out.println("Cycle created! ID: " + cycle.getId());
        } else if ("2".equals(choice)) {
            System.out.print("Enter Cycle ID: ");
            String cycleId = scanner.nextLine();
            System.out.print("Set to active? (true/false): ");
            boolean active = Boolean.parseBoolean(scanner.nextLine());
            adminService.setCycleActive(cycleId, active);
            System.out.println("Cycle status updated.");
        } else {
            System.out.println("Invalid choice.");
        }
    }

    private void handleRunSelection() {
        System.out.print("Enter Admission Cycle ID to run selection: ");
        String cycleId = scanner.nextLine();
        var selected = selectionService.runSelection(cycleId, new com.admission.strategy.impl.MeritBasedSelectionStrategy());
        System.out.println("Selection completed. Number of processed applications: " + selected.size());
    }

    private void handleViewAnalytics() {
        System.out.print("Enter Program ID for Analytics: ");
        String progId = scanner.nextLine();
        System.out.println("Total Applications: " + analyticsService.getApplicationCountByProgram(progId));
        System.out.println("Enrollment Stats: " + analyticsService.getEnrollmentStats(progId));
    }
}
