import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;

public class CalendarGenerator {

    // Inject your notes storage (a static Map or file loader)
    public static Map<String, String> notesStore;

    public static String generateCalendarHtml(int year, int month) {
        YearMonth ym = YearMonth.of(year, month);
        LocalDate firstDay = ym.atDay(1);
        int lengthOfMonth = ym.lengthOfMonth();
        int startDayOfWeek = firstDay.getDayOfWeek().getValue(); // Monday=1

        StringBuilder html = new StringBuilder();
        html.append("""
            <table>
                <tr>
                    <th>Sun</th><th>Mon</th><th>Tue</th><th>Wed</th><th>Thu</th>
                    <th>Fri</th><th>Sat</th>
                </tr>
            """);

        html.append("<tr>");

        // Empty cells before the first day
        for (int i = 1; i < startDayOfWeek; i++) {
            html.append("<td></td>");
        }

        LocalDate today = LocalDate.now();
        int dayOfWeekCounter = startDayOfWeek;

        for (int day = 1; day <= lengthOfMonth; day++) {
            boolean isToday = (today.getYear() == year &&
                               today.getMonthValue() == month &&
                               today.getDayOfMonth() == day);

            // 🔥 Build key for notes: "2025-11-20"
            String key = year + "-" + month + "-" + day;
            String savedNote = (notesStore != null ? notesStore.get(key) : null);

            html.append("<td data-day='").append(day).append("'")
                .append(isToday ? " class='today'" : "")
                .append(">");

            // 🔥 If a saved note exists, show it
            if (savedNote != null && !savedNote.isBlank()) {
                html.append("<div class='note'>")
                    .append(savedNote.replace("\n", "<br>")) // safe formatting
                    .append("</div>");
            }

            html.append("</td>");

            if (dayOfWeekCounter == 7) {
                html.append("</tr><tr>");
                dayOfWeekCounter = 1;
            } else {
                dayOfWeekCounter++;
            }
        }

        html.append("</tr></table>");
        return html.toString();
    }
}
