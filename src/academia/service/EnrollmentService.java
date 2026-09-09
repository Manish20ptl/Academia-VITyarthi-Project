package academia.service;

import academia.exception.CourseNotFoundException;
import academia.exception.DuplicateEnrollmentException;
import academia.exception.StudentNotFoundException;
import academia.model.Enrollment;
import academia.model.Student;
import academia.util.FileUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EnrollmentService {
    private static final String FILE = "enrollment.csv";
    private static final String HEADER = "studentId,courseCode,grade";

    private final List<Enrollment> enrollments = new ArrayList<>();
    private final StudentService studentService;
    private final CourseService courseService;

    public EnrollmentService(StudentService studentService, CourseService courseService) {
        this.studentService = studentService;
        this.courseService = courseService;
    }

    public void load() {
        enrollments.clear();
        for (String line : FileUtil.readLines(FILE)) {
            enrollments.add(Enrollment.fromCsv(line));
        }
    }

    public void save() {
        List<String> lines = enrollments.stream().map(Enrollment::toCsv).toList();
        FileUtil.writeLines(FILE, HEADER, lines);
    }

    public Enrollment enroll(String studentId, String courseCode)
            throws StudentNotFoundException, CourseNotFoundException, DuplicateEnrollmentException {
        Student student = studentService.require(studentId);
        courseService.require(courseCode); // validates the course exists

        boolean already = enrollments.stream()
                .anyMatch(e -> e.getStudentId().equals(studentId) && e.getCourseCode().equals(courseCode));
        if (already) {
            throw new DuplicateEnrollmentException(studentId, courseCode);
        }

        Enrollment e = new Enrollment(studentId, courseCode, "");
        enrollments.add(e);
        student.addCourse(courseCode);
        save();
        studentService.save();
        return e;
    }

    public void unenroll(String studentId, String courseCode) throws StudentNotFoundException {
        Student student = studentService.require(studentId);
        enrollments.removeIf(e -> e.getStudentId().equals(studentId) && e.getCourseCode().equals(courseCode));
        student.removeCourse(courseCode);
        save();
        studentService.save();
    }

    public void recordGrade(String studentId, String courseCode, String grade) {
        findEnrollment(studentId, courseCode).ifPresent(e -> {
            e.setGrade(grade);
            save();
        });
    }

    public Optional<Enrollment> findEnrollment(String studentId, String courseCode) {
        return enrollments.stream()
                .filter(e -> e.getStudentId().equals(studentId) && e.getCourseCode().equals(courseCode))
                .findFirst();
    }

    public List<Enrollment> forStudent(String studentId) {
        return enrollments.stream().filter(e -> e.getStudentId().equals(studentId)).toList();
    }

    public List<Enrollment> listAll() {
        return List.copyOf(enrollments);
    }

    /** Simple GPA: average grade points across graded enrollments for one student. Returns -1 if none graded. */
    public double gpaFor(String studentId) {
        List<Enrollment> graded = forStudent(studentId).stream()
                .filter(e -> e.gradePoints() >= 0)
                .toList();
        if (graded.isEmpty()) return -1;
        return graded.stream().mapToDouble(Enrollment::gradePoints).average().orElse(-1);
    }
}
