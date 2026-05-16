package com.admission.repository.csv;

import com.admission.model.College;
import com.admission.repository.CollegeRepository;
import com.admission.utils.CsvUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CsvCollegeRepository implements CollegeRepository {

    private final String filePath;
    private static final String[] HEADERS = {"id", "name", "code", "address"};
    private static final String ID_PREFIX = "COL";

    public CsvCollegeRepository(String filePath) {
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

    private String[] toRow(College college) {
        return new String[]{
                college.getId(),
                college.getName(),
                college.getCode(),
                college.getAddress()
        };
    }

    private College fromRow(String[] row) {
        if (row.length < 4) return null;
        return new College(row[0], row[1], row[2], row[3]);
    }

    @Override
    public College save(College entity) {
        if (entity.getId() == null || entity.getId().isEmpty()) {
            entity.setId(generateId());
        }
        CsvUtil.append(filePath, toRow(entity));
        return entity;
    }

    @Override
    public Optional<College> findById(String id) {
        List<String[]> data = CsvUtil.readAll(filePath);
        for (String[] row : data) {
            if (row.length > 0 && row[0].equals(id)) {
                College college = fromRow(row);
                if (college != null) return Optional.of(college);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<College> findAll() {
        List<String[]> data = CsvUtil.readAll(filePath);
        List<College> colleges = new ArrayList<>();
        for (String[] row : data) {
            College college = fromRow(row);
            if (college != null) colleges.add(college);
        }
        return colleges;
    }

    @Override
    public void updateAll(List<College> entities) {
        List<String[]> data = new ArrayList<>();
        for (College college : entities) {
            data.add(toRow(college));
        }
        CsvUtil.writeAll(filePath, HEADERS, data);
    }
}
