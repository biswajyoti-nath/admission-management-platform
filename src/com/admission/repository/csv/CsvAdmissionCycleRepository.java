package com.admission.repository.csv;

import com.admission.model.AdmissionCycle;
import com.admission.repository.AdmissionCycleRepository;
import com.admission.utils.CsvUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CsvAdmissionCycleRepository implements AdmissionCycleRepository {

    private final String filePath;
    private static final String[] HEADERS = {"id", "programId", "year", "seatCount", "active"};
    private static final String ID_PREFIX = "CYC";

    public CsvAdmissionCycleRepository(String filePath) {
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

    private String[] toRow(AdmissionCycle cycle) {
        return new String[]{
                cycle.getId(),
                cycle.getProgramId(),
                String.valueOf(cycle.getYear()),
                String.valueOf(cycle.getSeatCount()),
                String.valueOf(cycle.isActive())
        };
    }

    private AdmissionCycle fromRow(String[] row) {
        if (row.length < 5) return null;
        try {
            return new AdmissionCycle(row[0], row[1], Integer.parseInt(row[2]), Integer.parseInt(row[3]), Boolean.parseBoolean(row[4]));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public AdmissionCycle save(AdmissionCycle entity) {
        if (entity.getId() == null || entity.getId().isEmpty()) {
            entity.setId(generateId());
        }
        CsvUtil.append(filePath, toRow(entity));
        return entity;
    }

    @Override
    public Optional<AdmissionCycle> findById(String id) {
        List<String[]> data = CsvUtil.readAll(filePath);
        for (String[] row : data) {
            if (row.length > 0 && row[0].equals(id)) {
                AdmissionCycle cycle = fromRow(row);
                if (cycle != null) return Optional.of(cycle);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<AdmissionCycle> findAll() {
        List<String[]> data = CsvUtil.readAll(filePath);
        List<AdmissionCycle> cycles = new ArrayList<>();
        for (String[] row : data) {
            AdmissionCycle cycle = fromRow(row);
            if (cycle != null) cycles.add(cycle);
        }
        return cycles;
    }

    @Override
    public void updateAll(List<AdmissionCycle> entities) {
        List<String[]> data = new ArrayList<>();
        for (AdmissionCycle cycle : entities) {
            data.add(toRow(cycle));
        }
        CsvUtil.writeAll(filePath, HEADERS, data);
    }

    @Override
    public List<AdmissionCycle> findActiveByProgram(String programId) {
        List<String[]> data = CsvUtil.readAll(filePath);
        List<AdmissionCycle> cycles = new ArrayList<>();
        for (String[] row : data) {
            if (row.length >= 5 && row[1].equals(programId) && Boolean.parseBoolean(row[4])) {
                AdmissionCycle cycle = fromRow(row);
                if (cycle != null) cycles.add(cycle);
            }
        }
        return cycles;
    }
}
