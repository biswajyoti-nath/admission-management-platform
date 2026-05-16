package com.admission.model;

public class Program {
    private String id;
    private String departmentId;
    private String name;
    private int durationYears;
    private int totalSeats;

    public Program() {
    }

    public Program(String id, String departmentId, String name, int durationYears, int totalSeats) {
        this.id = id;
        this.departmentId = departmentId;
        this.name = name;
        this.durationYears = durationYears;
        this.totalSeats = totalSeats;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDurationYears() {
        return durationYears;
    }

    public void setDurationYears(int durationYears) {
        this.durationYears = durationYears;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    @Override
    public String toString() {
        return "Program{" +
                "id='" + id + '\'' +
                ", departmentId='" + departmentId + '\'' +
                ", name='" + name + '\'' +
                ", durationYears=" + durationYears +
                ", totalSeats=" + totalSeats +
                '}';
    }
}
