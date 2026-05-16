package com.admission.repository.csv;

import com.admission.model.Admin;
import com.admission.repository.AdminRepository;
import com.admission.utils.CsvUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CsvAdminRepository implements AdminRepository {

    private final String filePath;
    private static final String[] HEADERS = {"id", "name", "email", "password"};
    private static final String ID_PREFIX = "ADM";

    public CsvAdminRepository(String filePath) {
        this.filePath = filePath;
        CsvUtil.ensureFile(filePath, HEADERS);
    }

    private String generateId() {
        List<String[]> data = CsvUtil.readAll(filePath);
        int max = 0;
        for (String[] row : data) {
            if (row.length > 0) {
                String[] parts = row[0].split("-");
                if (parts.length == 2) {
                    try {
                        int num = Integer.parseInt(parts[1]);
                        if (num > max) max = num;
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
        }
        return String.format("%s-%04d", ID_PREFIX, max + 1);
    }

    private String[] toRow(Admin admin) {
        return new String[]{
                admin.getId(),
                admin.getName(),
                admin.getEmail(),
                admin.getPassword()
        };
    }

    private Admin fromRow(String[] row) {
        if (row.length < 4) return null;
        return new Admin(row[0], row[1], row[2], row[3]);
    }

    @Override
    public Admin save(Admin entity) {
        if (entity.getId() == null || entity.getId().isEmpty()) {
            entity.setId(generateId());
        }
        CsvUtil.append(filePath, toRow(entity));
        return entity;
    }

    @Override
    public Optional<Admin> findById(String id) {
        List<String[]> data = CsvUtil.readAll(filePath);
        for (String[] row : data) {
            if (row.length > 0 && row[0].equals(id)) {
                Admin admin = fromRow(row);
                if (admin != null) return Optional.of(admin);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Admin> findAll() {
        List<String[]> data = CsvUtil.readAll(filePath);
        List<Admin> admins = new ArrayList<>();
        for (String[] row : data) {
            Admin admin = fromRow(row);
            if (admin != null) admins.add(admin);
        }
        return admins;
    }

    @Override
    public void updateAll(List<Admin> entities) {
        List<String[]> data = new ArrayList<>();
        for (Admin admin : entities) {
            data.add(toRow(admin));
        }
        CsvUtil.writeAll(filePath, HEADERS, data);
    }

    @Override
    public Optional<Admin> findByEmail(String email) {
        List<String[]> data = CsvUtil.readAll(filePath);
        for (String[] row : data) {
            if (row.length >= 3 && row[2].equalsIgnoreCase(email)) {
                Admin admin = fromRow(row);
                if (admin != null) return Optional.of(admin);
            }
        }
        return Optional.empty();
    }
}
