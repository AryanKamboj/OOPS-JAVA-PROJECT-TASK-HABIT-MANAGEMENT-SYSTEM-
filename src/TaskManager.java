import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the collection of tasks.
 * Handles adding, listing, completing, and deleting tasks.
 */
public class TaskManager {

    private List<Task> tasks;
    private int nextId; // tracks the next available unique ID

    public TaskManager() {
        tasks = new ArrayList<>();
        nextId = 1;
    }

    // Called after loading data from file to set nextId correctly
    public void recalculateNextId() {
        nextId = 1;
        for (Task t : tasks) {
            if (t.getId() >= nextId) {
                nextId = t.getId() + 1;
            }
        }
    }

    // Returns an unmodifiable view of the task list for display and statistics
    public List<Task> getAllTasks() {
        return tasks;
    }

    // Adds a task with the given details. Returns error message or null on success.
    public String addTask(String title, String description, String priorityStr, String dueDateStr) {
        // Validate title
        if (title == null || title.trim().isEmpty()) {
            return "Error: Task title cannot be blank.";
        }
        title = title.trim();

        // Validate priority
        Task.Priority priority;
        try {
            priority = Task.Priority.valueOf(priorityStr.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return "Error: Priority must be HIGH, MEDIUM, or LOW.";
        }

        // Validate due date (expects YYYY-MM-DD from user input)
        LocalDate dueDate;
        try {
            dueDate = LocalDate.parse(dueDateStr.trim());
        } catch (DateTimeParseException e) {
            return "Error: Date must be in YYYY-MM-DD format (e.g. 2026-10-27).";
        }

        Task newTask = new Task(nextId, title, description.trim(), priority, dueDate);
        tasks.add(newTask);
        nextId++;
        return null; // null means success
    }

    // Marks the task with the given ID as completed. Returns error message or null.
    public String completeTask(int id) {
        Task t = findById(id);
        if (t == null) {
            return "Error: No task found with ID " + id + ".";
        }
        if (t.isCompleted()) {
            return "Task \"" + t.getTitle() + "\" is already marked as completed.";
        }
        t.setCompleted(true);
        return null;
    }

    // Removes the task with the given ID. Returns error message or null.
    public String deleteTask(int id) {
        Task t = findById(id);
        if (t == null) {
            return "Error: No task found with ID " + id + ".";
        }
        tasks.remove(t);
        return null;
    }

    // Finds a task by its ID, returns null if not found
    private Task findById(int id) {
        for (Task t : tasks) {
            if (t.getId() == id) return t;
        }
        return null;
    }

    // --- Statistics helpers ---

    public int totalTasks() { return tasks.size(); }

    public int completedTasks() {
        int count = 0;
        for (Task t : tasks) {
            if (t.isCompleted()) count++;
        }
        return count;
    }

    public int pendingTasks() {
        return totalTasks() - completedTasks();
    }

    public int overdueTasks() {
        int count = 0;
        for (Task t : tasks) {
            if (t.isOverdue()) count++;
        }
        return count;
    }

    // Returns completion percentage, or 0.0 if there are no tasks
    public double completionPercentage() {
        if (totalTasks() == 0) return 0.0;
        return (completedTasks() * 100.0) / totalTasks();
    }

    // Replaces the internal list — used when loading saved data
    public void setTasks(List<Task> loaded) {
        this.tasks = loaded;
        recalculateNextId();
    }
}
