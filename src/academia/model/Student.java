package academia.model;

import academia.util.Persistable;

import java.util.ArrayList;
import java.util.List;

public class Student implements Persistable {
    private final String id;
    private String regNo;
    private String name;
    private String email;
    private final List<String> enrolledCourseCodes = new ArrayList<>();

    public Student(String id, String regNo, String name, String email) {
        this.id = id;
        this.regNo = regNo;
        this.name = name;
        this.email = email;
    }

    public String getId() { return id; }
    public String getRegNo() { return regNo; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public List<String> getEnrolledCourseCodes() { return enrolledCourseCodes; }

    public void setRegNo(String regNo) { this.regNo = regNo; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }

    public void addCourse(String courseCode) {
        if (!enrolledCourseCodes.contains(courseCode)) {
            enrolledCourseCodes.add(courseCode);
        }
    }

    public void removeCourse(String courseCode) {
        enrolledCourseCodes.remove(courseCode);
    }

    /** id,regNo,name,email,course1|course2|... */
    @Override
    public String toCsv() {
        String courses = String.join("|", enrolledCourseCodes);
        return String.join(",", id, regNo, name, email, courses);
    }

    public static Student fromCsv(String line) {
        String[] parts = line.split(",", -1);
        Student s = new Student(parts[0], parts[1], parts[2], parts[3]);
        if (parts.length > 4 && !parts[4].isBlank()) {
            for (String code : parts[4].split("\\|")) {
                s.addCourse(code);
            }
        }
        return s;
    }

    @Override
    public String toString() {
        return String.format("%-8s %-12s %-20s %-25s courses: %s",
                id, regNo, name, email, enrolledCourseCodes);
    }
}
