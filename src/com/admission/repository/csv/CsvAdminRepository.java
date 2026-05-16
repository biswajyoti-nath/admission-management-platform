package com.admission.repository.csv;

import com.admission.model.Admin;
import com.admission.repository.AdminRepository;

import java.util.List;
import java.util.Optional;

public class CsvAdminRepository implements AdminRepository {

    private final String filePath;

    public CsvAdminRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public Admin save(Admin entity) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Optional<Admin> findById(String id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<Admin> findAll() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void updateAll(List<Admin> entities) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Optional<Admin> findByEmail(String email) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
