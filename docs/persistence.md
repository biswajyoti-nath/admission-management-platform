# Persistence Strategy

The Admission Management Platform relies on a custom flat-file CSV persistence mechanism located in the `src/com/admission/repository/csv` package.

## CSV File Structure

All data is stored in the `data/` directory (or `qa_data/` for testing), using one CSV file per entity:
*   `admins.csv`
*   `students.csv`
*   `colleges.csv`
*   `departments.csv`
*   `programs.csv`
*   `subjects.csv`
*   `cycles.csv`
*   `applications.csv`
*   `enrollments.csv`
*   `student_subjects.csv`

The first row of every file strictly contains the column headers, enforced by `CsvUtil.ensureFile(filePath, HEADERS)`.

## ID Generation Strategy

The system utilizes an auto-incrementing ID pattern, combined with an entity-specific prefix:
*   Format: `PREFIX-XXXX` (e.g., `APP-0001`, `STU-0042`)
*   Generation: The `generateId()` function in each CSV Repository parses the *entire CSV file* into memory, extracts the numeric portion of the ID column (index 0) by splitting on `-`, finds the `max` value, and increments it by 1.

## Read/Write Behavior

The underlying persistence logic dictates how records are mutated:
*   **Read**: Queries like `findAll()` or `findById()` read the entire CSV file into memory as a `List<String[]>`, convert the arrays into objects, and perform filtering.
*   **Create (Append)**: `save()` utilizes `CsvUtil.append()` which directly appends a new comma-separated line to the end of the target file using standard file IO in append mode.
*   **Update/Delete (Rewrite)**: Modifications or deletions (`updateAll()`) require the entire dataset to be rewritten. The repository maps updated objects back to a `List<String[]>`, clears the existing file, and writes the entire structure sequentially using a `PrintWriter`.

## Known Risks and Mitigations

### 1. No Concurrency Support
The current implementation of `CsvUtil` does not employ read/write locks.
*   **Risk**: Concurrent attempts to `save()` or `updateAll()` from different threads could lead to file corruption, data overwrites, or parsing exceptions.
*   **Mitigation**: The system is designed as a single-threaded CLI application, nullifying race conditions in standard operation.

### 2. Full File Scans (Memory Overhead)
*   **Risk**: Fetching a single record via `findById()` parses the entire dataset. As the system scales, memory consumption will increase linearly.
*   **Mitigation**: Given the prototype nature of the platform, the dataset is assumed to comfortably fit within standard JVM heap allocations.

### 3. Non-Atomic Writes
*   **Risk**: If the application crashes midway through `updateAll()`, the CSV file could be left incomplete or empty, resulting in permanent data loss.
*   **Mitigation**: Currently unmitigated. A robust solution would involve writing to a temporary file (`file.csv.tmp`) and performing an OS-level atomic move/rename to replace the original file.
