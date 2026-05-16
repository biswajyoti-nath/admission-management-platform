# Known Issues & Limitations

The Admission Management Platform is a prototype relying on file-system persistence. The following architectural limitations and technical debts exist in the current implementation:

## Data Layer Limitations

### 1. File Concurrency Risks
The `CsvUtil` read/write operations lack thread synchronization and OS-level file locking. If deployed in a multi-threaded web environment, concurrent reads and writes will likely cause file corruption. This is currently mitigated by the synchronous, single-threaded nature of the CLI interface.

### 2. Non-Atomic Batch Updates
Operations that update or delete data (e.g., `updateAll` in Repositories) write directly over the target CSV file. If the JVM crashes or the system loses power during the write process, the file may be left empty or partially written, resulting in permanent data loss.

### 3. High Memory Overhead (Full Scans)
Every query, including finding a single record by ID (`findById`), requires reading the entire CSV file into memory, parsing the strings, creating object instances, and iterating through them. The system's performance will degrade significantly as the dataset grows.

### 4. ID Generation Race Conditions
The `generateId()` function determines the next ID by parsing the maximum existing ID from the file. In a concurrent environment, two threads could read the same maximum ID simultaneously and generate duplicate IDs.

## Business Logic Limitations

### 1. Pseudo-Transactions
When an enrollment is created, the system attempts to auto-assign subjects. If subject assignment throws an exception, the `EnrollmentServiceImpl` attempts to manually delete the newly created enrollment to simulate a rollback. However, this relies on a secondary `updateAll` call which could also fail, potentially leaving the system in an inconsistent state.

### 2. Weak Foreign Key Constraints
Referential integrity is primarily enforced at the Service layer (e.g., checking if a cycle exists before applying). However, deletions do not cascade. Deleting a `Program` directly from the CSV or via an un-guarded admin function would leave orphaned `Subject`, `AdmissionCycle`, and `Application` records.

### 3. Password Security
Passwords for Students and Admins are stored in plain text within the CSV files.

### 4. Limited Search Capabilities
Filtering entities (e.g., finding applications by status) requires loading all applications into a Java List and applying stream filters. There is no indexing mechanism.
