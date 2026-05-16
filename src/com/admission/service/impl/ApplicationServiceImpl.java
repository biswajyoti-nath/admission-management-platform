package com.admission.service.impl;

import com.admission.model.Application;
import com.admission.repository.AdmissionCycleRepository;
import com.admission.repository.ApplicationRepository;
import com.admission.service.ApplicationService;

import java.util.List;

public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final AdmissionCycleRepository cycleRepository;

    public ApplicationServiceImpl(ApplicationRepository applicationRepository, AdmissionCycleRepository cycleRepository) {
        this.applicationRepository = applicationRepository;
        this.cycleRepository = cycleRepository;
    }

    @Override
    public Application apply(String studentId, String cycleId, double score) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<Application> getApplicationsByStudent(String studentId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<Application> getApplicationsByCycle(String cycleId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
