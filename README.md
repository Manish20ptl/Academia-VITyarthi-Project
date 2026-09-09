# Academia — Student & Course Management System

## Overview

Academia is a console-based Java application built for the "Programming in
Java" course project. It manages students, courses, enrollments, and grades
for a small academic institute, with all data persisted to CSV files on
disk. It's a menu-driven CLI: no GUI, no database server — just `javac`,
`java`, and plain text files, which keeps the whole system easy to read,
run, and extend.

## Features

The system is organized into three major functional modules, each with a
clear input/output structure driven by numbered CLI menus:

1. **Student Management** — add, list, search, update, and delete student
   records (ID, registration number, name, email, enrolled courses).
2. **Course Management** — add, list, search, update, and delete courses
   (code, title, credits, instructor, semester).
3. **Enrollment, Grades & Reporting** — enroll/unenroll students in
   courses, record letter grades, print a per-student transcript with a
   computed GPA, and view a GPA distribution report across all students.

Supporting features:
- **Data Persistence** — every change is written immediately to CSV files
  under `data/`, which are reloaded automatically on the next run.
- **Backup** — one menu option copies all current CSV files into a
  timestamped `data/backup_YYYYMMDD_HHmmss/` folder.
- **Validation & custom exceptions** — invalid IDs/codes and duplicate
  enrollments are caught with dedicated checked exceptions instead of
  crashing the program.

## Technologies / Tools Used

- **Language**: Java 17+ (uses `switch` expressions, `var`, text blocks-style
  formatting, and the `java.nio.file` API)
- **Build**: plain `javac`/`java` — no Maven/Gradle required
- **Storage**: CSV files via `java.nio.file`, no external database
- **Version control**: Git / GitHub

## Project Structure

```
Academia/
├── README.md
├── statement.md
├── report/                     # project report PDF + source diagrams
├── src/
│   └── academia/
│       ├── Main.java                 # Entry point, CLI menu logic
│       ├── model/
│       │   ├── Student.java
│       │   ├── Course.java
│       │   ├── Enrollment.java
│       │   └── Instructor.java
│       ├── exception/
│       │   ├── StudentNotFoundException.java
│       │   ├── CourseNotFoundException.java
│       │   └── DuplicateEnrollmentException.java
│       ├── service/
│       │   ├── StudentService.java
│       │   ├── CourseService.java
│       │   ├── EnrollmentService.java
│       │   └── ReportService.java
│       └── util/
│           ├── Persistable.java      # interface: entity -> CSV line
│           ├── Searchable.java       # interface: generic keyword search
│           ├── IdGenerator.java
│           └── FileUtil.java         # CSV read/write + backups
└── data/                              # created automatically on first run
    ├── students.csv
    ├── courses.csv
    ├── enrollment.csv
    └── backup_YYYYMMDD_HHmmss/
```

That's 16 Java files across 4 packages plus the entry point — comfortably
inside the 5–10 "meaningful modules/classes/files" expectation, with a real
package structure (`model` / `service` / `exception` / `util`) rather than
one flat file.

## Steps to Install & Run

### 1. Prerequisites
Install a JDK (17 or later). Check with:
```
java -version
javac -version
```
If missing, install Temurin/OpenJDK from https://adoptium.net/.

### 2. Clone the repository
```
git clone https://github.com/<your-username>/<your-repo>.git
cd <your-repo>
```

### 3. Compile
```
javac -d out $(find src -name "*.java")
```
On Windows without Git Bash/WSL, compile explicitly instead:
```
javac -d out src/academia/*.java src/academia/model/*.java src/academia/exception/*.java src/academia/service/*.java src/academia/util/*.java
```

### 4. Run
```
java -cp out academia.Main
```
The app creates a `data/` folder with empty CSVs on first run — no manual
configuration needed.

## Instructions for Testing

There's no separate test suite (this is a CLI CRUD app), but the full
feature set can be exercised manually in one sitting:

1. **Manage Students** → Add a student → List → confirm it appears.
2. **Manage Courses** → Add a course → List → confirm it appears.
3. **Manage Enrollment & Grades** → Enroll the student in the course →
   try enrolling the same pair again → confirm you get a clear
   "already enrolled" message instead of a duplicate row or a crash.
4. Record a grade → View Enrollments → confirm the grade shows.
5. **Reports** → Student Transcript → confirm the GPA is computed
   correctly for the grade you entered.
6. **Reports** → GPA Distribution → confirm the student is bucketed
   correctly.
7. **Backup Data** → confirm a new `data/backup_<timestamp>/` folder
   appears with copies of the three CSV files.
8. Exit and re-run the app → confirm all data reloads exactly as left.
9. Try an invalid ID (e.g. enroll a nonexistent student) → confirm you get
   a `StudentNotFoundException` message, not a stack trace.

## Data Model Overview

- **Student**: ID, Registration No., Name, Email, Enrolled Course Codes
- **Course**: Code, Title, Credits, Instructor, Semester
- **Enrollment**: links a Student and a Course, plus an optional Grade
- **Instructor**: ID, Name, Email

## Design Notes

- `Persistable` and `Searchable<T>` are custom interfaces implemented by
  the model/service classes, keeping CSV serialization and keyword search
  generic rather than duplicated per entity.
- `StudentNotFoundException`, `CourseNotFoundException`, and
  `DuplicateEnrollmentException` are custom checked exceptions so invalid
  operations fail with a clear message instead of crashing the app.
- The `service` package separates business logic from the `Main` CLI layer
  and from the `model` classes, so each layer can be extended
  independently (e.g. swapping CSV storage for a real database later
  would only touch `FileUtil` and the services, not `Main` or the models).

## Screenshots

See `report/diagrams/06_terminal_screenshot_1.png` and `_2.png` for a full
sample session (add student/course, enroll, grade, transcript, GPA
distribution, backup).

## Author

Submitted as coursework for "Programming in Java".
