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
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private void handleConfigureCollege() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private void handleConfigureProgram() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private void handleManageCycles() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private void handleRunSelection() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private void handleViewAnalytics() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
