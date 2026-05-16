package com.admission.service.impl;

import com.admission.model.AdmissionCycle;
import com.admission.model.Application;
import com.admission.model.ApplicationStatus;
import com.admission.repository.AdmissionCycleRepository;
import com.admission.repository.ApplicationRepository;
import com.admission.service.SelectionService;
import com.admission.strategy.SelectionStrategy;

import java.util.List;
import java.util.Optional;

public class SelectionServiceImpl implements SelectionService {

    private final ApplicationRepository applicationRepository;
    private final AdmissionCycleRepository cycleRepository;

    public SelectionServiceImpl(ApplicationRepository applicationRepository, AdmissionCycleRepository cycleRepository) {
        this.applicationRepository = applicationRepository;
        this.cycleRepository = cycleRepository;
    }

    @Override
    public List<Application> runSelection(String cycleId, SelectionStrategy strategy) {
        Optional<AdmissionCycle> cycleOpt = cycleRepository.findById(cycleId);
        if (cycleOpt.isEmpty()) {
            throw new RuntimeException("Admission cycle not found");
        }
        AdmissionCycle cycle = cycleOpt.get();

        List<Application> appliedApplications = applicationRepository.findByStatus(cycleId, ApplicationStatus.APPLIED);
        if (appliedApplications.isEmpty()) {
            throw new RuntimeException("No applications to process");
        }

        strategy.select(appliedApplications, cycle.getSeatCount());

        List<Application> allApplications = applicationRepository.findAll();
        for (int i = 0; i < allApplications.size(); i++) {
            Application app = allApplications.get(i);
            if (app.getAdmissionCycleId().equals(cycleId) && app.getStatus() == ApplicationStatus.APPLIED) {
                for (Application updatedApp : appliedApplications) {
                    if (updatedApp.getId().equals(app.getId())) {
                        allApplications.set(i, updatedApp);
                        break;
                    }
                }
            }
        }
        
        applicationRepository.updateAll(allApplications);
        return appliedApplications;
    }
}
