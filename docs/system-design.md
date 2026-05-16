# System Design — Configurable Multi-College Admission & Academic Management System

> **Phase 1 Deliverable — Architecture, SOLID Mapping, and Design Rationale**

---

## 1. System Overview

### 1.1 Problem Definition

Educational institutions need a unified platform to manage the entire student lifecycle:
admission configuration, application intake, merit-based selection, enrollment, and
academic subject assignment — across **multiple colleges**, each with its own departments,
programs, and admission cycles.

Most existing solutions are either monolithic database-driven web applications or
institution-specific scripts. This project delivers a **lightweight, file-based,
CLI-driven system** that any institution can adopt without external dependencies.

### 1.2 Scope Boundaries

**In Scope:**

| Capability                         | Description                                                         |
| ---------------------------------- | ------------------------------------------------------------------- |
| Multi-college configuration        | Admin creates colleges, departments, programs, and admission cycles |
| Student self-service               | Register, browse programs, apply, view status, enroll               |
| Merit-based selection              | Configurable selection strategies (merit cutoff, rank-based)        |
| Subject management                 | Programs define subjects; enrolled students view their subjects     |
| CSV persistence                    | All data stored in flat CSV files under `/data`                     |
| Role-based CLI menus               | Separate flows for Admin and Student                                |

**Out of Scope:**

- GUI / Web interface
- Payment or fee management
- Real-time notifications
- Multi-user concurrency (single-user CLI assumed)
- External database or ORM
- Third-party libraries

### 1.3 Key Assumptions

| #  | Assumption                                                                                          |
| -- | --------------------------------------------------------------------------------------------------- |
| A1 | The system runs as a single-user CLI process. No concurrent access to CSV files.                    |
| A2 | Colleges, departments, and programs are configured by an Admin before students interact.            |
| A3 | Each admission cycle belongs to exactly one program and has a defined seat capacity.                 |
| A4 | A student can apply to multiple programs but can enroll in at most one per admission cycle.          |
| A5 | Selection is performed per admission cycle; the strategy is pluggable (default: merit-based cutoff). |
| A6 | Subject assignment is automatic upon enrollment — all subjects of the program are assigned.          |
| A7 | IDs are auto-generated using a monotonically increasing counter per entity, persisted in CSV.        |
| A8 | Data integrity is enforced at the service layer (no database-level constraints).                     |

---

## 2. Architecture

### 2.1 Layered Architecture

The system follows a strict **4-layer architecture** with unidirectional dependency flow:

```
┌─────────────────────────────────────────────────┐
│                   CLI Layer                      │
│         (Controller / Menu classes)              │
│   Reads user input, delegates to services,       │
│   formats and displays output                    │
├─────────────────────────────────────────────────┤
│                 Service Layer                    │
│       (Business logic & orchestration)           │
│   Validates rules, coordinates repositories,     │
│   implements workflows (apply, select, enroll)   │
├─────────────────────────────────────────────────┤
│               Repository Layer                   │
│         (Data access abstraction)                │
│   CRUD operations on CSV files via interfaces,   │
│   encapsulates file I/O details                  │
├─────────────────────────────────────────────────┤
│                 Model Layer                      │
│           (Domain entities / POJOs)              │
│   Pure data classes with no business logic,      │
│   represent the problem domain                   │
└─────────────────────────────────────────────────┘
         │
         ▼
    ┌──────────┐
    │ /data    │  CSV flat files (persistence)
    └──────────┘
```

### 2.2 Layer Responsibilities

#### Model Layer (`src/com/admission/model/`)

- Pure Java POJOs representing domain entities.
- Each model has fields, a constructor, getters/setters, and a `toString()`.
- **No business logic, no I/O, no dependencies on other layers.**
- Enums for fixed-value domains: `Role` (STUDENT, ADMIN), `ApplicationStatus` (APPLIED, SELECTED, REJECTED, ENROLLED).

#### Repository Layer (`src/com/admission/repository/`)

- Defines **interfaces** for each entity's CRUD operations.
- Concrete implementations read/write CSV files using a shared `CsvUtil`.
- Each repository is responsible for exactly **one entity type** and **one CSV file**.
- Handles serialization (object → CSV row) and deserialization (CSV row → object).

#### Service Layer (`src/com/admission/service/`)

- Contains all **business rules and validation**.
- Orchestrates multi-repository operations (e.g., enrollment requires checking application status, seat availability, and creating enrollment + subject records).
- **Never touches CSV files directly** — always goes through repository interfaces.
- Uses **Dependency Injection (constructor injection)** to receive repository instances.

#### Controller / CLI Layer (`src/com/admission/controller/`)

- Entry point for user interaction.
- Presents menus, reads `Scanner` input, calls service methods, prints results.
- **No business logic** — purely a translation layer between human input and service calls.
- Separate controllers for Admin and Student flows.

### 2.3 Data Flow (Textual)

**Example: Student submits an application**

```
1. StudentController displays "Apply to Program" menu
2. User enters program ID and their percentage/score
3. StudentController calls ApplicationService.apply(studentId, programId, score)
4. ApplicationService:
   a. Calls AdmissionCycleRepository.findActiveByProgram(programId)
      → Validates an active cycle exists
   b. Calls ApplicationRepository.findByStudentAndCycle(studentId, cycleId)
      → Validates no duplicate application
   c. Creates Application object (status = APPLIED)
   d. Calls ApplicationRepository.save(application)
5. ApplicationRepository serializes Application to CSV row, appends to applications.csv
6. Control returns up the stack; StudentController prints confirmation
```

**Example: Admin runs selection for an admission cycle**

```
1. AdminController displays "Run Selection" menu
2. Admin enters admission cycle ID and selects a strategy
3. AdminController calls SelectionService.runSelection(cycleId, strategy)
4. SelectionService:
   a. Loads AdmissionCycle → gets seat capacity
   b. Loads all Applications with status APPLIED for this cycle
   c. Delegates to SelectionStrategy.select(applications, seats)
      → Strategy sorts by merit, returns top-N as SELECTED, rest as REJECTED
   d. Updates each Application's status via ApplicationRepository.update()
5. AdminController prints selection summary
```

---

## 3. SOLID Mapping

### 3.1 Single Responsibility Principle (SRP)

> *Each class has one, and only one, reason to change.*

| Class / Interface             | Single Responsibility                                      |
| ----------------------------- | ---------------------------------------------------------- |
| `Student` (model)             | Holds student data — changes only if student schema changes |
| `CsvStudentRepository`        | Reads/writes `students.csv` — changes only if CSV format changes |
| `ApplicationService`          | Application workflow rules — changes only if business rules change |
| `StudentController`           | Student CLI interaction — changes only if menu UX changes   |
| `CsvUtil`                     | Generic CSV read/write — changes only if file I/O strategy changes |
| `MeritBasedSelectionStrategy` | Merit selection algorithm — changes only if ranking logic changes |

### 3.2 Open/Closed Principle (OCP)

> *Open for extension, closed for modification.*

- **`SelectionStrategy` interface**: New selection algorithms (e.g., lottery-based, weighted-criteria) can be added by implementing the interface — no changes to `SelectionService`.
- **`Repository<T>` generic interface**: New entity repositories implement the same contract without modifying existing repository code.
- **Enum-based status transitions**: Adding a new status (e.g., `WAITLISTED`) requires adding an enum constant, not modifying conditional logic scattered across the codebase.

### 3.3 Liskov Substitution Principle (LSP)

> *Subtypes must be substitutable for their base types.*

- `CsvStudentRepository` implements `StudentRepository` (which extends `Repository<Student>`). Any code using `StudentRepository` works identically regardless of whether the underlying implementation uses CSV, JSON, or an in-memory map.
- `MeritBasedSelectionStrategy` and any future `LotterySelectionStrategy` both implement `SelectionStrategy` — the `SelectionService` treats them identically.

### 3.4 Interface Segregation Principle (ISP)

> *No client should be forced to depend on methods it does not use.*

- Repository interfaces are entity-specific (`StudentRepository`, `ApplicationRepository`) rather than one monolithic `DatabaseRepository` with methods for all entities.
- `SelectionStrategy` has a single method `select(List<Application>, int seats)` — it does not bundle unrelated operations like "notify students" or "generate reports."
- Service interfaces are split by domain concern: `ApplicationService`, `EnrollmentService`, `CollegeConfigService` — controllers depend only on the services they need.

### 3.5 Dependency Inversion Principle (DIP)

> *High-level modules should not depend on low-level modules. Both should depend on abstractions.*

- **Services depend on repository interfaces, not CSV implementations:**
  ```
  ApplicationService → ApplicationRepository (interface)
                              ↑
                     CsvApplicationRepository (implementation)
  ```
- **Controllers depend on service interfaces, not concrete services.**
- **Wiring happens in `Main.java`**: The application entry point creates concrete implementations and injects them into services and controllers (poor-man's DI).

---

## 4. Domain Model

### 4.1 Entity Definitions

#### College

| Attribute   | Type   | Description                        |
| ----------- | ------ | ---------------------------------- |
| `id`        | String | Unique identifier (auto-generated) |
| `name`      | String | College name                       |
| `code`      | String | Short code (e.g., "NIT-S")         |
| `address`   | String | Physical address                   |

**Relationships:** A College has many Departments.

---

#### Department

| Attribute   | Type   | Description                        |
| ----------- | ------ | ---------------------------------- |
| `id`        | String | Unique identifier                  |
| `collegeId` | String | FK → College                       |
| `name`      | String | Department name (e.g., "Computer Science") |
| `code`      | String | Short code (e.g., "CSE")           |

**Relationships:** Belongs to one College. Has many Programs.

---

#### Program

| Attribute      | Type   | Description                              |
| -------------- | ------ | ---------------------------------------- |
| `id`           | String | Unique identifier                        |
| `departmentId` | String | FK → Department                          |
| `name`         | String | Program name (e.g., "B.Tech CSE")        |
| `durationYears`| int    | Duration in years                        |
| `totalSeats`   | int    | Maximum seats available                  |

**Relationships:** Belongs to one Department. Has many Subjects. Has many AdmissionCycles.

---

#### AdmissionCycle

| Attribute    | Type    | Description                                        |
| ------------ | ------- | -------------------------------------------------- |
| `id`         | String  | Unique identifier                                  |
| `programId`  | String  | FK → Program                                       |
| `year`       | int     | Academic year (e.g., 2026)                          |
| `seatCount`  | int     | Seats available for this cycle (≤ Program.totalSeats) |
| `isActive`   | boolean | Whether this cycle is currently accepting applications |

**Relationships:** Belongs to one Program. Has many Applications.

---

#### Student (extends/implements User concept)

| Attribute    | Type   | Description                              |
| ------------ | ------ | ---------------------------------------- |
| `id`         | String | Unique identifier                        |
| `name`       | String | Full name                                |
| `email`      | String | Email address (used for login)           |
| `password`   | String | Hashed/plain password (plain for simplicity) |
| `phone`      | String | Contact number                           |
| `role`       | Role   | Always `STUDENT`                         |

**Relationships:** Has many Applications. Has many StudentSubjects (after enrollment).

---

#### Admin (extends/implements User concept)

| Attribute  | Type   | Description            |
| ---------- | ------ | ---------------------- |
| `id`       | String | Unique identifier      |
| `name`     | String | Full name              |
| `email`    | String | Email (used for login) |
| `password` | String | Password               |
| `role`     | Role   | Always `ADMIN`         |

**Relationships:** Manages Colleges, Departments, Programs, AdmissionCycles, and runs selections.

---

#### Application

| Attribute           | Type              | Description                          |
| ------------------- | ----------------- | ------------------------------------ |
| `id`                | String            | Unique identifier                    |
| `studentId`         | String            | FK → Student                         |
| `admissionCycleId`  | String            | FK → AdmissionCycle                  |
| `score`             | double            | Student's merit score / percentage   |
| `status`            | ApplicationStatus | APPLIED → SELECTED / REJECTED             |
| `appliedDate`       | String            | ISO date of application              |

**Relationships:** Belongs to one Student and one AdmissionCycle.

---

#### Enrollment

| Attribute           | Type   | Description                           |
| ------------------- | ------ | ------------------------------------- |
| `id`                | String | Unique identifier                     |
| `studentId`         | String | FK → Student                          |
| `applicationId`     | String | FK → Application (the selected one)   |
| `programId`         | String | FK → Program                          |
| `enrolledDate`      | String | ISO date of enrollment                |

**Relationships:** Belongs to one Student, one Application, one Program. Triggers StudentSubject creation.

---

#### Subject

| Attribute   | Type   | Description                             |
| ----------- | ------ | --------------------------------------- |
| `id`        | String | Unique identifier                       |
| `programId` | String | FK → Program                            |
| `name`      | String | Subject name (e.g., "Data Structures")  |
| `code`      | String | Subject code (e.g., "CS201")            |
| `semester`  | int    | Semester number                         |
| `credits`   | int    | Credit hours                            |

**Relationships:** Belongs to one Program. Assigned to students via StudentSubject.

---

#### StudentSubject

| Attribute   | Type   | Description                        |
| ----------- | ------ | ---------------------------------- |
| `id`        | String | Unique identifier                  |
| `studentId` | String | FK → Student                       |
| `subjectId` | String | FK → Subject                       |
| `semester`  | int    | Current semester of assignment     |

**Relationships:** Links a Student to a Subject (created at enrollment time for semester 1).

---

### 4.2 Enums

#### Role
```
STUDENT, ADMIN
```

#### ApplicationStatus
```
APPLIED, SELECTED, REJECTED
```

> **Note:** Enrollment is modeled as a separate entity (`Enrollment`), not as an
> application status. When a SELECTED student enrolls, the application remains
> SELECTED and a new `Enrollment` record is created.

### 4.3 Entity-Relationship Summary

```
College ──1:N──► Department ──1:N──► Program ──1:N──► Subject
                                        │
                                        ├──1:N──► AdmissionCycle ──1:N──► Application
                                        │                                     │
                                        └──1:N──► Enrollment ◄────────────────┘
                                                      │
Student ──1:N──► Application                          │
    │                                                  │
    └──1:N──► StudentSubject ◄─────── (created upon enrollment)
                    │
                    └────► Subject
```

---

## 5. CLI Flow

### 5.1 Main Menu

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

### 5.2 Student Menu (after login)

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

### 5.3 Admin Menu (after login)

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

### 5.4 Application State Transitions

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
    (creates Enrollment record)
```

> **Design Note:** `SELECTED` is a terminal application state. Enrollment is
> tracked as a separate `Enrollment` entity, not as an application status change.

**Transition Rules:**

| From     | To       | Triggered By          | Validation                                   |
| -------- | -------- | --------------------- | -------------------------------------------- |
| APPLIED  | SELECTED | Admin (selection run) | Score meets cutoff; seats available           |
| APPLIED  | REJECTED | Admin (selection run) | Score below cutoff or no seats remaining      |

**On enrollment** (SELECTED application → creates Enrollment record):
- Student must have a SELECTED application
- Student must not already be enrolled in another program for the same cycle
- Application status remains SELECTED; a new `Enrollment` row is created

**Invalid transitions** (enforced by service layer):
- REJECTED → anything
- SELECTED → APPLIED (no rollback)

---

## 6. Cross-Cutting Concerns

### 6.1 Error Handling

- All user input validated at the controller layer (format checks).
- All business rules validated at the service layer (throws descriptive exceptions).
- Controllers catch exceptions and display user-friendly error messages.

### 6.2 ID Generation

- Each entity type maintains a counter in its CSV file.
- Format: `<PREFIX>-<ZERO_PADDED_NUMBER>` (e.g., `STU-0001`, `COL-0001`, `APP-0042`).
- The repository reads the max existing ID on startup and increments from there.

### 6.3 Configuration

- No hardcoded colleges, departments, or programs.
- Everything is created at runtime by the Admin through the CLI.
- The `/data` directory is created on first run if it doesn't exist.

---

*Document version: 1.1 — Phase 1 (updated in Phase 2 to correct status model)*
*Author: System Architect*
