package com.admission.repository.csv;

import com.admission.model.Enrollment;
import com.admission.repository.EnrollmentRepository;
import com.admission.utils.CsvUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CsvEnrollmentRepository implements EnrollmentRepository {

    private final String filePath;
    private static final String[] HEADERS = {"id", "studentId", "applicationId", "programId", "enrolledDate"};
    private static final String ID_PREFIX = "ENR";

    public CsvEnrollmentRepository(String filePath) {
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

    private String[] toRow(Enrollment enrollment) {
        return new String[]{
                enrollment.getId(),
                enrollment.getStudentId(),
                enrollment.getApplicationId(),
                enrollment.getProgramId(),
                enrollment.getEnrolledDate()
        };
    }

    private Enrollment fromRow(String[] row) {
        if (row.length < 5) return null;
        return new Enrollment(row[0], row[1], row[2], row[3], row[4]);
    }

    @Override
    public Enrollment save(Enrollment entity) {
        if (entity.getId() == null || entity.getId().isEmpty()) {
            entity.setId(generateId());
        }
        CsvUtil.append(filePath, toRow(entity));
        return entity;
    }

    @Override
    public Optional<Enrollment> findById(String id) {
        List<String[]> data = CsvUtil.readAll(filePath);
        for (String[] row : data) {
            if (row.length > 0 && row[0].equals(id)) {
                Enrollment enrollment = fromRow(row);
                if (enrollment != null) return Optional.of(enrollment);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Enrollment> findAll() {
        List<String[]> data = CsvUtil.readAll(filePath);
        List<Enrollment> enrollments = new ArrayList<>();
        for (String[] row : data) {
            Enrollment enrollment = fromRow(row);
            if (enrollment != null) enrollments.add(enrollment);
        }
        return enrollments;
    }

    @Override
    public void updateAll(List<Enrollment> entities) {
        List<String[]> data = new ArrayList<>();
        for (Enrollment enrollment : entities) {
            data.add(toRow(enrollment));
        }
        CsvUtil.writeAll(filePath, HEADERS, data);
    }

    @Override
    public Optional<Enrollment> findByStudentAndCycle(String studentId, String cycleId) {
        java.io.File file = new java.io.File(filePath);
        java.io.File parent = file.getParentFile();
        String appFilePath = (parent == null)
                ? "applications.csv"
                : new java.io.File(parent, "applications.csv").getPath();
        List<String[]> apps = CsvUtil.readAll(appFilePath);
        List<String> validAppIds = new ArrayList<>();
        for (String[] appRow : apps) {
            if (appRow.length >= 3 && appRow[2].equals(cycleId)) {
                validAppIds.add(appRow[0]);
            }
        }
        
        List<String[]> data = CsvUtil.readAll(filePath);
        for (String[] row : data) {
            if (row.length >= 3 && row[1].equals(studentId) && validAppIds.contains(row[2])) {
                Enrollment enrollment = fromRow(row);
                if (enrollment != null) return Optional.of(enrollment);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Enrollment> findByProgram(String programId) {
        List<String[]> data = CsvUtil.readAll(filePath);
        List<Enrollment> enrollments = new ArrayList<>();
        for (String[] row : data) {
            if (row.length >= 4 && row[3].equals(programId)) {
                Enrollment enrollment = fromRow(row);
                if (enrollment != null) enrollments.add(enrollment);
            }
        }
        return enrollments;
    }
}
