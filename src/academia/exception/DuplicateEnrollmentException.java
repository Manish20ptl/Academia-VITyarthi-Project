package academia.exception;

public class DuplicateEnrollmentException extends Exception {
    public DuplicateEnrollmentException(String studentId, String courseCode) {
        super("Student " + studentId + " is already enrolled in course " + courseCode);
    }
}
