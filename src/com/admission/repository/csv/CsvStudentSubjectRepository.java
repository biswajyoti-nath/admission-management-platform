package com.admission.repository.csv;

import com.admission.model.StudentSubject;
import com.admission.repository.StudentSubjectRepository;
import com.admission.utils.CsvUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CsvStudentSubjectRepository implements StudentSubjectRepository {

    private final String filePath;
    private static final String[] HEADERS = {"id", "studentId", "subjectId", "semester"};
    private static final String ID_PREFIX = "STS";

    public CsvStudentSubjectRepository(String filePath) {
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

    private String[] toRow(StudentSubject ss) {
        return new String[]{
                ss.getId(),
                ss.getStudentId(),
                ss.getSubjectId(),
                String.valueOf(ss.getSemester())
        };
    }

    private StudentSubject fromRow(String[] row) {
        if (row.length < 4) return null;
        try {
            return new StudentSubject(row[0], row[1], row[2], Integer.parseInt(row[3]));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public StudentSubject save(StudentSubject entity) {
        if (entity.getId() == null || entity.getId().isEmpty()) {
            entity.setId(generateId());
        }
        CsvUtil.append(filePath, toRow(entity));
        return entity;
    }

    @Override
    public Optional<StudentSubject> findById(String id) {
        List<String[]> data = CsvUtil.readAll(filePath);
        for (String[] row : data) {
            if (row.length > 0 && row[0].equals(id)) {
                StudentSubject ss = fromRow(row);
                if (ss != null) return Optional.of(ss);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<StudentSubject> findAll() {
        List<String[]> data = CsvUtil.readAll(filePath);
        List<StudentSubject> list = new ArrayList<>();
        for (String[] row : data) {
            StudentSubject ss = fromRow(row);
            if (ss != null) list.add(ss);
        }
        return list;
    }

    @Override
    public void updateAll(List<StudentSubject> entities) {
        List<String[]> data = new ArrayList<>();
        for (StudentSubject ss : entities) {
            data.add(toRow(ss));
        }
        CsvUtil.writeAll(filePath, HEADERS, data);
    }

    @Override
    public List<StudentSubject> findByStudent(String studentId) {
        List<String[]> data = CsvUtil.readAll(filePath);
        List<StudentSubject> list = new ArrayList<>();
        for (String[] row : data) {
            if (row.length >= 2 && row[1].equals(studentId)) {
                StudentSubject ss = fromRow(row);
                if (ss != null) list.add(ss);
            }
        }
        return list;
    }
}
