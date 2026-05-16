package com.admission.model;

public class Application {
    private String id;
    private String studentId;
    private String admissionCycleId;
    private double score;
    private ApplicationStatus status;
    private String appliedDate;

    public Application() {
    }

    public Application(String id, String studentId, String admissionCycleId, double score, ApplicationStatus status, String appliedDate) {
        this.id = id;
        this.studentId = studentId;
        this.admissionCycleId = admissionCycleId;
        this.score = score;
        this.status = status;
        this.appliedDate = appliedDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getAdmissionCycleId() {
        return admissionCycleId;
    }

    public void setAdmissionCycleId(String admissionCycleId) {
        this.admissionCycleId = admissionCycleId;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public String getAppliedDate() {
        return appliedDate;
    }

    public void setAppliedDate(String appliedDate) {
        this.appliedDate = appliedDate;
    }

    @Override
    public String toString() {
        return "Application{" +
                "id='" + id + '\'' +
                ", studentId='" + studentId + '\'' +
                ", admissionCycleId='" + admissionCycleId + '\'' +
                ", score=" + score +
                ", status=" + status +
                ", appliedDate='" + appliedDate + '\'' +
                '}';
    }
}
