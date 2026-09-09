package academia.model;

public class Instructor {
    private final String id;
    private final String name;
    private final String email;

    public Instructor(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }

    @Override
    public String toString() {
        return String.format("%-8s %-20s %-25s", id, name, email);
    }
}
