import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a habit the user wants to track.
 * Stores the list of dates on which the habit was completed.
 */
public class Habit {

    private int id;
    private String name;
    // Each entry is a date string (ISO format) representing one recorded completion
    private List<String> completionDates;

    public Habit(int id, String name) {
        this.id = id;
        this.name = name;
        this.completionDates = new ArrayList<>();
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public int getTotalCompletions() { return completionDates.size(); }

    /**
     * Records today's completion.
     * Returns true if the record was added, false if already done today.
     */
    public boolean recordToday() {
        String today = LocalDate.now().toString(); // e.g. "2026-10-27"
        if (completionDates.contains(today)) {
            return false; // already completed today
        }
        completionDates.add(today);
        return true;
    }

    // Saves the habit as a pipe-delimited line.
    // Dates are joined with commas inside the dates field.
    public String toFileString() {
        String datesJoined = String.join(",", completionDates);
        return id + "|" + name + "|" + datesJoined;
    }

    // Restores a Habit from a saved file line
    public static Habit fromFileString(String line) {
        String[] parts = line.split("\\|", -1);
        if (parts.length < 2) return null;

        int id = Integer.parseInt(parts[0].trim());
        String name = parts[1].trim();

        Habit h = new Habit(id, name);
        // parts[2] contains the comma-separated dates (may be empty)
        if (parts.length == 3 && !parts[2].trim().isEmpty()) {
            String[] dates = parts[2].trim().split(",");
            for (String d : dates) {
                if (!d.trim().isEmpty()) {
                    h.completionDates.add(d.trim());
                }
            }
        }
        return h;
    }

    @Override
    public String toString() {
        return String.format("[%3d] %-25s | Completions: %d", id, name, getTotalCompletions());
    }
}
