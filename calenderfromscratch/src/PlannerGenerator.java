import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import org.json.JSONObject;
import org.json.JSONArray;

public class PlannerGenerator {

    public static String generateWeeklyPlannerHtml(LocalDate date, JSONObject calendarNotes, JSONObject plannerNotes) {
        // Start of week = Sunday
        LocalDate startOfWeek = date;
        while (startOfWeek.getDayOfWeek() != DayOfWeek.SUNDAY) {
            startOfWeek = startOfWeek.minusDays(1);
        }

        StringBuilder html = new StringBuilder();

        html.append("<table id='weeklyPlanner'>");

        // ---- HEADER ROW ----
        html.append("<tr>");
        for (int i = 0; i < 7; i++) {
            LocalDate day = startOfWeek.plusDays(i);
            html.append("<th>")
                .append(day.getDayOfWeek())
                .append("<br>")
                .append(day.format(DateTimeFormatter.ofPattern("MM/dd")))
                .append("</th>");
        }
        html.append("</tr>");

        // ---- NOTES ROW ----
        html.append("<tr>");
        for (int i = 0; i < 7; i++) {

            LocalDate day = startOfWeek.plusDays(i);
            String key = day.getYear() + "-" + day.getMonthValue() + "-" + day.getDayOfMonth();

            html.append("<td data-day='").append(key).append("'>");

            // TOP DATE LABEL
            html.append("<div class='plannerDate'>")
                .append(day.getDayOfMonth())
                .append("</div>");

            // EXISTING ITEMS
            html.append("<ul class='plannerItems'>");

            JSONArray items = plannerNotes.optJSONArray(key);
            if (items != null) {
                for (int j = 0; j < items.length(); j++) {
                    html.append("<li>").append(items.getString(j)).append("</li>");
                }
            }

            // LAST CALENDAR NOTE
            String calNote = calendarNotes.optString(key, "");
            if (!calNote.isBlank()) {
                html.append("<li>").append(calNote).append("</li>");
            }

            html.append("</ul>");

            // TEXTAREA FOR NEW ITEM
            html.append("<textarea placeholder='Add item...'></textarea>");

            html.append("</td>");
        }
        html.append("</tr>");

        html.append("</table>");

        return html.toString();
    }
}
