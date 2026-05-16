package com.admission.model;

public class StudentSubject {
    private String id;
    private String studentId;
    private String subjectId;
    private int semester;

    public StudentSubject() {
    }

    public StudentSubject(String id, String studentId, String subjectId, int semester) {
        this.id = id;
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.semester = semester;
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

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    @Override
    public String toString() {
        return "StudentSubject{" +
                "id='" + id + '\'' +
                ", studentId='" + studentId + '\'' +
                ", subjectId='" + subjectId + '\'' +
                ", semester=" + semester +
                '}';
    }
}
