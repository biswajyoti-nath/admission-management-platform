package com.admission.service.impl;

import com.admission.model.AdmissionCycle;
import com.admission.model.Application;
import com.admission.model.ApplicationStatus;
import com.admission.repository.AdmissionCycleRepository;
import com.admission.repository.ApplicationRepository;
import com.admission.service.ApplicationService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final AdmissionCycleRepository cycleRepository;

    public ApplicationServiceImpl(ApplicationRepository applicationRepository, AdmissionCycleRepository cycleRepository) {
        this.applicationRepository = applicationRepository;
        this.cycleRepository = cycleRepository;
    }

    @Override
    public Application apply(String studentId, String cycleId, double score) {
        Optional<AdmissionCycle> cycleOpt = cycleRepository.findById(cycleId);
        if (cycleOpt.isEmpty()) {
            throw new RuntimeException("Admission cycle not found");
        }
        AdmissionCycle cycle = cycleOpt.get();
        if (!cycle.isActive()) {
            throw new RuntimeException("Admission cycle is not active");
        }

        Optional<Application> existingOpt = applicationRepository.findByStudentAndCycle(studentId, cycleId);
        if (existingOpt.isPresent()) {
            throw new RuntimeException("Application already exists for this cycle");
        }

        Application application = new Application(
                null,
                studentId,
                cycleId,
                score,
                ApplicationStatus.APPLIED,
                LocalDate.now().toString()
        );
        return applicationRepository.save(application);
    }

    @Override
    public List<Application> getApplicationsByStudent(String studentId) {
        return applicationRepository.findAll().stream()
                .filter(app -> app.getStudentId().equals(studentId))
                .toList();
    }

    @Override
    public List<Application> getApplicationsByCycle(String cycleId) {
        return applicationRepository.findByCycle(cycleId);
    }
}
