package com.admission.service.impl;

import com.admission.model.Admin;
import com.admission.model.Student;
import com.admission.repository.AdminRepository;
import com.admission.repository.StudentRepository;
import com.admission.service.AuthService;

import java.util.Optional;

public class AuthServiceImpl implements AuthService {

    private final StudentRepository studentRepository;
    private final AdminRepository adminRepository;

    public AuthServiceImpl(StudentRepository studentRepository, AdminRepository adminRepository) {
        this.studentRepository = studentRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public Student registerStudent(String name, String email, String password, String phone) {
        Optional<Student> existing = studentRepository.findByEmail(email);
        if (existing.isPresent()) {
            throw new RuntimeException("Email already registered");
        }
        Student student = new Student(null, name, email, password, phone);
        return studentRepository.save(student);
    }

    @Override
    public Optional<Student> loginStudent(String email, String password) {
        Optional<Student> studentOpt = studentRepository.findByEmail(email);
        if (studentOpt.isEmpty()) {
            throw new RuntimeException("Student not found");
        }
        Student student = studentOpt.get();
        if (!student.getPassword().equals(password)) {
            throw new RuntimeException("Invalid credentials");
        }
        return Optional.of(student);
    }

    @Override
    public Optional<Admin> loginAdmin(String email, String password) {
        Optional<Admin> adminOpt = adminRepository.findByEmail(email);
        if (adminOpt.isEmpty()) {
            throw new RuntimeException("Admin not found");
        }
        Admin admin = adminOpt.get();
        if (!admin.getPassword().equals(password)) {
            throw new RuntimeException("Invalid credentials");
        }
        return Optional.of(admin);
    }
}
