import org.json.JSONObject;
import org.json.JSONException;
import java.nio.file.*;
import java.io.IOException;

public class NotesStorage {

    private static final String FILE_PATH = "notes.json";

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
}
