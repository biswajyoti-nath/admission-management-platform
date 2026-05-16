# Admission Management Platform

> A production-quality, CLI-based, configurable multi-college admission and academic management system built in pure Java with CSV-based persistence.

[![Phase](https://img.shields.io/badge/Phase-1%20%E2%80%93%20System%20Design-blue)]()
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
├── docs/               # Design documentation
│   ├── system-design.md
│   ├── data-model.md
│   └── cli-flow.md
└── README.md
```

## Current Phase

**Phase 1 — System Design** (completed)
- System overview, scope, and assumptions
- Layered architecture with SOLID principles
- Domain model with 10 entities and relationships
- CLI flow with state transition diagrams

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

## Design Principles

- **SOLID** — Every class has one responsibility; abstractions over concretions
- **No external dependencies** — Pure Java, CSV persistence
- **Fully configurable** — No hardcoded colleges or programs
- **Pluggable selection** — Strategy pattern for admission selection

---

## License

MIT
