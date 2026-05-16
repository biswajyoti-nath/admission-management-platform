# Admission Management Platform

A robust, console-based Java application designed to streamline the academic admission process for colleges. It provides dedicated flows for system administrators to manage academic structures and for students to apply and enroll in programs.

## Project Overview

The platform is built using pure Core Java, implementing a clean layered architecture (Controller → Service → Repository). It eschews traditional relational databases in favor of a custom, flat-file CSV persistence engine, making it lightweight and highly portable without sacrificing core architectural principles.

## Key Features (Implemented)

*   **Role-Based Access Control:** Distinct authentication and CLI menus for Students and System Administrators.
*   **Academic Modeling:** Admins can dynamically structure Colleges, Departments, Programs, and Subjects.
*   **Cycle Management:** Admins can open and close specific Admission Cycles for different programs.
*   **Application Flow:** Students can securely register, view active cycles, and submit applications. The system prevents duplicate applications and ensures cycles are active.
*   **Merit-Based Selection:** An automated strategy that sorts applications by score and selects the top candidates based on the cycle's defined seat quota.
*   **Automated Onboarding:** When a `SELECTED` student enrolls, the system automatically assigns them their required first-semester subjects.
*   **Analytics:** Administrators can view application volumes and enrollment statistics across programs.

## Architecture & Data Flow

The project strictly adheres to SOLID principles and Dependency Injection:

1.  **Presentation (Controllers):** `MainController`, `AdminController`, and `StudentController` handle the CLI UI and user input.
2.  **Business Logic (Services):** Classes like `ApplicationServiceImpl` and `EnrollmentServiceImpl` enforce business rules and orchestrate transactions across multiple data domains.
3.  **Data Access (Repositories):** Interfaces abstract the storage mechanism.
4.  **Persistence (CSV):** Implementations like `CsvStudentRepository` map domain objects to rows in flat CSV files.

**Typical Data Flow (Enrollment):**
Student CLI input → `StudentController` → `EnrollmentService.enroll()` → Validates `Application` via `ApplicationRepository` → Creates `Enrollment` via `EnrollmentRepository` → Assigns `StudentSubject` via `StudentSubjectRepository`.

For detailed architecture documentation, see [docs/architecture.md](docs/architecture.md).

## Persistence Design

Data is stored persistently in the `data/` directory using `.csv` files.
*   The system uses auto-incrementing, string-prefixed IDs (e.g., `APP-0001`, `STU-0012`).
*   New records are appended to the file.
*   Updates and Deletions require reading the entire file into memory, modifying the list, and rewriting the file completely (`updateAll`).

For detailed database documentation, see [docs/persistence.md](docs/persistence.md).

## How to Run

Ensure you have a JDK installed (Java 17+ recommended).

**1. Compile the Source Code:**
From the project root directory, compile all `.java` files into the `bin/` directory:
```bash
javac -d bin -sourcepath src src/com/admission/Main.java
```

**2. Execute the Application:**
Run the compiled `Main` class:
```bash
java -cp bin com.admission.Main
```

**3. Run the QA Test Suite:**
A comprehensive end-to-end test suite exists to validate core flows and error handling without polluting the main database.
```bash
javac -d bin -sourcepath src src/com/admission/QATest.java
java -cp bin com.admission.QATest
```

## Example Usage Flow

### Admin Workflow:
1. Login with the default admin account (created automatically on first run: `admin@test.com` / `admin123`).
2. Navigate to **Academic Management**.
3. Create a College → Department → Program → Add Subjects to the Program.
4. Navigate to **Cycle Management** and create a new Admission Cycle for the Program. Activate it.

### Student Workflow:
1. Choose **Student Registration** from the main menu.
2. Login with your new credentials.
3. Select **Apply for Admission**.
4. Choose the active Admission Cycle ID and enter your academic score.

### Selection & Enrollment Workflow:
1. Admin logs in, navigates to **Cycle Management**, and selects **Run Selection Algorithm**. The system ranks applicants by score and selects the top candidates.
2. The selected Student logs in, checks their application status (now `SELECTED`), and chooses **Confirm Enrollment**. They are automatically assigned their first-semester subjects.

## Known Limitations

This prototype relies on file-system persistence, which introduces specific constraints:
*   **No Concurrency:** The CSV reader/writer does not support concurrent access. Multi-threaded execution may cause file corruption.
*   **Memory Overhead:** Querying records requires parsing the entire CSV file into memory.
*   **Non-Atomic Writes:** Modifications overwrite the entire file. A system crash during a write operation could result in data loss.
*   **Weak Foreign Keys:** Referential integrity is enforced in the Service layer; there are no native database cascading deletes.

For a full breakdown of technical debt, see [docs/known-issues.md](docs/known-issues.md).

## Future Improvements

*   **Database Migration:** Swap the `Csv*Repository` implementations with JDBC/Hibernate repositories to utilize a true RDBMS (e.g., PostgreSQL, MySQL) for ACID compliance, indexing, and concurrency.
*   **Password Hashing:** Implement Bcrypt or Argon2 in the `AuthService` to secure stored credentials.
*   **Atomic CSV Writes:** If sticking with CSVs temporarily, update `CsvUtil` to write to a `.tmp` file and perform an OS-level atomic rename to prevent data loss.
*   **GUI / Web Interface:** Replace the CLI controllers with a web framework (e.g., Spring Boot) or desktop UI (e.g., JavaFX).
