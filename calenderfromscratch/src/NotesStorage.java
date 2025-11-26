import org.json.JSONObject;
import org.json.JSONArray;
import java.nio.file.*;
import java.io.IOException;

public class NotesStorage {

    private static final String FILE_PATH = "notes.json";
    private static final String PLANNER_FILE = "planner.json";

    // ---------- Calendar Notes ----------
    public static JSONObject loadNotes() {
        try {
            if (!Files.exists(Paths.get(FILE_PATH))) {
                return new JSONObject();
            }
            String content = Files.readString(Paths.get(FILE_PATH));
            return new JSONObject(content);
        } catch (Exception e) {
            return new JSONObject();
        }
    }

    public static void saveNotes(JSONObject obj) {
        try {
            Files.writeString(Paths.get(FILE_PATH), obj.toString(4));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ---------- Planner Notes ----------
    public static JSONObject loadPlannerNotes() {
        try {
            if (!Files.exists(Paths.get(PLANNER_FILE))) {
                return new JSONObject();
            }
            String content = Files.readString(Paths.get(PLANNER_FILE));
            return new JSONObject(content);
        } catch (Exception e) {
            return new JSONObject();
        }
    }

    public static void savePlannerNotes(JSONObject obj) {
        try {
            Files.writeString(Paths.get(PLANNER_FILE), obj.toString(4));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
