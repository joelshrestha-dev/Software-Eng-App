import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;

public class CalendarGenerator {

    // Filled by Server before generating HTML
    public static Map<String, String> notesStore;

    public static String generateCalendarHtml(int year, int month) {
        YearMonth ym = YearMonth.of(year, month);
        LocalDate firstDay = ym.atDay(1);
        int lengthOfMonth = ym.lengthOfMonth();

        // Day-of-week: Java uses MONDAY = 1 … SUNDAY = 7
        int startDayOfWeek = firstDay.getDayOfWeek().getValue();

        StringBuilder html = new StringBuilder();
        html.append("""
            <table>
                <tr>
                    <th>Sun</th><th>Mon</th><th>Tue</th><th>Wed</th>
                    <th>Thu</th><th>Fri</th><th>Sat</th>
                </tr>
        """);

        html.append("<tr>");

        // Insert blanks before the first day
        int blanks = startDayOfWeek % 7; // Sunday = 0 blanks
        for (int i = 0; i < blanks; i++) {
            html.append("<td></td>");
        }

        for (int day = 1; day <= lengthOfMonth; day++) {
            LocalDate current = LocalDate.of(year, month, day);
            int dow = current.getDayOfWeek().getValue(); // MON=1 … SUN=7

            // Start a new row if Sunday (except for the first day)
            if (dow == 7 && day != 1) {
                html.append("</tr><tr>");
            }

            String key = year + "-" + month + "-" + day;
            String note = (notesStore != null ? notesStore.getOrDefault(key, "") : "");

            // Build cell with date label and note
            html.append("<td data-day='").append(day).append("'")
                .append(current.equals(LocalDate.now()) ? " class='today'" : "")
                .append(">");

            // ✅ Day number
            html.append("<div class='dateLabel'>").append(day).append("</div>");

            // ✅ Existing note
            if (!note.isBlank()) {
                html.append("<div class='savedNote'>")
                    .append(escape(note))
                    .append("</div>");
            }

            html.append("</td>");
        }

        html.append("</tr></table>");
        return html.toString();
    }

    private static String escape(String s) {
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
}
