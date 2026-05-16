package com.admission.service.impl;

import com.admission.model.StudentSubject;
import com.admission.model.Subject;
import com.admission.repository.StudentSubjectRepository;
import com.admission.repository.SubjectRepository;
import com.admission.service.AcademicService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AcademicServiceImpl implements AcademicService {

    private final SubjectRepository subjectRepository;
    private final StudentSubjectRepository studentSubjectRepository;

    public AcademicServiceImpl(SubjectRepository subjectRepository, StudentSubjectRepository studentSubjectRepository) {
        this.subjectRepository = subjectRepository;
        this.studentSubjectRepository = studentSubjectRepository;
    }

    @Override
    public List<Subject> getSubjectsByStudent(String studentId) {
        List<StudentSubject> studentSubjects = studentSubjectRepository.findByStudent(studentId);
        List<Subject> subjects = new ArrayList<>();
        for (StudentSubject ss : studentSubjects) {
            Optional<Subject> subjectOpt = subjectRepository.findById(ss.getSubjectId());
            subjectOpt.ifPresent(subjects::add);
        }
        return subjects;
    }
}
