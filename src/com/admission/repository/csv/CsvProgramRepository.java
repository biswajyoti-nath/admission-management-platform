package com.admission.repository.csv;

import com.admission.model.Program;
import com.admission.repository.ProgramRepository;

import java.util.List;
import java.util.Optional;

public class CsvProgramRepository implements ProgramRepository {

    private final String filePath;

    public CsvProgramRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public Program save(Program entity) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Optional<Program> findById(String id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<Program> findAll() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void updateAll(List<Program> entities) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<Program> findByDepartment(String departmentId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
