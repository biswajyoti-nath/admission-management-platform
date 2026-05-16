package com.admission.repository.csv;

import com.admission.model.Department;
import com.admission.repository.DepartmentRepository;

import java.util.List;
import java.util.Optional;

public class CsvDepartmentRepository implements DepartmentRepository {

    private final String filePath;

    public CsvDepartmentRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public Department save(Department entity) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Optional<Department> findById(String id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<Department> findAll() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void updateAll(List<Department> entities) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<Department> findByCollege(String collegeId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
