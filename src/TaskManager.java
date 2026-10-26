import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class TaskManager {

    private List<Task> tasks;
    private int nextId;

    public TaskManager() {
        tasks = new ArrayList<>();
        nextId = 1;
    }

    public void recalculateNextId() {
        nextId = 1;
        for (Task t : tasks) {
            if (t.getId() >= nextId) {
                nextId = t.getId() + 1;
            }
        }
    }

    public List<Task> getAllTasks() {
        return tasks;
    }

    public String addTask(String title, String description, String priorityStr, String dueDateStr) {
        if (title == null || title.trim().isEmpty()) {
            return "Error: Task title cannot be blank.";
        }
        title = title.trim();

        Task.Priority priority;
        try {
            priority = Task.Priority.valueOf(priorityStr.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return "Error: Priority must be HIGH, MEDIUM, or LOW.";
        }

        LocalDate dueDate;
        try {
            dueDate = LocalDate.parse(dueDateStr.trim());
        } catch (DateTimeParseException e) {
            return "Error: Date must be in YYYY-MM-DD format (e.g. 2026-10-27).";
        }

        Task newTask = new Task(nextId, title, description.trim(), priority, dueDate);
        tasks.add(newTask);
        nextId++;
        return null;
    }

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

    public String deleteTask(int id) {
        Task t = findById(id);
        if (t == null) {
            return "Error: No task found with ID " + id + ".";
        }
        tasks.remove(t);
        return null;
    }

    private Task findById(int id) {
        for (Task t : tasks) {
            if (t.getId() == id) return t;
        }
        return null;
    }

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

    public double completionPercentage() {
        if (totalTasks() == 0) return 0.0;
        return (completedTasks() * 100.0) / totalTasks();
    }

    public void setTasks(List<Task> loaded) {
        this.tasks = loaded;
        recalculateNextId();
    }
}
