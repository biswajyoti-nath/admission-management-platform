# Data Model — Entity Definitions & Relationships

> **Phase 1 Deliverable**

---

## Entity Summary

| Entity          | Primary Key | Foreign Keys                                          |
| --------------- | ----------- | ----------------------------------------------------- |
| College         | id          | —                                                     |
| Department      | id          | collegeId → College                                   |
| Program         | id          | departmentId → Department                             |
| AdmissionCycle  | id          | programId → Program                                   |
| Student         | id          | —                                                     |
| Admin           | id          | —                                                     |
| Application     | id          | studentId → Student, admissionCycleId → AdmissionCycle|
| Enrollment      | id          | studentId, applicationId, programId                   |
| Subject         | id          | programId → Program                                   |
| StudentSubject  | id          | studentId → Student, subjectId → Subject              |

---

## Relationship Diagram

```
College ──1:N──► Department ──1:N──► Program ──1:N──► Subject
                                        │
                                        ├──1:N──► AdmissionCycle ──1:N──► Application
                                        │                                     │
                                        └──1:N──► Enrollment ◄────────────────┘
                                                      │
Student ──1:N──► Application                          │
    │                                                  │
    └──1:N──► StudentSubject ◄── (created upon enrollment)
                    │
                    └────► Subject
```

## Cardinality Rules

- College → Department: 1:N (department belongs to one college)
- Department → Program: 1:N (program belongs to one department)
- Program → AdmissionCycle: 1:N (one cycle per year per program)
- Program → Subject: 1:N (subject belongs to one program)
- Student → Application: 1:N (student may apply to multiple cycles)
- Student + AdmissionCycle → Application: 1:1 (unique pair)
- Application → Enrollment: 0..1 (only SELECTED can enroll)
- Enrollment → StudentSubject: 1:N (triggers subject assignment)

## Enums

### Role
`STUDENT`, `ADMIN`

### ApplicationStatus
`APPLIED` → `SELECTED` / `REJECTED` → `ENROLLED`

## ID Prefix Convention

| Entity         | Prefix | Example  |
| -------------- | ------ | -------- |
| College        | COL    | COL-0001 |
| Department     | DEP    | DEP-0001 |
| Program        | PRG    | PRG-0001 |
| AdmissionCycle | CYC    | CYC-0001 |
| Student        | STU    | STU-0001 |
| Admin          | ADM    | ADM-0001 |
| Application    | APP    | APP-0001 |
| Enrollment     | ENR    | ENR-0001 |
| Subject        | SUB    | SUB-0001 |
| StudentSubject | SSB    | SSB-0001 |

---

*Document version: 1.0 — Phase 1*
