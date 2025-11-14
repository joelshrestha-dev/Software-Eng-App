import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Server {

    static String backgroundColor = "#26a798ff"; 
    static String tableHeaderColor = "#6d3460ff";
    static String tableBackgroundColor = "#b593aaff";
    static String titleFontColor = "#050505ff";
    static String headerFontColor = "white";
    static String currentDayColor = "#732670ff";

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new java.net.InetSocketAddress(8080), 0);

        // Initial page
        server.createContext("/", new CalendarPageHandler());
        // Endpoint for dynamically loading a specific month
        server.createContext("/calendar", new MonthlyCalendarHandler());

        System.out.println("Server running at http://localhost:8080");
        server.start();
    }

    static class CalendarPageHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            LocalDate today = LocalDate.now();
            String calendarHtml = CalendarGenerator.generateCalendarHtml(today.getYear(), today.getMonthValue());

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
                        <div id="calendarContainer">%s</div>
                        <script>%s</script>
                    </body>
                    </html>
                    """.formatted(css, monthTitle, today.getYear(), today.getMonthValue(), calendarHtml, script);

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
            // Parse query params
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

            // Generate calendar table
            String calendarHtml = CalendarGenerator.generateCalendarHtml(year, month);

            // Generate CSS & JS
            String css = StyleGenerator.getCalendarCSS(
                    backgroundColor,
                    titleFontColor,
                    tableHeaderColor,
                    headerFontColor,
                    tableBackgroundColor,
                    currentDayColor
            );

            String script = JavascriptGenerator.getIt(tableBackgroundColor);

            String monthTitle = LocalDate.of(year, month, 1)
                    .format(DateTimeFormatter.ofPattern("MMMM yyyy"));

            // Build full HTML page
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
                        <div id="calendarContainer">%s</div>
                        <script>%s</script>
                    </body>
                    </html>
                    """.formatted(css, monthTitle, year, month, calendarHtml, script);

            byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
    }
}
