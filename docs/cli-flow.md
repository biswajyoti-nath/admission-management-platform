# CLI Flow — Menu Structure & State Transitions

> **Phase 1 Deliverable**

---

## Main Menu

```
========================================
  ADMISSION MANAGEMENT SYSTEM
========================================
1. Login as Student
2. Login as Admin
3. Register as Student
4. Exit
========================================
Enter choice:
```

## Student Menu (after login)

```
========================================
  STUDENT DASHBOARD — Welcome, <name>
========================================
1. Browse Colleges & Programs
2. View Active Admission Cycles
3. Apply to Admission Cycle
4. View My Applications
5. Accept Selection & Enroll
6. View My Enrolled Subjects
7. Logout
========================================
Enter choice:
```

## Admin Menu (after login)

```
========================================
  ADMIN DASHBOARD — Welcome, <name>
========================================
 --- College Configuration ---
1.  Add College
2.  Add Department
3.  Add Program
4.  Add Subjects to Program
 --- Admission Management ---
5.  Create Admission Cycle
6.  Open / Close Admission Cycle
7.  Run Selection for Cycle
8.  View Applications for Cycle
 --- Reports ---
9.  View Enrollments for Program
10. View All Colleges & Programs
 --- Account ---
11. Logout
========================================
Enter choice:
```

---

## Application State Transitions

```
                ┌──────────┐
                │  APPLIED │ ◄── Student submits application
                └────┬─────┘
                     │
            Admin runs selection
                     │
           ┌─────────┴──────────┐
           ▼                    ▼
    ┌───────────┐        ┌───────────┐
    │ SELECTED  │        │ REJECTED  │
    │ (terminal)│        │ (terminal)│
    └───────────┘        └───────────┘
          │
    Student accepts & enrolls
    → creates Enrollment record
    → assigns semester 1 subjects
```

> **Note:** `SELECTED` is a terminal **application** state. Enrollment is a
> separate entity — the application stays SELECTED; an `Enrollment` row is created.

### Transition Rules

| From     | To       | Triggered By | Validation                                      |
| -------- | -------- | ------------ | ----------------------------------------------- |
| APPLIED  | SELECTED | Admin        | Score meets cutoff; seats available              |
| APPLIED  | REJECTED | Admin        | Score below cutoff or no seats                   |

### On Enrollment (SELECTED → creates Enrollment record)

- Student must have a SELECTED application for the cycle
- Student must not already be enrolled in another program for the same cycle
- A new `Enrollment` record is created; application status remains SELECTED
- All semester-1 subjects of the program are assigned via `StudentSubject`

### Invalid Transitions (enforced by service layer)

- REJECTED → anything
- SELECTED → APPLIED (no rollback)

---

## Flow Sequences

### Student Registration & Application Flow

1. Student selects "Register" → enters name, email, password, phone
2. System creates Student record → returns to main menu
3. Student logs in → Dashboard appears
4. Student browses colleges/programs → selects a program
5. Student views active admission cycles for that program
6. Student applies → enters score → Application created (APPLIED)
7. After admin runs selection → student views application status
8. If SELECTED → student accepts → Enrollment + StudentSubject created

### Admin Configuration Flow

1. Admin logs in (pre-seeded or registered)
2. Admin adds College → Department → Program → Subjects
3. Admin creates AdmissionCycle for a program
4. Admin opens the cycle (isActive = true)
5. Students apply during active cycle
6. Admin runs selection → applications updated to SELECTED/REJECTED
7. Admin views enrollment reports

---

*Document version: 1.1 — Phase 1 (updated in Phase 2 to correct status model)*
