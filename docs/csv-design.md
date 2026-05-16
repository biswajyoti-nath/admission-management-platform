# CSV Persistence Design

> **Phase 2 Deliverable — File storage schema, constraints, and strategies**

---

## 1. Folder Structure

```
admission-management-platform/
└── data/
    ├── colleges.csv
    ├── departments.csv
    ├── programs.csv
    ├── admission_cycles.csv
    ├── students.csv
    ├── admins.csv
    ├── applications.csv
    ├── enrollments.csv
    ├── subjects.csv
    └── student_subjects.csv
```

**Rules:**

- The `/data` directory is created automatically on first application run.
- Each CSV file is auto-created with a header row if it does not exist.
- One CSV file per entity — no shared files.
- File names use `snake_case` (plural form of the entity).

---

## 2. CSV Schemas

### 2.1 colleges.csv

| Column    | Type   | Constraints        | Description                  |
| --------- | ------ | ------------------ | ---------------------------- |
| `id`      | String | PK, format COL-XXXX | Auto-generated identifier   |
| `name`    | String | Required, non-empty | College name                |
| `code`    | String | Required, non-empty | Short code (e.g., "NIT-S")  |
| `address` | String | Required, non-empty | Physical address            |

**Header:**
```
id,name,code,address
```

**Example row:**
```
COL-0001,National Institute of Technology Silchar,NIT-S,Silchar Assam 788010
```

---

### 2.2 departments.csv

| Column      | Type   | Constraints          | Description                |
| ----------- | ------ | -------------------- | -------------------------- |
| `id`        | String | PK, format DEP-XXXX  | Auto-generated identifier |
| `collegeId` | String | FK → colleges.csv    | Parent college ID         |
| `name`      | String | Required, non-empty   | Department name           |
| `code`      | String | Required, non-empty   | Short code (e.g., "CSE") |

**Header:**
```
id,collegeId,name,code
```

**Example row:**
```
DEP-0001,COL-0001,Computer Science and Engineering,CSE
```

---

### 2.3 programs.csv

| Column         | Type   | Constraints          | Description              |
| -------------- | ------ | -------------------- | ------------------------ |
| `id`           | String | PK, format PRG-XXXX  | Auto-generated identifier|
| `departmentId` | String | FK → departments.csv | Parent department ID     |
| `name`         | String | Required, non-empty   | Program name             |
| `durationYears`| int    | Required, > 0         | Duration in years        |
| `totalSeats`   | int    | Required, > 0         | Max seat capacity        |

**Header:**
```
id,departmentId,name,durationYears,totalSeats
```

**Example row:**
```
PRG-0001,DEP-0001,B.Tech Computer Science and Engineering,4,120
```

---

### 2.4 admission_cycles.csv

| Column      | Type    | Constraints             | Description                |
| ----------- | ------- | ----------------------- | -------------------------- |
| `id`        | String  | PK, format CYC-XXXX     | Auto-generated identifier |
| `programId` | String  | FK → programs.csv       | Parent program ID         |
| `year`      | int     | Required, > 0            | Academic year (e.g., 2026)|
| `seatCount` | int     | Required, > 0            | Seats for this cycle      |
| `isActive`  | boolean | Required (true/false)    | Accepting applications?   |

**Header:**
```
id,programId,year,seatCount,isActive
```

**Example row:**
```
CYC-0001,PRG-0001,2026,60,true
```

---

### 2.5 students.csv

| Column     | Type   | Constraints              | Description            |
| ---------- | ------ | ------------------------ | ---------------------- |
| `id`       | String | PK, format STU-XXXX      | Auto-generated ID     |
| `name`     | String | Required, non-empty       | Full name             |
| `email`    | String | Required, unique          | Login email           |
| `password` | String | Required, non-empty       | Plain-text password   |
| `phone`    | String | Required, non-empty       | Contact number        |

**Header:**
```
id,name,email,password,phone
```

**Example row:**
```
STU-0001,Rahul Sharma,rahul@example.com,pass123,9876543210
```

> **Note:** The `role` field is omitted from the CSV — it is always `STUDENT`
> and is set implicitly in the model constructor.

---

### 2.6 admins.csv

| Column     | Type   | Constraints              | Description            |
| ---------- | ------ | ------------------------ | ---------------------- |
| `id`       | String | PK, format ADM-XXXX      | Auto-generated ID     |
| `name`     | String | Required, non-empty       | Full name             |
| `email`    | String | Required, unique          | Login email           |
| `password` | String | Required, non-empty       | Password              |

**Header:**
```
id,name,email,password
```

**Example row:**
```
ADM-0001,Admin User,admin@system.com,admin123
```

> **Note:** The `role` field is omitted — always `ADMIN` by construction.

---

### 2.7 applications.csv

| Column             | Type   | Constraints                          | Description           |
| ------------------ | ------ | ------------------------------------ | --------------------- |
| `id`               | String | PK, format APP-XXXX                  | Auto-generated ID    |
| `studentId`        | String | FK → students.csv                    | Applicant            |
| `admissionCycleId` | String | FK → admission_cycles.csv            | Target cycle         |
| `score`            | double | Required, ≥ 0.0                      | Merit score / %      |
| `status`           | String | Enum: APPLIED, SELECTED, REJECTED    | Current status       |
| `appliedDate`      | String | ISO-8601 date (yyyy-MM-dd)           | Submission date      |

**Header:**
```
id,studentId,admissionCycleId,score,status,appliedDate
```

**Example row:**
```
APP-0001,STU-0001,CYC-0001,87.5,APPLIED,2026-05-16
```

---

### 2.8 enrollments.csv

| Column          | Type   | Constraints              | Description                  |
| --------------- | ------ | ------------------------ | ---------------------------- |
| `id`            | String | PK, format ENR-XXXX      | Auto-generated ID           |
| `studentId`     | String | FK → students.csv        | Enrolled student            |
| `applicationId` | String | FK → applications.csv    | The SELECTED application    |
| `programId`     | String | FK → programs.csv        | Enrolled program            |
| `enrolledDate`  | String | ISO-8601 date (yyyy-MM-dd)| Enrollment date            |

**Header:**
```
id,studentId,applicationId,programId,enrolledDate
```

**Example row:**
```
ENR-0001,STU-0001,APP-0001,PRG-0001,2026-05-20
```

---

### 2.9 subjects.csv

| Column      | Type   | Constraints          | Description             |
| ----------- | ------ | -------------------- | ----------------------- |
| `id`        | String | PK, format SUB-XXXX  | Auto-generated ID      |
| `programId` | String | FK → programs.csv    | Parent program         |
| `name`      | String | Required, non-empty   | Subject name           |
| `code`      | String | Required, non-empty   | Subject code           |
| `semester`  | int    | Required, ≥ 1         | Semester number        |
| `credits`   | int    | Required, ≥ 1         | Credit hours           |

**Header:**
```
id,programId,name,code,semester,credits
```

**Example row:**
```
SUB-0001,PRG-0001,Data Structures,CS201,3,4
```

---

### 2.10 student_subjects.csv

| Column      | Type   | Constraints          | Description              |
| ----------- | ------ | -------------------- | ------------------------ |
| `id`        | String | PK, format SSB-XXXX  | Auto-generated ID       |
| `studentId` | String | FK → students.csv    | Assigned student        |
| `subjectId` | String | FK → subjects.csv    | Assigned subject        |
| `semester`  | int    | Required, ≥ 1         | Semester of assignment  |

**Header:**
```
id,studentId,subjectId,semester
```

**Example row:**
```
SSB-0001,STU-0001,SUB-0001,1
```

---

## 3. ID Generation Strategy

### Format

```
<PREFIX>-<ZERO_PADDED_4_DIGIT_NUMBER>
```

Examples: `COL-0001`, `STU-0042`, `APP-0123`

### Prefix Table

| Entity         | Prefix |
| -------------- | ------ |
| College        | COL    |
| Department     | DEP    |
| Program        | PRG    |
| AdmissionCycle | CYC    |
| Student        | STU    |
| Admin          | ADM    |
| Application    | APP    |
| Enrollment     | ENR    |
| Subject        | SUB    |
| StudentSubject | SSB    |

### Generation Algorithm

```
1. On repository initialization:
   a. Read all rows from the entity's CSV file
   b. Extract the numeric portion of each ID (e.g., "COL-0042" → 42)
   c. Store the maximum number found as `currentMax`
   d. If file is empty (no rows), set currentMax = 0

2. On save(entity):
   a. Increment currentMax by 1
   b. Generate ID: PREFIX + "-" + String.format("%04d", currentMax)
   c. Assign this ID to the entity
   d. Write the entity to CSV
```

**Why 4 digits?** Sufficient for the small-dataset assumption (<10k rows).
If the number exceeds 9999, the format naturally extends (e.g., `STU-10000`).

---

## 4. Update Strategy

### Insert — Append

New records are **appended** to the end of the CSV file.

**Why:** Appending is O(1) — the file is opened in append mode, a single line
is written, and the file is closed. No existing data is read or modified.

```
FileWriter(filePath, true)  // true = append mode
```

### Update — Full File Rewrite

Record updates require **reading all rows, modifying the target row, and
rewriting the entire file**.

**Why:** CSV files do not support in-place modification of a specific row.
Rows are variable-length; modifying one row's content changes byte offsets
of all subsequent rows. The only safe approach is:

```
1. Read all rows into memory (List<String[]>)
2. Find the row with the matching ID
3. Replace that row's fields with updated values
4. Write ALL rows (header + data) back to the file
```

**When updates occur:**

| Operation              | Strategy | Explanation                                     |
| ---------------------- | -------- | ----------------------------------------------- |
| Add College            | Append   | New record                                      |
| Add Application        | Append   | New record                                      |
| Run Selection          | Rewrite  | Updates status of multiple application rows     |
| Open/Close Cycle       | Rewrite  | Updates `isActive` field of a cycle row         |
| Student Registration   | Append   | New record                                      |
| Create Enrollment      | Append   | New record (applications.csv is NOT rewritten)  |

### Delete — Full File Rewrite (if needed)

Deletion follows the same pattern as update: read all, filter out the target,
rewrite. Currently, no entity supports deletion in the business requirements,
but the `CsvUtil` will support it for completeness.

---

## 5. Relationship Handling

### No Joins — Foreign Keys Resolved at Service Layer

CSV files have no join capability. Relationships are maintained through
**explicit foreign key columns** and resolved **programmatically**:

```
Example: "Get all departments for a college"

1. Service receives collegeId
2. Calls DepartmentRepository.findAll()
3. Filters: department.getCollegeId().equals(collegeId)
4. Returns filtered list
```

### Foreign Key Reference Map

| Child Entity   | FK Column          | Parent Entity  | Parent File           |
| -------------- | ------------------ | -------------- | --------------------- |
| Department     | collegeId          | College        | colleges.csv          |
| Program        | departmentId       | Department     | departments.csv       |
| AdmissionCycle | programId          | Program        | programs.csv          |
| Application    | studentId          | Student        | students.csv          |
| Application    | admissionCycleId   | AdmissionCycle | admission_cycles.csv  |
| Enrollment     | studentId          | Student        | students.csv          |
| Enrollment     | applicationId      | Application    | applications.csv      |
| Enrollment     | programId          | Program        | programs.csv          |
| Subject        | programId          | Program        | programs.csv          |
| StudentSubject | studentId          | Student        | students.csv          |
| StudentSubject | subjectId          | Subject        | subjects.csv          |

### Common Query Patterns

| Query                                    | Implementation                                       |
| ---------------------------------------- | ---------------------------------------------------- |
| Departments of a college                 | Filter departments by collegeId                      |
| Programs of a department                 | Filter programs by departmentId                      |
| Active cycles for a program              | Filter admission_cycles by programId AND isActive    |
| Applications for a cycle                 | Filter applications by admissionCycleId              |
| Student's applications                   | Filter applications by studentId                     |
| Subjects for a program                   | Filter subjects by programId                         |
| Enrolled subjects for a student          | Filter student_subjects by studentId, join subjects  |
| Check duplicate application              | Filter applications by studentId + admissionCycleId  |

---

## 6. Data Integrity Rules

### 6.1 Uniqueness Constraints

| Constraint                               | Enforced By        | Validation                                  |
| ---------------------------------------- | ------------------ | ------------------------------------------- |
| Student email must be unique             | StudentService     | Check all students before registration      |
| Admin email must be unique               | AdminService       | Check all admins before registration        |
| One application per (student + cycle)    | ApplicationService | Query existing applications before saving   |
| One enrollment per student per cycle     | EnrollmentService  | Query existing enrollments before creating  |

### 6.2 Referential Integrity

| Rule                                         | Enforced By          | Action on Violation      |
| -------------------------------------------- | -------------------- | ------------------------ |
| Department.collegeId must exist in colleges  | CollegeConfigService | Reject with error message|
| Program.departmentId must exist              | CollegeConfigService | Reject with error message|
| AdmissionCycle.programId must exist          | CollegeConfigService | Reject with error message|
| Application.studentId must exist             | ApplicationService   | Reject with error message|
| Application.admissionCycleId must exist      | ApplicationService   | Reject with error message|
| Enrollment.applicationId must exist & SELECTED| EnrollmentService  | Reject with error message|
| Subject.programId must exist                 | CollegeConfigService | Reject with error message|

### 6.3 Business Rules

| Rule                                            | Enforced By          |
| ----------------------------------------------- | -------------------- |
| Cannot apply if admission cycle is inactive     | ApplicationService   |
| Cannot enroll if application status ≠ SELECTED  | EnrollmentService    |
| Cannot enroll if already enrolled in same cycle | EnrollmentService    |
| Score must be ≥ 0.0                             | ApplicationService   |
| seatCount must be > 0                           | CollegeConfigService |
| Selection can only run on an active cycle       | SelectionService     |

---

## 7. Edge Cases & Error Handling

### 7.1 Missing Files

```
On startup / first access:
  If CSV file does not exist:
    → Create the file with the header row only
    → Return empty list for findAll()
```

### 7.2 Empty Files (Header Only)

```
If CSV file exists but contains only the header row:
  → findAll() returns an empty list
  → findById() returns null / Optional.empty()
  → ID generation starts from PREFIX-0001
```

### 7.3 Invalid Foreign Keys

```
If a foreign key references a non-existent parent:
  → Service layer throws IllegalArgumentException
  → Controller catches and displays: "Error: College with ID 'COL-9999' not found."
  → Operation is aborted; no partial writes
```

### 7.4 CSV Parsing Issues

```
If a row has fewer columns than expected:
  → Skip the row (log a warning to System.err)
  → Continue processing remaining rows

If a numeric field cannot be parsed:
  → Skip the row (log a warning to System.err)
  → Continue processing remaining rows
```

### 7.5 Concurrent Access

Not supported. The system is designed for single-user CLI operation.
No file locking is implemented. This is documented as Assumption A1.

---

## 8. CsvUtil Design

The `CsvUtil` class provides generic CSV read/write operations used by all
repository implementations.

### Responsibilities

| Method                     | Description                                      |
| -------------------------- | ------------------------------------------------ |
| `readAll(filePath)`        | Read all rows (excluding header) as `List<String[]>` |
| `writeAll(filePath, header, rows)` | Overwrite file with header + all rows   |
| `append(filePath, row)`   | Append a single row to the end of the file       |
| `ensureFile(filePath, header)` | Create file with header if it doesn't exist  |

### CSV Format Rules

- Delimiter: `,` (comma)
- No quoting unless a field contains a comma (fields should avoid commas)
- No multi-line fields
- Line separator: `\n` (system-dependent via `PrintWriter`)
- Encoding: UTF-8

### Serialization Convention

Each repository is responsible for converting its entity to/from a `String[]`:

```
// In CsvStudentRepository:
String[] toRow(Student s) →  [s.id, s.name, s.email, s.password, s.phone]
Student fromRow(String[] row) →  new Student(row[0], row[1], row[2], row[3], row[4])
```

---

## 9. Performance Assumptions

| Factor              | Assumption                                           |
| ------------------- | ---------------------------------------------------- |
| Dataset size        | < 10,000 rows per entity                             |
| Read strategy       | Full file scan (no indexing) — acceptable at this scale |
| Write strategy      | Full rewrite for updates — acceptable at this scale  |
| Memory              | Entire file fits in memory                           |
| Concurrency         | Single-user, single-thread — no locking needed       |
| Startup             | Read max ID per entity on first access (lazy init)   |

---

*Document version: 1.0 — Phase 2*
