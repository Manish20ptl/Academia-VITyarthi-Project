package academia.service;

import academia.model.Student;

import java.util.LinkedHashMap;
import java.util.Map;

public class ReportService {
    private final StudentService studentService;
    private final EnrollmentService enrollmentService;

    public ReportService(StudentService studentService, EnrollmentService enrollmentService) {
        this.studentService = studentService;
        this.enrollmentService = enrollmentService;
    }

    /** Buckets every student's GPA into letter-ish bands for a quick distribution view. */
    public Map<String, Integer> gpaDistribution() {
        Map<String, Integer> bands = new LinkedHashMap<>();
        bands.put("9.0 - 10.0", 0);
        bands.put("7.5 - 8.9", 0);
        bands.put("6.0 - 7.4", 0);
        bands.put("Below 6.0", 0);
        bands.put("No grades yet", 0);

        for (Student s : studentService.listAll()) {
            double gpa = enrollmentService.gpaFor(s.getId());
            if (gpa < 0) {
                bands.merge("No grades yet", 1, Integer::sum);
            } else if (gpa >= 9.0) {
                bands.merge("9.0 - 10.0", 1, Integer::sum);
            } else if (gpa >= 7.5) {
                bands.merge("7.5 - 8.9", 1, Integer::sum);
            } else if (gpa >= 6.0) {
                bands.merge("6.0 - 7.4", 1, Integer::sum);
            } else {
                bands.merge("Below 6.0", 1, Integer::sum);
            }
        }
        return bands;
    }

    public void printTranscript(Student s) {
        System.out.println("Transcript for " + s.getName() + " (" + s.getId() + ")");
        System.out.println("--------------------------------------------------");
        enrollmentService.forStudent(s.getId()).forEach(System.out::println);
        double gpa = enrollmentService.gpaFor(s.getId());
        System.out.println("--------------------------------------------------");
        System.out.println(gpa < 0 ? "GPA: not available (no grades yet)" : String.format("GPA: %.2f", gpa));
    }
}
