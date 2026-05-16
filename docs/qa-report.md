# QA Test Report

This document outlines the end-to-end testing coverage implemented in `src/com/admission/QATest.java`. The test suite operates on an isolated `qa_data/` directory to preserve production data.

## Test Summary
*   **Total Tests Run**: 14
*   **Tests Passed**: 14
*   **Tests Failed**: 0
*   **Status**: SUCCESS

## Test Phases & Validations

The `QATest` program executes a complete lifecycle flow, explicitly testing boundary conditions and exception handling across 8 distinct phases:

### Phase 1: System Setup
*   **Action**: Admin creates a College, Department, Program, 3 Subjects, and activates an Admission Cycle.
*   **Validation**: Confirms core entities are successfully written to CSV and associations are maintained.

### Phase 2: Student Registration
*   **Action**: Registers 3 students (Alice, Bob, Charlie).
*   **Validation**: Attempts to register a fourth student using an existing email address. Successfully verifies that the `AuthService` blocks duplicate emails.

### Phase 3: Application Flow
*   **Action**: Alice (90.0), Bob (80.0), and Charlie (70.0) apply to the active cycle.
*   **Validations**: 
    1. Prevents duplicate applications from the same student to the same cycle.
    2. Prevents new applications when the admin sets the cycle to inactive.

### Phase 4: Selection
*   **Action**: Admin triggers the `SelectionService` using `MeritBasedSelectionStrategy` for a cycle with a `seatCount` of 2.
*   **Validation**: Confirms Alice and Bob are marked as `SELECTED` (top 2 scores), while Charlie is marked as `REJECTED`.

### Phase 5: Enrollment
*   **Action**: Selected students attempt to enroll.
*   **Validations**:
    1. Alice and Bob successfully enroll.
    2. Charlie's enrollment is blocked due to `REJECTED` status.
    3. Alice's attempt to enroll a second time is blocked (prevents double enrollment).

### Phase 6: Subject Assignment
*   **Action**: Verifies automatic academic onboarding post-enrollment.
*   **Validation**: Checks the `StudentSubjectRepository` to ensure Alice was automatically assigned all 3 Semester-1 subjects associated with her program.

### Phase 7: Analytics
*   **Action**: Admin queries the `AnalyticsService`.
*   **Validation**: Accurately counts 3 total applications and 2 successful enrollments for the program.

### Phase 8: Error Handling Edge Cases
*   **Action**: Attempts various invalid operations.
*   **Validations**:
    1. Prevents login with invalid credentials.
    2. Blocks enrollment requests containing non-existent application IDs.
    3. Blocks department creation linked to a non-existent College ID (simulated foreign key constraint).
