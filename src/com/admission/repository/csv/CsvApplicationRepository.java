package com.admission.repository.csv;

import com.admission.model.Application;
import com.admission.model.ApplicationStatus;
import com.admission.repository.ApplicationRepository;
import com.admission.utils.CsvUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CsvApplicationRepository implements ApplicationRepository {

    private final String filePath;
    private static final String[] HEADERS = {"id", "studentId", "admissionCycleId", "score", "status", "appliedDate"};
    private static final String ID_PREFIX = "APP";

    public CsvApplicationRepository(String filePath) {
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

    private String[] toRow(Application application) {
        return new String[]{
                application.getId(),
                application.getStudentId(),
                application.getAdmissionCycleId(),
                String.valueOf(application.getScore()),
                application.getStatus().name(),
                application.getAppliedDate()
        };
    }

    private Application fromRow(String[] row) {
        if (row.length < 6) return null;
        try {
            return new Application(row[0], row[1], row[2], Double.parseDouble(row[3]), ApplicationStatus.valueOf(row[4]), row[5]);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public Application save(Application entity) {
        if (entity.getId() == null || entity.getId().isEmpty()) {
            entity.setId(generateId());
        }
        CsvUtil.append(filePath, toRow(entity));
        return entity;
    }

    @Override
    public Optional<Application> findById(String id) {
        List<String[]> data = CsvUtil.readAll(filePath);
        for (String[] row : data) {
            if (row.length > 0 && row[0].equals(id)) {
                Application application = fromRow(row);
                if (application != null) return Optional.of(application);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Application> findAll() {
        List<String[]> data = CsvUtil.readAll(filePath);
        List<Application> applications = new ArrayList<>();
        for (String[] row : data) {
            Application application = fromRow(row);
            if (application != null) applications.add(application);
        }
        return applications;
    }

    @Override
    public void updateAll(List<Application> entities) {
        List<String[]> data = new ArrayList<>();
        for (Application application : entities) {
            data.add(toRow(application));
        }
        CsvUtil.writeAll(filePath, HEADERS, data);
    }

    @Override
    public Optional<Application> findByStudentAndCycle(String studentId, String cycleId) {
        List<String[]> data = CsvUtil.readAll(filePath);
        for (String[] row : data) {
            if (row.length >= 3 && row[1].equals(studentId) && row[2].equals(cycleId)) {
                Application application = fromRow(row);
                if (application != null) return Optional.of(application);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Application> findByCycle(String cycleId) {
        List<String[]> data = CsvUtil.readAll(filePath);
        List<Application> applications = new ArrayList<>();
        for (String[] row : data) {
            if (row.length >= 3 && row[2].equals(cycleId)) {
                Application application = fromRow(row);
                if (application != null) applications.add(application);
            }
        }
        return applications;
    }

    @Override
    public List<Application> findByStatus(String cycleId, ApplicationStatus status) {
        List<String[]> data = CsvUtil.readAll(filePath);
        List<Application> applications = new ArrayList<>();
        for (String[] row : data) {
            if (row.length >= 5 && row[2].equals(cycleId) && row[4].equals(status.name())) {
                Application application = fromRow(row);
                if (application != null) applications.add(application);
            }
        }
        return applications;
    }
}
