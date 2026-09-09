package academia;

import academia.exception.CourseNotFoundException;
import academia.exception.DuplicateEnrollmentException;
import academia.exception.StudentNotFoundException;
import academia.model.Course;
import academia.model.Student;
import academia.service.*;
import academia.util.FileUtil;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Academia — Student & Course Management System
 * A console-based Java application for managing students, courses,
 * enrollments, grades, backups, and reports.
 */
public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final StudentService studentService = new StudentService();
    private static final CourseService courseService = new CourseService();
    private static final EnrollmentService enrollmentService =
            new EnrollmentService(studentService, courseService);
    private static final ReportService reportService =
            new ReportService(studentService, enrollmentService);

    public static void main(String[] args) {
        FileUtil.ensureDataDir();
        studentService.load();
        courseService.load();
        enrollmentService.load();

        boolean running = true;
        while (running) {
            printMainMenu();
            switch (readInt("Enter your choice: ")) {
                case 1 -> manageStudents();
                case 2 -> manageCourses();
                case 3 -> manageEnrollment();
                case 4 -> backupData();
                case 5 -> reports();
                case 6 -> running = false;
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
        System.out.println("Data saved. Goodbye!");
    }

    // ---------- Main menu ----------

    private static void printMainMenu() {
        System.out.println();
        System.out.println("--- Main Menu ---");
        System.out.println("1. Manage Students");
        System.out.println("2. Manage Courses");
        System.out.println("3. Manage Enrollment & Grades");
        System.out.println("4. Backup Data");
        System.out.println("5. Reports");
        System.out.println("6. Exit");
    }

    // ---------- Students ----------

    private static void manageStudents() {
        System.out.println();
        System.out.println("-- Manage Students --");
        System.out.println("1. Add  2. List  3. Search  4. Update  5. Delete  6. Back");
        switch (readInt("Choice: ")) {
            case 1 -> {
                String regNo = readLine("Registration No: ");
                String name = readLine("Name: ");
                String email = readLine("Email: ");
                Student s = studentService.addStudent(regNo, name, email);
                System.out.println("Added: " + s);
            }
            case 2 -> studentService.listAll().forEach(System.out::println);
            case 3 -> {
                String kw = readLine("Search keyword: ");
                studentService.search(kw).forEach(System.out::println);
            }
            case 4 -> {
                String id = readLine("Student ID: ");
                try {
                    String name = readLine("New name (blank to skip): ");
                    String email = readLine("New email (blank to skip): ");
                    studentService.updateStudent(id, name, email);
                    System.out.println("Updated.");
                } catch (StudentNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            }
            case 5 -> {
                String id = readLine("Student ID: ");
                try {
                    studentService.deleteStudent(id);
                    System.out.println("Deleted.");
                } catch (StudentNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            }
            case 6 -> { /* back */ }
            default -> System.out.println("Invalid choice.");
        }
    }

    // ---------- Courses ----------

    private static void manageCourses() {
        System.out.println();
        System.out.println("-- Manage Courses --");
        System.out.println("1. Add  2. List  3. Search  4. Update  5. Delete  6. Back");
        switch (readInt("Choice: ")) {
            case 1 -> {
                String code = readLine("Course Code: ");
                String title = readLine("Title: ");
                int credits = readInt("Credits: ");
                String instructor = readLine("Instructor: ");
                String semester = readLine("Semester: ");
                Course c = courseService.addCourse(code, title, credits, instructor, semester);
                System.out.println("Added: " + c);
            }
            case 2 -> courseService.listAll().forEach(System.out::println);
            case 3 -> {
                String kw = readLine("Search keyword: ");
                courseService.search(kw).forEach(System.out::println);
            }
            case 4 -> {
                String code = readLine("Course Code: ");
                try {
                    String title = readLine("New title (blank to skip): ");
                    String instructor = readLine("New instructor (blank to skip): ");
                    String semester = readLine("New semester (blank to skip): ");
                    courseService.updateCourse(code, title, null, instructor, semester);
                    System.out.println("Updated.");
                } catch (CourseNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            }
            case 5 -> {
                String code = readLine("Course Code: ");
                try {
                    courseService.deleteCourse(code);
                    System.out.println("Deleted.");
                } catch (CourseNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            }
            case 6 -> { /* back */ }
            default -> System.out.println("Invalid choice.");
        }
    }

    // ---------- Enrollment & Grades ----------

    private static void manageEnrollment() {
        System.out.println();
        System.out.println("-- Enrollment & Grades --");
        System.out.println("1. Enroll  2. Unenroll  3. Record Grade  4. View Enrollments  5. Back");
        switch (readInt("Choice: ")) {
            case 1 -> {
                String sid = readLine("Student ID: ");
                String code = readLine("Course Code: ");
                try {
                    enrollmentService.enroll(sid, code);
                    System.out.println("Enrolled.");
                } catch (StudentNotFoundException | CourseNotFoundException | DuplicateEnrollmentException e) {
                    System.out.println(e.getMessage());
                }
            }
            case 2 -> {
                String sid = readLine("Student ID: ");
                String code = readLine("Course Code: ");
                try {
                    enrollmentService.unenroll(sid, code);
                    System.out.println("Unenrolled.");
                } catch (StudentNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            }
            case 3 -> {
                String sid = readLine("Student ID: ");
                String code = readLine("Course Code: ");
                String grade = readLine("Grade (S/A/B/C/D/E/F): ");
                enrollmentService.recordGrade(sid, code, grade);
                System.out.println("Grade recorded.");
            }
            case 4 -> enrollmentService.listAll().forEach(System.out::println);
            case 5 -> { /* back */ }
            default -> System.out.println("Invalid choice.");
        }
    }

    // ---------- Backup ----------

    private static void backupData() {
        Path backupDir = FileUtil.backupAll();
        System.out.println("Backup created at: " + backupDir.toAbsolutePath());
    }

    // ---------- Reports ----------

    private static void reports() {
        System.out.println();
        System.out.println("-- Reports --");
        System.out.println("1. GPA Distribution  2. Student Transcript  3. Back");
        switch (readInt("Choice: ")) {
            case 1 -> {
                Map<String, Integer> dist = reportService.gpaDistribution();
                dist.forEach((band, count) -> System.out.printf("%-15s : %d%n", band, count));
            }
            case 2 -> {
                String id = readLine("Student ID: ");
                studentService.findById(id).ifPresentOrElse(
                        reportService::printTranscript,
                        () -> System.out.println("No student found with ID: " + id)
                );
            }
            case 3 -> { /* back */ }
            default -> System.out.println("Invalid choice.");
        }
    }

    // ---------- Input helpers ----------

    private static String readLine(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
