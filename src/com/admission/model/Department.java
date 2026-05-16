package com.admission.model;

public class Department {
    private String id;
    private String collegeId;
    private String name;
    private String code;

    public Department() {
    }

    public Department(String id, String collegeId, String name, String code) {
        this.id = id;
        this.collegeId = collegeId;
        this.name = name;
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(String collegeId) {
        this.collegeId = collegeId;
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

    @Override
    public String toString() {
        return "Department{" +
                "id='" + id + '\'' +
                ", collegeId='" + collegeId + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
