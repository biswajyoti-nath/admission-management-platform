package com.admission.repository.csv;

import com.admission.model.Subject;
import com.admission.repository.SubjectRepository;

import java.util.List;
import java.util.Optional;

public class CsvSubjectRepository implements SubjectRepository {

    private final String filePath;

    public CsvSubjectRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public Subject save(Subject entity) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Optional<Subject> findById(String id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<Subject> findAll() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void updateAll(List<Subject> entities) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<Subject> findByProgram(String programId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<Subject> findByProgramAndSemester(String programId, int semester) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
