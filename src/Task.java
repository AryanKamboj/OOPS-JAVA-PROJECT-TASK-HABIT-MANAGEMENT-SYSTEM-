import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Task {

    public enum Priority { HIGH, MEDIUM, LOW }

    private int id;
    private String title;
    private String description;
    private Priority priority;
    private LocalDate dueDate;
    private boolean completed;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public Task(int id, String title, String description, Priority priority, LocalDate dueDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.dueDate = dueDate;
        this.completed = false;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Priority getPriority() { return priority; }
    public LocalDate getDueDate() { return dueDate; }
    public boolean isCompleted() { return completed; }

    public void setCompleted(boolean completed) { this.completed = completed; }

    public boolean isOverdue() {
        return !completed && dueDate.isBefore(LocalDate.now());
    }

    public String getDueDateString() {
        return dueDate.format(DATE_FORMAT);
    }

    public String toFileString() {
        return id + "|" + title + "|" + description + "|" + priority + "|" + dueDate + "|" + completed;
    }

    public static Task fromFileString(String line) {
        String[] parts = line.split("\\|", -1);
        if (parts.length != 6) return null;

        int id = Integer.parseInt(parts[0].trim());
        String title = parts[1].trim();
        String description = parts[2].trim();
        Priority priority = Priority.valueOf(parts[3].trim());
        LocalDate dueDate = LocalDate.parse(parts[4].trim());
        boolean completed = Boolean.parseBoolean(parts[5].trim());

        Task t = new Task(id, title, description, priority, dueDate);
        t.setCompleted(completed);
        return t;
    }

    @Override
    public String toString() {
        String status = completed ? "DONE  " : (isOverdue() ? "OVERDUE" : "PENDING");
        return String.format("[%3d] %-25s | %-8s | %-8s | %s | %s",
                id, title, priority, status, getDueDateString(), description);
    }
}
