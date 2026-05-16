package com.admission.service.impl;

import com.admission.model.Application;
import com.admission.repository.AdmissionCycleRepository;
import com.admission.repository.ApplicationRepository;
import com.admission.service.SelectionService;
import com.admission.strategy.SelectionStrategy;

import java.util.List;

public class SelectionServiceImpl implements SelectionService {

    private final ApplicationRepository applicationRepository;
    private final AdmissionCycleRepository cycleRepository;

    public SelectionServiceImpl(ApplicationRepository applicationRepository, AdmissionCycleRepository cycleRepository) {
        this.applicationRepository = applicationRepository;
        this.cycleRepository = cycleRepository;
    }

    @Override
    public List<Application> runSelection(String cycleId, SelectionStrategy strategy) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
