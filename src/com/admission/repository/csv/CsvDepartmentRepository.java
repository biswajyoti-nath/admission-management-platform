package com.admission.repository.csv;

import com.admission.model.Department;
import com.admission.repository.DepartmentRepository;
import com.admission.utils.CsvUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CsvDepartmentRepository implements DepartmentRepository {

    private final String filePath;
    private static final String[] HEADERS = {"id", "collegeId", "name", "code"};
    private static final String ID_PREFIX = "DEP";

    public CsvDepartmentRepository(String filePath) {
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

    private String[] toRow(Department department) {
        return new String[]{
                department.getId(),
                department.getCollegeId(),
                department.getName(),
                department.getCode()
        };
    }

    private Department fromRow(String[] row) {
        if (row.length < 4) return null;
        return new Department(row[0], row[1], row[2], row[3]);
    }

    @Override
    public Department save(Department entity) {
        if (entity.getId() == null || entity.getId().isEmpty()) {
            entity.setId(generateId());
        }
        CsvUtil.append(filePath, toRow(entity));
        return entity;
    }

    @Override
    public Optional<Department> findById(String id) {
        List<String[]> data = CsvUtil.readAll(filePath);
        for (String[] row : data) {
            if (row.length > 0 && row[0].equals(id)) {
                Department department = fromRow(row);
                if (department != null) return Optional.of(department);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Department> findAll() {
        List<String[]> data = CsvUtil.readAll(filePath);
        List<Department> departments = new ArrayList<>();
        for (String[] row : data) {
            Department department = fromRow(row);
            if (department != null) departments.add(department);
        }
        return departments;
    }

    @Override
    public void updateAll(List<Department> entities) {
        List<String[]> data = new ArrayList<>();
        for (Department department : entities) {
            data.add(toRow(department));
        }
        CsvUtil.writeAll(filePath, HEADERS, data);
    }

    @Override
    public List<Department> findByCollege(String collegeId) {
        List<String[]> data = CsvUtil.readAll(filePath);
        List<Department> departments = new ArrayList<>();
        for (String[] row : data) {
            if (row.length >= 2 && row[1].equals(collegeId)) {
                Department department = fromRow(row);
                if (department != null) departments.add(department);
            }
        }
        return departments;
    }
}
