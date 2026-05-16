package com.admission.model;

public class Enrollment {
    private String id;
    private String studentId;
    private String applicationId;
    private String programId;
    private String enrolledDate;

    public Enrollment() {
    }

    public Enrollment(String id, String studentId, String applicationId, String programId, String enrolledDate) {
        this.id = id;
        this.studentId = studentId;
        this.applicationId = applicationId;
        this.programId = programId;
        this.enrolledDate = enrolledDate;
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

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getProgramId() {
        return programId;
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }

    public String getEnrolledDate() {
        return enrolledDate;
    }

    public void setEnrolledDate(String enrolledDate) {
        this.enrolledDate = enrolledDate;
    }

    @Override
    public String toString() {
        return "Enrollment{" +
                "id='" + id + '\'' +
                ", studentId='" + studentId + '\'' +
                ", applicationId='" + applicationId + '\'' +
                ", programId='" + programId + '\'' +
                ", enrolledDate='" + enrolledDate + '\'' +
                '}';
    }
}
