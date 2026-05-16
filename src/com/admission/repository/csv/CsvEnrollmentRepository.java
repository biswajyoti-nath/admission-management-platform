package com.admission.repository.csv;

import com.admission.model.Enrollment;
import com.admission.repository.EnrollmentRepository;

import java.util.List;
import java.util.Optional;

public class CsvEnrollmentRepository implements EnrollmentRepository {

    private final String filePath;

    public CsvEnrollmentRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public Enrollment save(Enrollment entity) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Optional<Enrollment> findById(String id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<Enrollment> findAll() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void updateAll(List<Enrollment> entities) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Optional<Enrollment> findByStudentAndCycle(String studentId, String cycleId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<Enrollment> findByProgram(String programId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
