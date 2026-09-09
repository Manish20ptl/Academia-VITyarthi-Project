package academia.service;

import academia.exception.CourseNotFoundException;
import academia.model.Course;
import academia.util.FileUtil;
import academia.util.Searchable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CourseService implements Searchable<Course> {
    private static final String FILE = "courses.csv";
    private static final String HEADER = "code,title,credits,instructor,semester";

    private final List<Course> courses = new ArrayList<>();

    public void load() {
        courses.clear();
        for (String line : FileUtil.readLines(FILE)) {
            courses.add(Course.fromCsv(line));
        }
    }

    public void save() {
        List<String> lines = courses.stream().map(Course::toCsv).toList();
        FileUtil.writeLines(FILE, HEADER, lines);
    }

    public Course addCourse(String code, String title, int credits, String instructor, String semester) {
        Course c = new Course(code, title, credits, instructor, semester);
        courses.add(c);
        save();
        return c;
    }

    public List<Course> listAll() {
        return List.copyOf(courses);
    }

    public Optional<Course> findByCode(String code) {
        return courses.stream().filter(c -> c.getCode().equalsIgnoreCase(code)).findFirst();
    }

    public Course require(String code) throws CourseNotFoundException {
        return findByCode(code).orElseThrow(() -> new CourseNotFoundException(code));
    }

    public void updateCourse(String code, String title, Integer credits, String instructor, String semester)
            throws CourseNotFoundException {
        Course c = require(code);
        if (title != null && !title.isBlank()) c.setTitle(title);
        if (credits != null) c.setCredits(credits);
        if (instructor != null && !instructor.isBlank()) c.setInstructor(instructor);
        if (semester != null && !semester.isBlank()) c.setSemester(semester);
        save();
    }

    public void deleteCourse(String code) throws CourseNotFoundException {
        Course c = require(code);
        courses.remove(c);
        save();
    }

    @Override
    public List<Course> search(String keyword) {
        String k = keyword.toLowerCase();
        return courses.stream()
                .filter(c -> c.getTitle().toLowerCase().contains(k)
                        || c.getCode().toLowerCase().contains(k)
                        || c.getInstructor().toLowerCase().contains(k))
                .toList();
    }
}
