package com.admission.repository.csv;

import com.admission.model.Program;
import com.admission.repository.ProgramRepository;
import com.admission.utils.CsvUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CsvProgramRepository implements ProgramRepository {

    private final String filePath;
    private static final String[] HEADERS = {"id", "departmentId", "name", "durationYears", "totalSeats"};
    private static final String ID_PREFIX = "PRG";

    public CsvProgramRepository(String filePath) {
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

    private String[] toRow(Program program) {
        return new String[]{
                program.getId(),
                program.getDepartmentId(),
                program.getName(),
                String.valueOf(program.getDurationYears()),
                String.valueOf(program.getTotalSeats())
        };
    }

    private Program fromRow(String[] row) {
        if (row.length < 5) return null;
        try {
            return new Program(row[0], row[1], row[2], Integer.parseInt(row[3]), Integer.parseInt(row[4]));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public Program save(Program entity) {
        if (entity.getId() == null || entity.getId().isEmpty()) {
            entity.setId(generateId());
        }
        CsvUtil.append(filePath, toRow(entity));
        return entity;
    }

    @Override
    public Optional<Program> findById(String id) {
        List<String[]> data = CsvUtil.readAll(filePath);
        for (String[] row : data) {
            if (row.length > 0 && row[0].equals(id)) {
                Program program = fromRow(row);
                if (program != null) return Optional.of(program);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Program> findAll() {
        List<String[]> data = CsvUtil.readAll(filePath);
        List<Program> programs = new ArrayList<>();
        for (String[] row : data) {
            Program program = fromRow(row);
            if (program != null) programs.add(program);
        }
        return programs;
    }

    @Override
    public void updateAll(List<Program> entities) {
        List<String[]> data = new ArrayList<>();
        for (Program program : entities) {
            data.add(toRow(program));
        }
        CsvUtil.writeAll(filePath, HEADERS, data);
    }

    @Override
    public List<Program> findByDepartment(String departmentId) {
        List<String[]> data = CsvUtil.readAll(filePath);
        List<Program> programs = new ArrayList<>();
        for (String[] row : data) {
            if (row.length >= 2 && row[1].equals(departmentId)) {
                Program program = fromRow(row);
                if (program != null) programs.add(program);
            }
        }
        return programs;
    }
}
