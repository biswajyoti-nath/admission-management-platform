package com.admission.model;

public class AdmissionCycle {
    private String id;
    private String programId;
    private int year;
    private int seatCount;
    private boolean active;

    public AdmissionCycle() {
    }

    public AdmissionCycle(String id, String programId, int year, int seatCount, boolean active) {
        this.id = id;
        this.programId = programId;
        this.year = year;
        this.seatCount = seatCount;
        this.active = active;
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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(int seatCount) {
        this.seatCount = seatCount;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "AdmissionCycle{" +
                "id='" + id + '\'' +
                ", programId='" + programId + '\'' +
                ", year=" + year +
                ", seatCount=" + seatCount +
                ", active=" + active +
                '}';
    }
}
