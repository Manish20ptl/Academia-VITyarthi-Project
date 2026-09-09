package academia.exception;

public class CourseNotFoundException extends Exception {
    public CourseNotFoundException(String courseCode) {
        super("No course found with code: " + courseCode);
    }
}
