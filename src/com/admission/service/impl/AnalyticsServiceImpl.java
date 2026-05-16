package com.admission.service.impl;

import com.admission.model.AdmissionCycle;
import com.admission.model.Application;
import com.admission.model.Enrollment;
import com.admission.repository.AdmissionCycleRepository;
import com.admission.repository.ApplicationRepository;
import com.admission.repository.EnrollmentRepository;
import com.admission.service.AnalyticsService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AnalyticsServiceImpl implements AnalyticsService {

    private final ApplicationRepository applicationRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final AdmissionCycleRepository cycleRepository;

    public AnalyticsServiceImpl(ApplicationRepository applicationRepository, 
                                EnrollmentRepository enrollmentRepository,
                                AdmissionCycleRepository cycleRepository) {
        this.applicationRepository = applicationRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.cycleRepository = cycleRepository;
    }

    @Override
    public int getApplicationCountByProgram(String programId) {
        Set<String> cycleIds = cycleRepository.findAll().stream()
                .filter(c -> c.getProgramId().equals(programId))
                .map(AdmissionCycle::getId)
                .collect(Collectors.toSet());

        return (int) applicationRepository.findAll().stream()
                .filter(a -> cycleIds.contains(a.getAdmissionCycleId()))
                .count();
    }

    @Override
    public Map<String, Integer> getEnrollmentStats(String programId) {
        List<Enrollment> enrollments = enrollmentRepository.findByProgram(programId);
        Map<String, Integer> stats = new HashMap<>();
        stats.put("Total Enrollments", enrollments.size());
        return stats;
    }
}
