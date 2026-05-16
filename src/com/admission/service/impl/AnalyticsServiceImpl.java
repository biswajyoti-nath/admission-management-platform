package com.admission.service.impl;

import com.admission.repository.ApplicationRepository;
import com.admission.repository.EnrollmentRepository;
import com.admission.service.AnalyticsService;

import java.util.Map;

public class AnalyticsServiceImpl implements AnalyticsService {

    private final ApplicationRepository applicationRepository;
    private final EnrollmentRepository enrollmentRepository;

    public AnalyticsServiceImpl(ApplicationRepository applicationRepository, EnrollmentRepository enrollmentRepository) {
        this.applicationRepository = applicationRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    @Override
    public int getApplicationCountByProgram(String programId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Map<String, Integer> getEnrollmentStats(String programId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
