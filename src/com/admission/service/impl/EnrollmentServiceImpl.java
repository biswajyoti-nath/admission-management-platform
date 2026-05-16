package com.admission.service.impl;

import com.admission.model.Enrollment;
import com.admission.repository.ApplicationRepository;
import com.admission.repository.EnrollmentRepository;
import com.admission.repository.StudentSubjectRepository;
import com.admission.repository.SubjectRepository;
import com.admission.service.EnrollmentService;

public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final ApplicationRepository applicationRepository;
    private final SubjectRepository subjectRepository;
    private final StudentSubjectRepository studentSubjectRepository;

    public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository,
                                 ApplicationRepository applicationRepository,
                                 SubjectRepository subjectRepository,
                                 StudentSubjectRepository studentSubjectRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.applicationRepository = applicationRepository;
        this.subjectRepository = subjectRepository;
        this.studentSubjectRepository = studentSubjectRepository;
    }

    @Override
    public Enrollment enroll(String studentId, String applicationId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
