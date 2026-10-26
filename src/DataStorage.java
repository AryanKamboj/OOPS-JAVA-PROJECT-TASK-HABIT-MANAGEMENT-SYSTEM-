import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataStorage {

    private final String tasksFilePath;
    private final String habitsFilePath;

    public DataStorage(String dataDir) {
        if (!dataDir.endsWith(File.separator)) {
            dataDir = dataDir + File.separator;
        }
        this.tasksFilePath = dataDir + "tasks.txt";
        this.habitsFilePath = dataDir + "habits.txt";
    }

    public void saveTasks(List<Task> tasks) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tasksFilePath))) {
            for (Task t : tasks) {
                writer.write(t.toFileString());
                writer.newLine();
            }
        }
    }

    public void saveHabits(List<Habit> habits) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(habitsFilePath))) {
            for (Habit h : habits) {
                writer.write(h.toFileString());
                writer.newLine();
            }
        }
    }

    public List<Task> loadTasks() {
        List<Task> tasks = new ArrayList<>();
        File file = new File(tasksFilePath);

        if (!file.exists()) {
            return tasks;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();
                if (line.isEmpty()) continue;

                Task t = Task.fromFileString(line);
                if (t == null) {
                    System.out.println("  [Warning] Skipping malformed task record at line " + lineNumber);
                } else {
                    tasks.add(t);
                }
            }
        } catch (IOException e) {
            System.out.println("  [Warning] Could not read tasks file: " + e.getMessage());
        }

        return tasks;
    }

    public List<Habit> loadHabits() {
        List<Habit> habits = new ArrayList<>();
        File file = new File(habitsFilePath);

        if (!file.exists()) {
            return habits;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();
                if (line.isEmpty()) continue;

                Habit h = Habit.fromFileString(line);
                if (h == null) {
                    System.out.println("  [Warning] Skipping malformed habit record at line " + lineNumber);
                } else {
                    habits.add(h);
                }
            }
        } catch (IOException e) {
            System.out.println("  [Warning] Could not read habits file: " + e.getMessage());
        }

        return habits;
    }
}
