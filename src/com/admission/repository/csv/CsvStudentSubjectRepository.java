package com.admission.repository.csv;

import com.admission.model.StudentSubject;
import com.admission.repository.StudentSubjectRepository;

import java.util.List;
import java.util.Optional;

public class CsvStudentSubjectRepository implements StudentSubjectRepository {

    private final String filePath;

    public CsvStudentSubjectRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public StudentSubject save(StudentSubject entity) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Optional<StudentSubject> findById(String id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<StudentSubject> findAll() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void updateAll(List<StudentSubject> entities) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<StudentSubject> findByStudent(String studentId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
