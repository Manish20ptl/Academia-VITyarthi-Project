package academia.service;

import academia.exception.StudentNotFoundException;
import academia.model.Student;
import academia.util.FileUtil;
import academia.util.IdGenerator;
import academia.util.Searchable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentService implements Searchable<Student> {
    private static final String FILE = "students.csv";
    private static final String HEADER = "id,regNo,name,email,courses";

    private final List<Student> students = new ArrayList<>();
    private final IdGenerator idGenerator = new IdGenerator("STU", 0);

    public void load() {
        students.clear();
        for (String line : FileUtil.readLines(FILE)) {
            Student s = Student.fromCsv(line);
            students.add(s);
            idGenerator.ensureAtLeast(Integer.parseInt(s.getId().replace("STU", "")));
        }
    }

    public void save() {
        List<String> lines = students.stream().map(Student::toCsv).toList();
        FileUtil.writeLines(FILE, HEADER, lines);
    }

    public Student addStudent(String regNo, String name, String email) {
        Student s = new Student(idGenerator.next(), regNo, name, email);
        students.add(s);
        save();
        return s;
    }

    public List<Student> listAll() {
        return List.copyOf(students);
    }

    public Optional<Student> findById(String id) {
        return students.stream().filter(s -> s.getId().equalsIgnoreCase(id)).findFirst();
    }

    public Student require(String id) throws StudentNotFoundException {
        return findById(id).orElseThrow(() -> new StudentNotFoundException(id));
    }

    public void updateStudent(String id, String name, String email) throws StudentNotFoundException {
        Student s = require(id);
        if (name != null && !name.isBlank()) s.setName(name);
        if (email != null && !email.isBlank()) s.setEmail(email);
        save();
    }

    public void deleteStudent(String id) throws StudentNotFoundException {
        Student s = require(id);
        students.remove(s);
        save();
    }

    @Override
    public List<Student> search(String keyword) {
        String k = keyword.toLowerCase();
        return students.stream()
                .filter(s -> s.getName().toLowerCase().contains(k)
                        || s.getRegNo().toLowerCase().contains(k)
                        || s.getId().toLowerCase().contains(k))
                .toList();
    }
}
