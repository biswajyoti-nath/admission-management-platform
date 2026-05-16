package com.admission.repository.csv;

import com.admission.model.College;
import com.admission.repository.CollegeRepository;

import java.util.List;
import java.util.Optional;

public class CsvCollegeRepository implements CollegeRepository {

    private final String filePath;

    public CsvCollegeRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public College save(College entity) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Optional<College> findById(String id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<College> findAll() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void updateAll(List<College> entities) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
