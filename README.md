# Admission Management Platform

> A production-quality, CLI-based, configurable multi-college admission and academic management system built in pure Java with CSV-based persistence.

[![Phase](https://img.shields.io/badge/Phase-2%20%E2%80%93%20Storage%20Design-blue)]()
[![Java](https://img.shields.io/badge/Java-17+-orange)]()
[![License](https://img.shields.io/badge/License-MIT-green)]()

---

## Overview

This system manages the full student admission lifecycle across multiple colleges:

- **College Configuration** — Colleges, departments, programs, subjects
- **Admission Cycles** — Configurable yearly intake cycles per program
- **Application Processing** — Students apply with merit scores
- **Selection Engine** — Pluggable strategies (merit-based, extensible)
- **Enrollment & Academics** — Subject assignment upon enrollment

All data is persisted in flat CSV files — **no database, no external libraries**.

## Architecture

```
CLI Controllers  →  Service Layer  →  Repository Layer  →  CSV Files
                                            ↑
                                      Model (POJOs)
```

- **Model**: Pure domain entities (College, Student, Application, etc.)
- **Repository**: CSV-backed CRUD via interfaces (one file per entity)
- **Service**: Business logic, validation, orchestration
- **Controller**: CLI menus, user I/O, delegates to services

See [docs/system-design.md](docs/system-design.md) for full architectural details.

## Project Structure

```
admission-management-platform/
├── src/com/admission/
│   ├── model/          # Domain entities & enums
│   ├── service/        # Business logic interfaces & implementations
│   ├── repository/     # Data access interfaces & CSV implementations
│   ├── controller/     # CLI menu controllers
│   └── utils/          # CSV utilities, ID generation
├── data/               # CSV data files (created at runtime)
│   ├── colleges.csv
│   ├── departments.csv
│   ├── programs.csv
│   ├── admission_cycles.csv
│   ├── students.csv
│   ├── admins.csv
│   ├── applications.csv
│   ├── enrollments.csv
│   ├── subjects.csv
│   └── student_subjects.csv
├── docs/               # Design documentation
│   ├── system-design.md
│   ├── data-model.md
│   ├── cli-flow.md
│   └── csv-design.md
└── README.md
```

## Current Phase

**Phase 2 — File Storage Design** (completed)

| Phase | Status |
| ----- | ------ |
| 1. System Design | ✅ Completed |
| 2. File Storage Design | ✅ Completed |
| 3. Interface Design | ⬜ Pending |
| 4. Class Skeletons | ⬜ Pending |
| 5. Implementation | ⬜ Pending |
| 6. Demo | ⬜ Pending |

## How to Run

> *Available after Phase 5 — Implementation*

```bash
javac -d out src/com/admission/**/*.java
java -cp out com.admission.Main
```

## Documentation

| Document | Description |
| -------- | ----------- |
| [System Design](docs/system-design.md) | Architecture, SOLID mapping, data flow |
| [Data Model](docs/data-model.md) | Entity definitions, relationships, ID conventions |
| [CLI Flow](docs/cli-flow.md) | Menu structures, state transitions |
| [CSV Design](docs/csv-design.md) | File schemas, ID generation, update strategies, constraints |

## Persistence Layer (CSV)

All data is stored in **flat CSV files** under the `/data` directory:

- **10 CSV files** — one per entity (colleges, departments, programs, etc.)
- **Auto-created** — files are created with headers on first run
- **Prefixed IDs** — auto-generated as `PREFIX-XXXX` (e.g., `STU-0001`)
- **Append for inserts** — O(1) writes for new records
- **Full rewrite for updates** — necessary because CSV rows are variable-length
- **Referential integrity** — enforced at the service layer, not file level
- **No joins** — relationships resolved programmatically via foreign key columns

See [docs/csv-design.md](docs/csv-design.md) for complete schema definitions.

## Design Principles

- **SOLID** — Every class has one responsibility; abstractions over concretions
- **No external dependencies** — Pure Java, CSV persistence
- **Fully configurable** — No hardcoded colleges or programs
- **Pluggable selection** — Strategy pattern for admission selection
- **Clean status model** — `ApplicationStatus` is {APPLIED, SELECTED, REJECTED}; enrollment is a separate entity

---

## License

MIT
