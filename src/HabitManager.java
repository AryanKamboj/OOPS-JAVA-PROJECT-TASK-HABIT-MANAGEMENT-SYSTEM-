import java.util.ArrayList;
import java.util.List;

public class HabitManager {

    private List<Habit> habits;
    private int nextId;

    public HabitManager() {
        habits = new ArrayList<>();
        nextId = 1;
    }

    public void recalculateNextId() {
        nextId = 1;
        for (Habit h : habits) {
            if (h.getId() >= nextId) {
                nextId = h.getId() + 1;
            }
        }
    }

    public List<Habit> getAllHabits() {
        return habits;
    }

    public String addHabit(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "Error: Habit name cannot be blank.";
        }
        name = name.trim();

        for (Habit h : habits) {
            if (h.getName().equalsIgnoreCase(name)) {
                return "Error: A habit named \"" + name + "\" already exists.";
            }
        }

        habits.add(new Habit(nextId, name));
        nextId++;
        return null;
    }

    public String recordCompletion(int id) {
        Habit h = findById(id);
        if (h == null) {
            return "Error: No habit found with ID " + id + ".";
        }
        boolean added = h.recordToday();
        if (added) {
            return "Completion recorded for \"" + h.getName() + "\"! Keep it up!";
        } else {
            return "You have already recorded \"" + h.getName() + "\" for today.";
        }
    }

    public int totalCompletions() {
        int total = 0;
        for (Habit h : habits) {
            total += h.getTotalCompletions();
        }
        return total;
    }

    private Habit findById(int id) {
        for (Habit h : habits) {
            if (h.getId() == id) return h;
        }
        return null;
    }

    public void setHabits(List<Habit> loaded) {
        this.habits = loaded;
        recalculateNextId();
    }
}
