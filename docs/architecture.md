# System Architecture

This document describes the high-level architecture of the Admission Management Platform.

## Layered Architecture

The system follows a strict **Layered Architecture** pattern, divided into four main tiers:

1.  **Presentation Layer (Controllers)**
2.  **Business Logic Layer (Services)**
3.  **Data Access Layer (Repositories)**
4.  **Persistence Layer (CSV Files)**

### 1. Presentation Layer (Controllers)
The entry point of the application is a CLI interface driven by `MainController`, `AdminController`, and `StudentController`.
*   **Responsibilities:** Handles user input, routing, session management (login state), and displaying text-based menus and output.
*   **Dependencies:** Depends strictly on the Service Layer interfaces. It does not interact with the repository or database directly.

### 2. Business Logic Layer (Services)
The core logic resides here, encapsulated in dedicated services like `AdminServiceImpl`, `ApplicationServiceImpl`, `SelectionServiceImpl`, and `EnrollmentServiceImpl`.
*   **Responsibilities:** Enforces business rules (e.g., duplicate application prevention, selection logic, enrollment validation). It orchestration multiple repositories to complete a transaction (e.g., enrolling a student also assigns subjects).
*   **Dependencies:** Interacts with domain models and depends heavily on Repository interfaces.

### 3. Data Access Layer (Repositories)
Abstracts data storage operations using the Repository Pattern.
*   **Responsibilities:** Provides CRUD (Create, Read, Update, Delete) operations for domain entities. Hides the implementation details of the underlying CSV storage from the Service layer.
*   **Dependencies:** Reads and writes to the CSV files using `CsvUtil`. Translates string arrays to domain Objects and vice versa.

### 4. Persistence Layer (CSV Data)
All state is persistently stored in CSV files within the `data/` or `qa_data/` directory.
*   **Responsibilities:** Permanent data storage for the system across executions.

---

## Dependency Flow & SOLID Principles

The system's dependency flow is strictly top-down:
**Controller → Service → Repository → Persistence**

We heavily utilize **SOLID principles** to maintain a clean architecture:
*   **Single Responsibility Principle (SRP):** Each service and repository handles a distinct domain (e.g., `ApplicationService` only handles applications; `SelectionService` only handles selection logic).
*   **Open/Closed Principle (OCP):** Selection logic uses the `SelectionStrategy` interface. We currently have `MeritBasedSelectionStrategy`. We can add new selection algorithms without modifying the core `SelectionServiceImpl`.
*   **Liskov Substitution Principle (LSP):** Repository implementations (`CsvStudentRepository`) can be safely substituted wherever the interface (`StudentRepository`) is expected.
*   **Interface Segregation Principle (ISP):** Repositories are split into small, specific interfaces (`AdminRepository`, `SubjectRepository`) rather than a single monolithic database interface.
*   **Dependency Inversion Principle (DIP):** Controllers depend on Service interfaces, and Services depend on Repository interfaces. Dependencies are injected via constructors (e.g., in `Main.java`), keeping components decoupled and testable.
