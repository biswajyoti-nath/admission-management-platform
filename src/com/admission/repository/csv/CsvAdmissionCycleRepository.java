package com.admission.repository.csv;

import com.admission.model.AdmissionCycle;
import com.admission.repository.AdmissionCycleRepository;

import java.util.List;
import java.util.Optional;

public class CsvAdmissionCycleRepository implements AdmissionCycleRepository {

    private final String filePath;

    public CsvAdmissionCycleRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public AdmissionCycle save(AdmissionCycle entity) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Optional<AdmissionCycle> findById(String id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<AdmissionCycle> findAll() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void updateAll(List<AdmissionCycle> entities) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<AdmissionCycle> findActiveByProgram(String programId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
