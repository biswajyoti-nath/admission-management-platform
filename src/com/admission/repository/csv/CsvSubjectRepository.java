package com.admission.repository.csv;

import com.admission.model.Subject;
import com.admission.repository.SubjectRepository;
import com.admission.utils.CsvUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CsvSubjectRepository implements SubjectRepository {

    private final String filePath;
    private static final String[] HEADERS = {"id", "programId", "name", "code", "semester", "credits"};
    private static final String ID_PREFIX = "SUB";

    public CsvSubjectRepository(String filePath) {
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

    private String[] toRow(Subject subject) {
        return new String[]{
                subject.getId(),
                subject.getProgramId(),
                subject.getName(),
                subject.getCode(),
                String.valueOf(subject.getSemester()),
                String.valueOf(subject.getCredits())
        };
    }

    private Subject fromRow(String[] row) {
        if (row.length < 6) return null;
        try {
            return new Subject(row[0], row[1], row[2], row[3], Integer.parseInt(row[4]), Integer.parseInt(row[5]));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public Subject save(Subject entity) {
        if (entity.getId() == null || entity.getId().isEmpty()) {
            entity.setId(generateId());
        }
        CsvUtil.append(filePath, toRow(entity));
        return entity;
    }

    @Override
    public Optional<Subject> findById(String id) {
        List<String[]> data = CsvUtil.readAll(filePath);
        for (String[] row : data) {
            if (row.length > 0 && row[0].equals(id)) {
                Subject subject = fromRow(row);
                if (subject != null) return Optional.of(subject);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Subject> findAll() {
        List<String[]> data = CsvUtil.readAll(filePath);
        List<Subject> subjects = new ArrayList<>();
        for (String[] row : data) {
            Subject subject = fromRow(row);
            if (subject != null) subjects.add(subject);
        }
        return subjects;
    }

    @Override
    public void updateAll(List<Subject> entities) {
        List<String[]> data = new ArrayList<>();
        for (Subject subject : entities) {
            data.add(toRow(subject));
        }
        CsvUtil.writeAll(filePath, HEADERS, data);
    }

    @Override
    public List<Subject> findByProgram(String programId) {
        List<String[]> data = CsvUtil.readAll(filePath);
        List<Subject> subjects = new ArrayList<>();
        for (String[] row : data) {
            if (row.length >= 2 && row[1].equals(programId)) {
                Subject subject = fromRow(row);
                if (subject != null) subjects.add(subject);
            }
        }
        return subjects;
    }

    @Override
    public List<Subject> findByProgramAndSemester(String programId, int semester) {
        List<String[]> data = CsvUtil.readAll(filePath);
        List<Subject> subjects = new ArrayList<>();
        for (String[] row : data) {
            if (row.length >= 5 && row[1].equals(programId) && row[4].equals(String.valueOf(semester))) {
                Subject subject = fromRow(row);
                if (subject != null) subjects.add(subject);
            }
        }
        return subjects;
    }
}
