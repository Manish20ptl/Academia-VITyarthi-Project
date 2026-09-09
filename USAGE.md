# Usage Guide

After running `java -cp out academia.Main`, you'll see:

```
--- Main Menu ---
1. Manage Students
2. Manage Courses
3. Manage Enrollment & Grades
4. Backup Data
5. Reports
6. Exit
Enter your choice:
```

## 1. Manage Students

```
-- Manage Students --
1. Add  2. List  3. Search  4. Update  5. Delete  6. Back
```
- **Add**: enter Registration No., Name, Email. The system assigns an ID like `STU0001`.
- **List**: prints every student on record.
- **Search**: matches on ID, registration number, or name (case-insensitive, partial match).
- **Update**: enter a Student ID, then a new name/email (leave blank to keep the current value).
- **Delete**: removes a student by ID.

## 2. Manage Courses

```
-- Manage Courses --
1. Add  2. List  3. Search  4. Update  5. Delete  6. Back
```
- **Add**: enter Course Code (e.g. `CS101`), Title, Credits, Instructor, Semester.
- **List / Search / Update / Delete**: same pattern as students, keyed by course code.

## 3. Manage Enrollment & Grades

```
-- Enrollment & Grades --
1. Enroll  2. Unenroll  3. Record Grade  4. View Enrollments  5. Back
```
- **Enroll**: give a Student ID and Course Code. Fails with a clear message if either
  doesn't exist, or if the student is already enrolled in that course.
- **Unenroll**: removes the link between a student and a course.
- **Record Grade**: enter a letter grade (`S`, `A`, `B`, `C`, `D`, `E`, `F`) for an
  existing enrollment.
- **View Enrollments**: lists every enrollment record with its grade.

## 4. Backup Data

Copies every CSV file in `data/` into a new folder named
`data/backup_YYYYMMDD_HHmmss/`. Use this before making bulk changes.

## 5. Reports

```
-- Reports --
1. GPA Distribution  2. Student Transcript  3. Back
```
- **GPA Distribution**: buckets all students into GPA bands (9-10, 7.5-8.9, 6-7.4,
  below 6, and "no grades yet").
- **Student Transcript**: enter a Student ID to see all their enrollments, grades,
  and computed GPA.

## Example Session

```
1        -> Manage Students
1        -> Add
REG001
Asha Rao
asha@example.com
2        -> Manage Courses
1        -> Add
CS101
Data Structures
4
Dr. Mehta
Sem3
3        -> Manage Enrollment & Grades
1        -> Enroll
STU0001
CS101
3        -> Manage Enrollment & Grades
3        -> Record Grade
STU0001
CS101
A
5        -> Reports
2        -> Student Transcript
STU0001
6        -> Exit
```

Data is saved to `data/*.csv` after every change, so it's safe to exit
and resume later — the app reloads everything on startup.
