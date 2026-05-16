package com.admission.repository.csv;

import com.admission.model.Student;
import com.admission.repository.StudentRepository;
import com.admission.utils.CsvUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CsvStudentRepository implements StudentRepository {

    private final String filePath;
    private static final String[] HEADERS = {"id", "name", "email", "password", "phone"};
    private static final String ID_PREFIX = "STU";

    public CsvStudentRepository(String filePath) {
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

    private String[] toRow(Student student) {
        return new String[]{
                student.getId(),
                student.getName(),
                student.getEmail(),
                student.getPassword(),
                student.getPhone()
        };
    }

    private Student fromRow(String[] row) {
        if (row.length < 5) return null;
        return new Student(row[0], row[1], row[2], row[3], row[4]);
    }

    @Override
    public Student save(Student entity) {
        if (entity.getId() == null || entity.getId().isEmpty()) {
            entity.setId(generateId());
        }
        CsvUtil.append(filePath, toRow(entity));
        return entity;
    }

    @Override
    public Optional<Student> findById(String id) {
        List<String[]> data = CsvUtil.readAll(filePath);
        for (String[] row : data) {
            if (row.length > 0 && row[0].equals(id)) {
                Student student = fromRow(row);
                if (student != null) return Optional.of(student);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Student> findAll() {
        List<String[]> data = CsvUtil.readAll(filePath);
        List<Student> students = new ArrayList<>();
        for (String[] row : data) {
            Student student = fromRow(row);
            if (student != null) students.add(student);
        }
        return students;
    }

    @Override
    public void updateAll(List<Student> entities) {
        List<String[]> data = new ArrayList<>();
        for (Student student : entities) {
            data.add(toRow(student));
        }
        CsvUtil.writeAll(filePath, HEADERS, data);
    }

    @Override
    public Optional<Student> findByEmail(String email) {
        List<String[]> data = CsvUtil.readAll(filePath);
        for (String[] row : data) {
            if (row.length >= 3 && row[2].equalsIgnoreCase(email)) {
                Student student = fromRow(row);
                if (student != null) return Optional.of(student);
            }
        }
        return Optional.empty();
    }
}
