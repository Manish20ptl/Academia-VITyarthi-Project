# Problem Statement

Small academic departments and institutes that don't have (or don't need)
a full ERP system still need to keep track of the same core facts: which
students exist, which courses are offered, who is enrolled in what, and
what grade each student earned. Doing this by hand in spreadsheets is
error-prone — duplicate enrollments slip through, records get overwritten,
and there's no single place to compute a student's GPA.

**Academia** solves this at a small scale: a single-user, console-based
Java application that stores student, course, and enrollment records in
plain CSV files, validates operations (you can't enroll a student in a
course that doesn't exist, or twice in the same course), and can generate
a transcript with GPA, or a GPA distribution across every student on
record — all without needing a database server or network connection.

## Scope of the Project

**In scope:**
- CRUD (create, read, update, delete) operations for students and courses
- Enrolling/unenrolling students in courses, and recording letter grades
- Computing GPA per student and a GPA distribution report across students
- Persisting all data to CSV files, with a one-click timestamped backup
- Input validation and custom exceptions for the common failure cases
  (missing student, missing course, duplicate enrollment)

**Out of scope (possible future work, not attempted here):**
- Multi-user access / authentication / role-based permissions
- A graphical user interface or web interface
- A relational database backend (CSV was chosen deliberately for
  simplicity and portability, per the assignment's freedom to choose an
  appropriate storage approach for a Java CLI project)
- Automated grade calculation from multiple assessment components
  (only a single letter grade per enrollment is modeled)

## Target Users

- **Academic administrators / department staff** at a small institute who
  need a lightweight way to track students, courses, and grades without
  provisioning a full campus ERP system.
- **Instructors** who want a quick way to look up which students are
  enrolled in their course and what grades have been recorded.
- As coursework, the immediate "user" is the evaluator running the CLI to
  verify the functional requirements below.

## High-Level Features

1. **Student Management** — add, list, search (by name/reg. no./ID),
   update, and delete student records.
2. **Course Management** — add, list, search (by code/title/instructor),
   update, and delete course records.
3. **Enrollment & Grades** — enroll/unenroll a student in a course
   (with duplicate-enrollment and missing-record checks), record a
   letter grade, and view all enrollment records.
4. **Reporting** — print a full transcript with computed GPA for a given
   student, and a GPA distribution report bucketing all students into
   GPA bands.
5. **Backup** — copy all current data files into a timestamped backup
   folder on demand.

## Non-Functional Requirements

- **Performance** — all operations run in-memory against small in-process
  lists (`List<Student>`, `List<Course>`, `List<Enrollment>`) with CSV
  writes only on change, so response time for every menu action is
  effectively instant for realistic class sizes (hundreds of students).
- **Reliability** — every mutating action (add/update/delete/enroll/grade)
  immediately persists to its CSV file, so data isn't lost if the program
  is closed unexpectedly between menu actions.
- **Usability** — a numbered, always-visible menu at every level means the
  user is never more than one typo away from finding their way back;
  invalid numeric input is caught and re-prompted instead of crashing the
  session.
- **Maintainability** — the codebase is split into `model` / `service` /
  `exception` / `util` packages so a change to, say, storage format only
  touches `FileUtil`, not the CLI or the business logic in the services.
- **Error handling strategy** — three custom checked exceptions
  (`StudentNotFoundException`, `CourseNotFoundException`,
  `DuplicateEnrollmentException`) turn invalid operations into a clear,
  caught message rather than an uncaught stack trace.
- **Resource efficiency** — the whole dataset lives in memory as plain
  Java objects and small CSV files; there's no persistent network
  connection, background thread, or external process running.
