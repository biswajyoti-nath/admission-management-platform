# Service & Business Flow

This document details the critical business processes orchestrated by the Service Layer, specifically focusing on the lifecycle of a student's admission.

## 1. Application Flow
The application process relies on `ApplicationServiceImpl` to ensure business rules are met before a student can apply.

*   **Validation Check**: The service queries the `AdmissionCycleRepository` to verify the targeted `cycleId` exists and that `isActive()` is `true`. Applications to inactive cycles are strictly rejected.
*   **Duplicate Check**: It checks the `ApplicationRepository` to ensure the student has not already applied to the same cycle. A student can apply to different cycles, but only once per cycle.
*   **Persistence**: A new `Application` entity is instantiated with the `APPLIED` status and the current date, then passed to the repository's `save()` method.

## 2. Selection Logic
The selection process is executed by an admin via the `SelectionServiceImpl`.

*   **State Verification**: The service retrieves all applications mapped to the active cycle with an `APPLIED` status.
*   **Strategy Execution**: It delegates the actual sorting and selection to a `SelectionStrategy`. Currently, the `MeritBasedSelectionStrategy` is used:
    1.  Sorts applications in descending order by `score`.
    2.  Iterates through the sorted list up to the cycle's `seatCount`.
    3.  Marks the top applications as `SELECTED` and the remainder as `REJECTED`.
*   **Batch Update**: The modified applications are merged back into the full list of applications and saved via a full-file rewrite (`updateAll`).

## 3. Enrollment and Subject Assignment
Enrollment finalizes a student's admission and triggers academic onboarding via `EnrollmentServiceImpl`.

*   **Authorization Check**: The service verifies the `applicationId` exists, corresponds to the correct `studentId`, and holds a `SELECTED` status.
*   **Enrollment Creation**: Generates an `Enrollment` record linking the student to the program.
*   **Automated Subject Assignment**: 
    1.  The service queries the `SubjectRepository` for all subjects associated with the program's Semester 1.
    2.  For each subject retrieved, a `StudentSubject` entity is instantiated and saved, effectively enrolling the student in their first-semester classes automatically.
*   **Error Handling (Rollback Simulation)**: If the subject assignment fails midway, a basic catch block attempts to remove the newly created `Enrollment` record using `updateAll` to prevent an orphaned enrollment state.

## 4. General Error Handling Strategy
Throughout the service layer, business rule violations (e.g., duplicate applications, invalid IDs, inactive cycles) are handled by throwing `RuntimeException` with descriptive messages. The Presentation Layer (Controllers) intercepts these exceptions and displays friendly error messages to the CLI user without crashing the application.
