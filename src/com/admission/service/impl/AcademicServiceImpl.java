package com.admission.service.impl;

import com.admission.model.Subject;
import com.admission.repository.StudentSubjectRepository;
import com.admission.repository.SubjectRepository;
import com.admission.service.AcademicService;

import java.util.List;

public class AcademicServiceImpl implements AcademicService {

    private final SubjectRepository subjectRepository;
    private final StudentSubjectRepository studentSubjectRepository;

    public AcademicServiceImpl(SubjectRepository subjectRepository, StudentSubjectRepository studentSubjectRepository) {
        this.subjectRepository = subjectRepository;
        this.studentSubjectRepository = studentSubjectRepository;
    }

    @Override
    public List<Subject> getSubjectsByStudent(String studentId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
