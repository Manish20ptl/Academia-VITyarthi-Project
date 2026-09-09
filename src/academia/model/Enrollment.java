package academia.model;

import academia.util.Persistable;

public class Enrollment implements Persistable {
    private final String studentId;
    private final String courseCode;
    private String grade; // null/"" until graded

    public Enrollment(String studentId, String courseCode, String grade) {
        this.studentId = studentId;
        this.courseCode = courseCode;
        this.grade = grade;
    }

    public String getStudentId() { return studentId; }
    public String getCourseCode() { return courseCode; }
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    /** Converts a letter grade to grade points on a 10-point scale. Ungraded returns -1. */
    public double gradePoints() {
        if (grade == null || grade.isBlank()) return -1;
        return switch (grade.toUpperCase()) {
            case "S" -> 10.0;
            case "A" -> 9.0;
            case "B" -> 8.0;
            case "C" -> 7.0;
            case "D" -> 6.0;
            case "E" -> 5.0;
            default -> 0.0; // "F"
        };
    }

    /** studentId,courseCode,grade */
    @Override
    public String toCsv() {
        return String.join(",", studentId, courseCode, grade == null ? "" : grade);
    }

    public static Enrollment fromCsv(String line) {
        String[] p = line.split(",", -1);
        return new Enrollment(p[0], p[1], p.length > 2 ? p[2] : "");
    }

    @Override
    public String toString() {
        return String.format("%-8s -> %-8s grade:%s", studentId, courseCode,
                (grade == null || grade.isBlank()) ? "(none)" : grade);
    }
}
