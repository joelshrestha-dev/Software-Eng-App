/* 
To run, type the following code into powershell:
cd:/.....

javac -cp "lib/json-20250517.jar" src/*.java

java -cp "lib/json-20250517.jar;src" Server
*/

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class Server {

    static String backgroundColor = "#26a798ff"; 
    static String tableHeaderColor = "#6d3460ff";
    static String tableBackgroundColor = "#b593aaff";
    static String titleFontColor = "#050505ff";
    static String headerFontColor = "white";
    static String currentDayColor = "#732670ff";

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new java.net.InetSocketAddress(8080), 0);

        server.createContext("/", new CalendarPageHandler());
        server.createContext("/calendar", new MonthlyCalendarHandler());
        server.createContext("/savenote", new SaveNoteHandler());

        server.createContext("/planner", new PlannerHandler());
        server.createContext("/saveplanneritem", new SavePlannerItemHandler());

        System.out.println("Server running at http://localhost:8080");
        server.start();
    }

    // CALENDAR HANDLERS
    static class CalendarPageHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            LocalDate today = LocalDate.now();

            // Load notes
            JSONObject notesJson = NotesStorage.loadNotes();
            Map<String, String> notesMap = new java.util.HashMap<>();
            for (String key : notesJson.keySet()) {
                notesMap.put(key, notesJson.getString(key));
            }
            CalendarGenerator.notesStore = notesMap;

            String calendarHtml = CalendarGenerator.generateCalendarHtml(today.getYear(), today.getMonthValue());

            // Planner link (for the current week/day)
            String plannerLink = "<a href='/planner?year=" + today.getYear() +
                    "&month=" + today.getMonthValue() +
                    "&day=" + today.getDayOfMonth() +
                    "' style='text-decoration:none;'>Open Weekly Planner</a>";

            String css = StyleGenerator.getCalendarCSS(
                    backgroundColor,
                    titleFontColor,
                    tableHeaderColor,
                    headerFontColor,
                    tableBackgroundColor,
                    currentDayColor
            );

            String script = JavascriptGenerator.getIt(tableBackgroundColor);

            String monthTitle = today.format(DateTimeFormatter.ofPattern("MMMM yyyy"));

            String response = """
                    <html>
                    <head>
                        <style>%s</style>
                    </head>
                    <body>
                        <h1>%s</h1>
                        <input type="hidden" id="currentYear" value="%d">
                        <input type="hidden" id="currentMonth" value="%d">
                        <div style="display: flex; align-items: center; gap: 410px;">
                            <button id="prevMonth">⬅</button>
                            <span id="monthLabel"></span>
                            <button id="nextMonth">➡</button>
                        </div>
                        <div style="margin-bottom:10px;">%s</div>
                        <div id="calendarContainer">%s</div>
                        <script>%s</script>
                    </body>
                    </html>
                    """.formatted(css, monthTitle, today.getYear(), today.getMonthValue(), plannerLink, calendarHtml, script);

            byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
    }

    static class MonthlyCalendarHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String query = exchange.getRequestURI().getQuery();
            int year = LocalDate.now().getYear();
            int month = LocalDate.now().getMonthValue();

            if (query != null) {
                for (String param : query.split("&")) {
                    String[] keyValue = param.split("=");
                    if (keyValue[0].equals("year")) year = Integer.parseInt(keyValue[1]);
                    if (keyValue[0].equals("month")) month = Integer.parseInt(keyValue[1]);
                }
            }

            JSONObject notesJson = NotesStorage.loadNotes();
            Map<String, String> notesMap = new java.util.HashMap<>();
            for (String key : notesJson.keySet()) {
                notesMap.put(key, notesJson.getString(key));
            }
            CalendarGenerator.notesStore = notesMap;

            String calendarHtml = CalendarGenerator.generateCalendarHtml(year, month);

            int day = 1; // default
                if (query != null) {
                    for (String param : query.split("&")) {
                        String[] keyValue = param.split("=");
                        if (keyValue[0].equals("year")) year = Integer.parseInt(keyValue[1]);
                        if (keyValue[0].equals("month")) month = Integer.parseInt(keyValue[1]);
                        if (keyValue[0].equals("day")) day = Integer.parseInt(keyValue[1]); // new
                    }
                }

            LocalDate displayDate = LocalDate.of(year, month, day);
            String plannerLink = "<a href='/planner?year=" + displayDate.getYear() +
                    "&month=" + displayDate.getMonthValue() +
                    "&day=" + displayDate.getDayOfMonth() +
                    "' style='text-decoration:none;'>Open Weekly Planner</a>";

            String css = StyleGenerator.getCalendarCSS(
                    backgroundColor,
                    titleFontColor,
                    tableHeaderColor,
                    headerFontColor,
                    tableBackgroundColor,
                    currentDayColor
            );

            String script = JavascriptGenerator.getIt(tableBackgroundColor);

            String monthTitle = displayDate.format(DateTimeFormatter.ofPattern("MMMM yyyy"));

            String response = """
                    <html>
                    <head>
                        <style>%s</style>
                    </head>
                    <body>
                        <h1>%s</h1>
                        <input type="hidden" id="currentYear" value="%d">
                        <input type="hidden" id="currentMonth" value="%d">
                        <div style="display: flex; align-items: center; gap: 410px;">
                            <button id="prevMonth">⬅</button>
                            <span id="monthLabel"></span>
                            <button id="nextMonth">➡</button>
                        </div>
                        <div style="margin-bottom:10px;">%s</div>
                        <div id="calendarContainer">%s</div>
                        <script>%s</script>
                    </body>
                    </html>
                    """.formatted(css, monthTitle, year, month, plannerLink, calendarHtml, script);

            byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
    }

    // SAVE NOTES
    static class SaveNoteHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange ex) throws IOException {
            if (!ex.getRequestMethod().equalsIgnoreCase("POST")) {
                ex.sendResponseHeaders(405, -1);
                return;
            }

            String body = new String(ex.getRequestBody().readAllBytes());
            JSONObject req = new JSONObject(body);

            int year = req.getInt("year");
            int month = req.getInt("month");
            int day = req.getInt("day");
            String note = req.getString("note");

            JSONObject notes = NotesStorage.loadNotes();
            String key = year + "-" + month + "-" + day;
            notes.put(key, note);
            NotesStorage.saveNotes(notes);

            byte[] res = "OK".getBytes(StandardCharsets.UTF_8);
            ex.sendResponseHeaders(200, res.length);
            ex.getResponseBody().write(res);
            ex.close();
        }
    }

    // PLANNER HANDLERS
    static class PlannerHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String query = exchange.getRequestURI().getQuery();
            LocalDate selectedDate = LocalDate.now();

            if (query != null) {
                for (String param : query.split("&")) {
                    String[] keyValue = param.split("=");
                    if (keyValue[0].equals("year")) selectedDate = selectedDate.withYear(Integer.parseInt(keyValue[1]));
                    if (keyValue[0].equals("month")) selectedDate = selectedDate.withMonth(Integer.parseInt(keyValue[1]));
                    if (keyValue[0].equals("day")) selectedDate = selectedDate.withDayOfMonth(Integer.parseInt(keyValue[1]));
                }
            }

            JSONObject plannerNotes = NotesStorage.loadPlannerNotes();
            JSONObject calendarNotes = NotesStorage.loadNotes();

            String plannerHtml = PlannerGenerator.generateWeeklyPlannerHtml(selectedDate, calendarNotes, plannerNotes);

            String css = StyleGenerator.getPlannerCSS();
            String script = JavascriptGenerator.getPlannerJS();

            String response = """
                    <html>
                    <head>
                        <style>%s</style>
                    </head>
                    <body>
                        <h1>Weekly Planner (%s)</h1>
                        <div id="plannerContainer">%s</div>
                        <script>%s</script>
                    </body>
                    </html>
                    """.formatted(css, selectedDate.toString(), plannerHtml, script);

            byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
    }

    static class SavePlannerItemHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange ex) throws IOException {
            if (!ex.getRequestMethod().equalsIgnoreCase("POST")) {
                ex.sendResponseHeaders(405, -1);
                return;
            }

            String body = new String(ex.getRequestBody().readAllBytes());
            JSONObject req = new JSONObject(body);

            String dateKey = req.getInt("year") + "-" + req.getInt("month") + "-" + req.getInt("day");
            String item = req.getString("item");

            JSONObject plannerNotes = NotesStorage.loadPlannerNotes();
            JSONArray dayItems = plannerNotes.optJSONArray(dateKey);
            if (dayItems == null) {
                dayItems = new JSONArray();
            }
            dayItems.put(item);
            plannerNotes.put(dateKey, dayItems);

            NotesStorage.savePlannerNotes(plannerNotes);

            byte[] res = "OK".getBytes(StandardCharsets.UTF_8);
            ex.sendResponseHeaders(200, res.length);
            ex.getResponseBody().write(res);
            ex.close();
        }
    }
}
