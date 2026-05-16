# Data Model

This document outlines the core entities of the Admission Management Platform, mirroring the actual Java class implementations in `com.admission.model`.

## Entities & Fields

### Core User Entities
*   **Admin**: `id` (String), `name` (String), `email` (String), `password` (String)
*   **Student**: `id` (String), `name` (String), `email` (String), `password` (String), `phone` (String), `role` (Role enum: STUDENT)

### Academic Structure Entities
*   **College**: `id` (String), `name` (String), `code` (String), `address` (String)
*   **Department**: `id` (String), `collegeId` (String), `name` (String), `code` (String)
*   **Program**: `id` (String), `departmentId` (String), `name` (String), `durationYears` (int), `totalSeats` (int)
*   **Subject**: `id` (String), `programId` (String), `name` (String), `code` (String), `semester` (int), `credits` (int)

### Admission & Enrollment Entities
*   **AdmissionCycle**: `id` (String), `programId` (String), `year` (int), `semester` (int), `isActive` (boolean), `seatCount` (int)
*   **Application**: `id` (String), `studentId` (String), `admissionCycleId` (String), `score` (double), `status` (ApplicationStatus enum), `appliedDate` (String)
*   **Enrollment**: `id` (String), `studentId` (String), `applicationId` (String), `programId` (String), `enrolledDate` (String)
*   **StudentSubject**: `id` (String), `studentId` (String), `subjectId` (String), `semester` (int)

---

## Entity Relationships

The data model connects entities logically via string-based foreign keys (e.g., `collegeId`, `programId`).

*   **College (1) ↔ (N) Department**: A college holds multiple departments.
*   **Department (1) ↔ (N) Program**: A department offers multiple programs (e.g., BTech CSE).
*   **Program (1) ↔ (N) Subject**: A program consists of various subjects mapped to specific semesters.
*   **Program (1) ↔ (N) AdmissionCycle**: A program can run multiple admission cycles across different years/semesters.
*   **AdmissionCycle (1) ↔ (N) Application**: Many students apply to a single active admission cycle.
*   **Student (1) ↔ (N) Application**: A student can have multiple applications, but only one per specific `AdmissionCycle`.
*   **Application (1) ↔ (0..1) Enrollment**: An application transitioning to `SELECTED` can trigger a single `Enrollment` record.
*   **Enrollment (1) ↔ (N) StudentSubject**: Once enrolled, the system automatically assigns multiple `StudentSubject` records for the student's 1st semester.

---

## Status Definitions

### ApplicationStatus
Used inside the `Application` entity to track the admission process:
*   `APPLIED`: Initial state when a student applies.
*   `SELECTED`: Assigned when the Selection Strategy determines the student's score meets the quota.
*   `REJECTED`: Assigned when the student's score falls below the cutoff limit.

**Note:** Enrollment is NOT a status of the Application. It is an independent entity (`Enrollment`) created only for `SELECTED` applications.

---

## Normalization & Tradeoffs

*   **String IDs**: All Foreign Keys are `String` types containing formatted prefixes (e.g., `PRG-0001`). This provides easy manual debugging of the CSV files but lacks native referential integrity constraints typical of an SQL database.
*   **Duplication tradeoff**: Some referential integrity is manually enforced in the Service Layer instead of the Data Layer, such as validating `studentId` matches the associated `Application` during enrollment.
*   **Flattened Foreign Keys**: `StudentSubject` connects the `studentId` and `subjectId` but also explicitly stores the `semester`. This denormalization avoids deep joins when querying current subjects.
