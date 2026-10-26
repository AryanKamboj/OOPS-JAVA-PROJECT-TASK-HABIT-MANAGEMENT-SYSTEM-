import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a single task in the Task & Habit Management System.
 * Demonstrates encapsulation: all fields are private, accessed via getters/setters.
 */
public class Task {

    // Priority options a task can have
    public enum Priority { HIGH, MEDIUM, LOW }

    private int id;
    private String title;
    private String description;
    private Priority priority;
    private LocalDate dueDate;
    private boolean completed;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    // Parameterized constructor — every task must be initialized with all required data
    public Task(int id, String title, String description, Priority priority, LocalDate dueDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.dueDate = dueDate;
        this.completed = false; // new tasks start as pending
    }

    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Priority getPriority() { return priority; }
    public LocalDate getDueDate() { return dueDate; }
    public boolean isCompleted() { return completed; }

    // Setter — only completion status can change after creation
    public void setCompleted(boolean completed) { this.completed = completed; }

    // Returns true if the task is past its due date and not yet completed
    public boolean isOverdue() {
        return !completed && dueDate.isBefore(LocalDate.now());
    }

    // Human-readable date string
    public String getDueDateString() {
        return dueDate.format(DATE_FORMAT);
    }

    // Formats this task as a pipe-delimited line for saving to file
    public String toFileString() {
        return id + "|" + title + "|" + description + "|" + priority + "|" + dueDate + "|" + completed;
    }

    // Recreates a Task object from a saved file line
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
