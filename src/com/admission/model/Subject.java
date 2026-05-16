package com.admission.model;

public class Subject {
    private String id;
    private String programId;
    private String name;
    private String code;
    private int semester;
    private int credits;

    public Subject() {
    }

    public Subject(String id, String programId, String name, String code, int semester, int credits) {
        this.id = id;
        this.programId = programId;
        this.name = name;
        this.code = code;
        this.semester = semester;
        this.credits = credits;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProgramId() {
        return programId;
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "id='" + id + '\'' +
                ", programId='" + programId + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", semester=" + semester +
                ", credits=" + credits +
                '}';
    }
}
