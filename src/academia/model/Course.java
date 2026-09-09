package academia.model;

import academia.util.Persistable;

public class Course implements Persistable {
    private final String code;
    private String title;
    private int credits;
    private String instructor;
    private String semester;

    public Course(String code, String title, int credits, String instructor, String semester) {
        this.code = code;
        this.title = title;
        this.credits = credits;
        this.instructor = instructor;
        this.semester = semester;
    }

    public String getCode() { return code; }
    public String getTitle() { return title; }
    public int getCredits() { return credits; }
    public String getInstructor() { return instructor; }
    public String getSemester() { return semester; }

    public void setTitle(String title) { this.title = title; }
    public void setCredits(int credits) { this.credits = credits; }
    public void setInstructor(String instructor) { this.instructor = instructor; }
    public void setSemester(String semester) { this.semester = semester; }

    /** code,title,credits,instructor,semester */
    @Override
    public String toCsv() {
        return String.join(",", code, title, String.valueOf(credits), instructor, semester);
    }

    public static Course fromCsv(String line) {
        String[] p = line.split(",", -1);
        return new Course(p[0], p[1], Integer.parseInt(p[2]), p[3], p[4]);
    }

    @Override
    public String toString() {
        return String.format("%-8s %-25s credits:%-3d instructor:%-15s semester:%s",
                code, title, credits, instructor, semester);
    }
}
