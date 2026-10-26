import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Task & Habit Management System
 * CSC2104 — Object Oriented Programming
 * Manipal University Jaipur | Section T4
 *
 * Team:
 *   Aryan Kamboj   — Reg. No. 2502060008
 *   Aaditya Kalme  — Reg. No. 2502060005
 *
 * Entry point: displays the menu and coordinates all operations.
 */
public class Main {

    // Data folder is always resolved relative to where the .class files live (the project root)
    private static final String DATA_DIR = resolveDataDir();

    private static String resolveDataDir() {
        // When run via: java -cp . Main (from project root), user.dir is the project root
        // We create the data folder there if it doesn't exist
        String dir = System.getProperty("user.dir") + java.io.File.separator + "data";
        new java.io.File(dir).mkdirs();
        return dir;
    }

    private static TaskManager taskManager = new TaskManager();
    private static HabitManager habitManager = new HabitManager();
    private static DataStorage storage = new DataStorage(DATA_DIR);
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        printWelcome();
        loadData();

        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1": addTask();            break;
                case "2": viewTasks();          break;
                case "3": completeTask();       break;
                case "4": deleteTask();         break;
                case "5": addHabit();           break;
                case "6": viewHabits();         break;
                case "7": recordHabit();        break;
                case "8": viewStatistics();     break;
                case "9": saveData();           break;
                case "0": running = false;      break;
                default:
                    printError("Invalid choice. Please enter a number from the menu.");
            }
        }

        saveData();
        printLine();
        System.out.println("  Data saved. Goodbye! Good luck with your studies.");
        printLine();
    }

    // =========================================================
    //  MENU FEATURES
    // =========================================================

    private static void addTask() {
        printHeader("ADD NEW TASK");

        System.out.print("  Title       : ");
        String title = scanner.nextLine();

        System.out.print("  Description : ");
        String desc = scanner.nextLine();

        System.out.print("  Priority    : [HIGH / MEDIUM / LOW] : ");
        String priority = scanner.nextLine();

        System.out.print("  Due Date    : [YYYY-MM-DD] : ");
        String dueDate = scanner.nextLine();

        String error = taskManager.addTask(title, desc, priority, dueDate);
        if (error != null) {
            printError(error);
        } else {
            printSuccess("Task added successfully!");
        }
    }

    private static void viewTasks() {
        printHeader("ALL TASKS");
        List<Task> tasks = taskManager.getAllTasks();

        if (tasks.isEmpty()) {
            System.out.println("  No tasks found. Use option 1 to add a task.");
            return;
        }

        // Column headers
        System.out.println("  " + repeat("-", 88));
        System.out.printf("  %s%n", formatRow("ID", "Title", "Priority", "Status", "Due Date", "Description"));
        System.out.println("  " + repeat("-", 88));

        for (Task t : tasks) {
            String status = t.isCompleted() ? "DONE" : (t.isOverdue() ? "OVERDUE" : "PENDING");
            System.out.printf("  %s%n", formatRow(
                    String.valueOf(t.getId()),
                    t.getTitle(),
                    t.getPriority().toString(),
                    status,
                    t.getDueDateString(),
                    t.getDescription()
            ));
        }
        System.out.println("  " + repeat("-", 88));
    }

    private static void completeTask() {
        printHeader("MARK TASK AS COMPLETED");
        viewTasks();
        System.out.print("\n  Enter Task ID to mark as completed: ");
        int id = readInt();
        if (id < 0) return;

        String error = taskManager.completeTask(id);
        if (error != null) {
            printError(error);
        } else {
            printSuccess("Task marked as completed!");
        }
    }

    private static void deleteTask() {
        printHeader("DELETE TASK");
        viewTasks();
        System.out.print("\n  Enter Task ID to delete: ");
        int id = readInt();
        if (id < 0) return;

        String error = taskManager.deleteTask(id);
        if (error != null) {
            printError(error);
        } else {
            printSuccess("Task deleted successfully.");
        }
    }

    private static void addHabit() {
        printHeader("ADD NEW HABIT");
        System.out.print("  Habit name: ");
        String name = scanner.nextLine();

        String error = habitManager.addHabit(name);
        if (error != null) {
            printError(error);
        } else {
            printSuccess("Habit added successfully!");
        }
    }

    private static void viewHabits() {
        printHeader("ALL HABITS");
        List<Habit> habits = habitManager.getAllHabits();

        if (habits.isEmpty()) {
            System.out.println("  No habits found. Use option 5 to add a habit.");
            return;
        }

        System.out.println("  " + repeat("-", 50));
        System.out.printf("  %-5s  %-25s  %s%n", "ID", "Habit Name", "Total Completions");
        System.out.println("  " + repeat("-", 50));
        for (Habit h : habits) {
            System.out.printf("  %-5d  %-25s  %d%n", h.getId(), h.getName(), h.getTotalCompletions());
        }
        System.out.println("  " + repeat("-", 50));
    }

    private static void recordHabit() {
        printHeader("RECORD HABIT COMPLETION");
        viewHabits();
        System.out.print("\n  Enter Habit ID to record today's completion: ");
        int id = readInt();
        if (id < 0) return;

        String message = habitManager.recordCompletion(id);
        // recordCompletion returns an error if ID invalid, or a result message otherwise
        if (message.startsWith("Error")) {
            printError(message);
        } else {
            printSuccess(message);
        }
    }

    private static void viewStatistics() {
        printHeader("PROGRESS STATISTICS");

        int total      = taskManager.totalTasks();
        int completed  = taskManager.completedTasks();
        int pending    = taskManager.pendingTasks();
        int overdue    = taskManager.overdueTasks();
        double pct     = taskManager.completionPercentage();

        int totalHabits      = habitManager.getAllHabits().size();
        int totalCompletions = habitManager.totalCompletions();

        System.out.println("  " + repeat("-", 40));
        System.out.println("  TASKS");
        System.out.println("  " + repeat("-", 40));
        System.out.printf("  %-25s : %d%n", "Total Tasks", total);
        System.out.printf("  %-25s : %d%n", "Completed", completed);
        System.out.printf("  %-25s : %d%n", "Pending", pending);
        System.out.printf("  %-25s : %d%n", "Overdue", overdue);
        System.out.printf("  %-25s : %.1f%%%n", "Completion Rate", pct);
        System.out.println("  " + repeat("-", 40));
        System.out.println("  HABITS");
        System.out.println("  " + repeat("-", 40));
        System.out.printf("  %-25s : %d%n", "Total Habits", totalHabits);
        System.out.printf("  %-25s : %d%n", "Total Completions", totalCompletions);
        System.out.println("  " + repeat("-", 40));

        // Progress bar (visual aid)
        if (total > 0) {
            int filled = (int) (pct / 5); // each block = 5%
            String bar = "[" + repeat("#", filled) + repeat(".", 20 - filled) + "]";
            System.out.println("  Progress: " + bar + " " + String.format("%.1f", pct) + "%");
        }
    }

    private static void saveData() {
        try {
            storage.saveTasks(taskManager.getAllTasks());
            storage.saveHabits(habitManager.getAllHabits());
            printSuccess("Data saved to the 'data' folder.");
        } catch (IOException e) {
            printError("Could not save data: " + e.getMessage());
        }
    }

    private static void loadData() {
        List<Task> savedTasks = storage.loadTasks();
        List<Habit> savedHabits = storage.loadHabits();

        taskManager.setTasks(savedTasks);
        habitManager.setHabits(savedHabits);

        int t = savedTasks.size();
        int h = savedHabits.size();
        System.out.println("  Loaded " + t + " task(s) and " + h + " habit(s) from saved data.");
    }

    // =========================================================
    //  DISPLAY HELPERS
    // =========================================================

    private static void printWelcome() {
        printLine();
        System.out.println("         TASK & HABIT MANAGEMENT SYSTEM");
        System.out.println("         CSC2104 | Manipal University Jaipur");
        System.out.println("         Team: Aryan Kamboj & Aaditya Kalme");
        printLine();
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("  ============= MAIN MENU =============");
        System.out.println("   1. Add a Task");
        System.out.println("   2. View All Tasks");
        System.out.println("   3. Mark a Task as Completed");
        System.out.println("   4. Delete a Task");
        System.out.println("   5. Add a Habit");
        System.out.println("   6. View Habits");
        System.out.println("   7. Record Today's Habit Completion");
        System.out.println("   8. View Progress Statistics");
        System.out.println("   9. Save Data");
        System.out.println("   0. Exit");
        System.out.println("  =====================================");
        System.out.print("  Your choice: ");
    }

    private static void printHeader(String title) {
        System.out.println();
        System.out.println("  --- " + title + " ---");
    }

    private static void printLine() {
        System.out.println("  " + repeat("=", 50));
    }

    private static void printSuccess(String msg) {
        System.out.println("  [OK] " + msg);
    }

    private static void printError(String msg) {
        System.out.println("  [!] " + msg);
    }

    // Repeats a character n times to build separator lines
    private static String repeat(String ch, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) sb.append(ch);
        return sb.toString();
    }

    // Formats a 6-column row for the task table
    private static String formatRow(String id, String title, String priority,
                                    String status, String dueDate, String desc) {
        return String.format("%-4s  %-22s  %-8s  %-7s  %-12s  %s",
                id, truncate(title, 22), priority, status, dueDate, truncate(desc, 20));
    }

    private static String truncate(String s, int max) {
        if (s.length() <= max) return s;
        return s.substring(0, max - 2) + "..";
    }

    // Reads an integer from the user; returns -1 on invalid input
    private static int readInt() {
        String line = scanner.nextLine().trim();
        try {
            return Integer.parseInt(line);
        } catch (NumberFormatException e) {
            printError("Invalid input — please enter a number.");
            return -1;
        }
    }
}
