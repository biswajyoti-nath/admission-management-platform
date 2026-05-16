package com.admission.repository.csv;

import com.admission.model.Application;
import com.admission.model.ApplicationStatus;
import com.admission.repository.ApplicationRepository;

import java.util.List;
import java.util.Optional;

public class CsvApplicationRepository implements ApplicationRepository {

    private final String filePath;

    public CsvApplicationRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public Application save(Application entity) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Optional<Application> findById(String id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<Application> findAll() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void updateAll(List<Application> entities) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Optional<Application> findByStudentAndCycle(String studentId, String cycleId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<Application> findByCycle(String cycleId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<Application> findByStatus(String cycleId, ApplicationStatus status) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
