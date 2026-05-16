package com.admission.service.impl;

import com.admission.model.AdmissionCycle;
import com.admission.model.Application;
import com.admission.model.ApplicationStatus;
import com.admission.model.Enrollment;
import com.admission.model.StudentSubject;
import com.admission.model.Subject;
import com.admission.repository.AdmissionCycleRepository;
import com.admission.repository.ApplicationRepository;
import com.admission.repository.EnrollmentRepository;
import com.admission.repository.StudentSubjectRepository;
import com.admission.repository.SubjectRepository;
import com.admission.service.EnrollmentService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final ApplicationRepository applicationRepository;
    private final SubjectRepository subjectRepository;
    private final StudentSubjectRepository studentSubjectRepository;
    private final AdmissionCycleRepository admissionCycleRepository;

    public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository,
                                 ApplicationRepository applicationRepository,
                                 SubjectRepository subjectRepository,
                                 StudentSubjectRepository studentSubjectRepository,
                                 AdmissionCycleRepository admissionCycleRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.applicationRepository = applicationRepository;
        this.subjectRepository = subjectRepository;
        this.studentSubjectRepository = studentSubjectRepository;
        this.admissionCycleRepository = admissionCycleRepository;
    }

    @Override
    public Enrollment enroll(String studentId, String applicationId) {
        Optional<Application> appOpt = applicationRepository.findById(applicationId);
        if (appOpt.isEmpty()) {
            throw new RuntimeException("Application not found");
        }
        Application application = appOpt.get();

        if (!application.getStudentId().equals(studentId)) {
            throw new RuntimeException("Student not authorized for this application");
        }

        if (application.getStatus() != ApplicationStatus.SELECTED) {
            throw new RuntimeException("Application is not selected");
        }

        Optional<AdmissionCycle> cycleOpt = admissionCycleRepository.findById(application.getAdmissionCycleId());
        if (cycleOpt.isEmpty()) {
            throw new RuntimeException("Admission cycle not found");
        }
        AdmissionCycle cycle = cycleOpt.get();

        Optional<Enrollment> existingOpt = enrollmentRepository.findByStudentAndCycle(studentId, cycle.getId());
        if (existingOpt.isPresent()) {
            throw new RuntimeException("Student already enrolled for this cycle");
        }

        Enrollment enrollment = new Enrollment(
                null,
                studentId,
                applicationId,
                cycle.getProgramId(),
                LocalDate.now().toString()
        );
        enrollment = enrollmentRepository.save(enrollment);

        List<Subject> subjects = subjectRepository.findByProgramAndSemester(cycle.getProgramId(), 1);
        for (Subject subject : subjects) {
            StudentSubject ss = new StudentSubject(
                    null,
                    studentId,
                    subject.getId(),
                    1
            );
            studentSubjectRepository.save(ss);
        }

        return enrollment;
    }
}
