import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Habit {

    private int id;
    private String name;
    private List<String> completionDates;

    public Habit(int id, String name) {
        this.id = id;
        this.name = name;
        this.completionDates = new ArrayList<>();
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getTotalCompletions() { return completionDates.size(); }

    public boolean recordToday() {
        String today = LocalDate.now().toString();
        if (completionDates.contains(today)) {
            return false;
        }
        completionDates.add(today);
        return true;
    }

    public String toFileString() {
        String datesJoined = String.join(",", completionDates);
        return id + "|" + name + "|" + datesJoined;
    }

    public static Habit fromFileString(String line) {
        String[] parts = line.split("\\|", -1);
        if (parts.length < 2) return null;

        int id = Integer.parseInt(parts[0].trim());
        String name = parts[1].trim();

        Habit h = new Habit(id, name);
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
