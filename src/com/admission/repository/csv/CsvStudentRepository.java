package com.admission.repository.csv;

import com.admission.model.Student;
import com.admission.repository.StudentRepository;

import java.util.List;
import java.util.Optional;

public class CsvStudentRepository implements StudentRepository {

    private final String filePath;

    public CsvStudentRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public Student save(Student entity) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Optional<Student> findById(String id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<Student> findAll() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void updateAll(List<Student> entities) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Optional<Student> findByEmail(String email) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
