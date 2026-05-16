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
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Optional<Student> loginStudent(String email, String password) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Optional<Admin> loginAdmin(String email, String password) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
