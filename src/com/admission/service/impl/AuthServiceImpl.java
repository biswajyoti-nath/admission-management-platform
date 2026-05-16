package com.admission.service.impl;

import com.admission.model.Admin;
import com.admission.model.Student;
import com.admission.repository.AdminRepository;
import com.admission.repository.StudentRepository;
import com.admission.service.AuthService;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
        Student student = new Student(null, name, email, hashPassword(password), phone);
        return studentRepository.save(student);
    }

    @Override
    public Optional<Student> loginStudent(String email, String password) {
        Optional<Student> studentOpt = studentRepository.findByEmail(email);
        if (studentOpt.isEmpty()) {
            throw new RuntimeException("Student not found");
        }
        Student student = studentOpt.get();
        if (!verifyPassword(student.getPassword(), password)) {
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
        if (!verifyPassword(admin.getPassword(), password)) {
            throw new RuntimeException("Invalid credentials");
        }
        return Optional.of(admin);
    }

    private static boolean verifyPassword(String storedPassword, String rawPassword) {
        return storedPassword.equals(rawPassword) || storedPassword.equals(hashPassword(rawPassword));
    }

    private static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Password hashing algorithm unavailable", e);
        }
    }
}
