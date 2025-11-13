import java.time.LocalDate;
import java.time.YearMonth;

public class CalendarGenerator {

    public static String generateCalendarHtml(int year, int month) {
        YearMonth ym = YearMonth.of(year, month);
        LocalDate firstDay = ym.atDay(1);
        int lengthOfMonth = ym.lengthOfMonth();
        int startDayOfWeek = firstDay.getDayOfWeek().getValue(); // Monday=1

        StringBuilder html = new StringBuilder();

        html.append("""
            <table>
                <tr>
                    <th>Mon</th><th>Tue</th><th>Wed</th><th>Thu</th>
                    <th>Fri</th><th>Sat</th><th>Sun</th>
                </tr>
            """);

        html.append("<tr>");

        // Empty cells before the first day of the month
        for (int i = 1; i < startDayOfWeek; i++) {
            html.append("<td></td>");
        }

        LocalDate today = LocalDate.now();
        int dayOfWeekCounter = startDayOfWeek;

        for (int day = 1; day <= lengthOfMonth; day++) {
            boolean isToday = (today.getYear() == year &&
                                today.getMonthValue() == month &&
                                today.getDayOfMonth() == day);

            html.append("<td")
                .append(isToday ? " class='today'" : "")
                .append(">")
                .append(day)
                .append("</td>");

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
