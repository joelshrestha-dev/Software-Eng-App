import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;

public class Server {

    // Change colors: 
    static String backgroundColor = "#26a798ff"; 
    static String tableHeaderColor = "#6d3460ff";
    static String tableBackgroundColor = "#b593aaff";
    static String titleFontColor = "#050505ff";
    static String headerFontColor = "white";
    static String currentDayColor = "#732670ff";

    public static void main(String[] args) throws IOException {
        // Create HTTP server on port 8080
        HttpServer server = HttpServer.create(new java.net.InetSocketAddress(8080), 0);
        server.createContext("/", new CalendarPageHandler()); // Attach handler for root path
        System.out.println("Server running at http://localhost:8080");
        server.start(); // Start the server
    }

    static class CalendarPageHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            LocalDate today = LocalDate.now(); // Get current date
            String calendarHtml = CalendarGenerator.generateCalendarHtml(today.getYear(), today.getMonthValue());

            // HTML response with embedded CSS
            String response = """
                <html>
                <head>
                <style>
                    body {
                        font-family: Arial, sans-serif; /* Font for page */
                        background-color: %s; /* Page background &&&&&&&&&&&&&&& */
                        margin: 0;              /* Remove default margin */
                        padding: 5px;          /* Space around content */
                        display: flex;
                        flex-direction: column; /* Stack title above calendar */
                        align-items: center;    /* Center content horizontally */
                    }

                    h1 {
                        margin-bottom: 20px;    /* Space from title to table */
                        color: %s;         /* titleFontColor &&&&&&&&&&&&&&&&&&&*/
                        font-size: 3em;         /* Change the font size */
                    }

                    table {
                        width: 800px;           /* Fixed table width */
                        height: 600px;          /* Fixed table height */
                        border-collapse: collapse; /* Remove gaps between cells */
                        background-color: %s; /* Table background &&&&&&&&&&&&&&&&&&&& */
                        table-layout: fixed;    /* Equal column widths */
                        /* box-shadow: 0 0 20px rgba(0,0,0,0.2); Shadow effect */
                    }

                    th, td {
                        width: 114px;           /* 7 columns: 600 / 7 ≈ 114px */
                        height: 100px;          /* 6 rows: 600 / 6 = 100px */
                        border: 1.5px solid #280303ff; /* Border color */
                        vertical-align: top;    /* Content starts at top */
                        padding: 5px;           /* Inner spacing */
                        font-size: 0.9em;       /* Smaller numbers */
                        position: relative;     /* Needed for td::before positioning */
                        overflow: auto;         /* Scroll if content too large */
                    }

                    /* Display day number in top-left corner of cell */
                    td::before {
                        content: attr(data-day); /* Use data-day attribute from CalendarGenerator */
                        position: absolute;
                        top: 5px;
                        left: 5px;
                        font-weight: bold;
                        font-size: 0.8em;       /* Small corner number */
                    }

                    th {
                        background-color: %s; /* tableHeaderColor &&&&&&&&&&&&&&&&&&77 */
                        color: %s;                /* headerFontColor &&&&&&&&&&&&&&&&&&&& */
                        height: 30px;                /* Make header row shorter */
                        padding: 5px;                /* Reduce top/bottom padding */
                    }

                    td {
                        background-color: %s; /* tableBackgroundColor &&&&&&&&&&&&&&77 */
                    }

                    .today {
                        background-color: %s; /* currentDayColor &&&&&&&&&&&&&&&& */
                        font-weight: bold;           /* Bold text for today */
                    }
                </style>
                </head>

                <body>
                    <h1>%s</h1>  <!-- Display current month -->
                    %s           <!-- Insert calendar table HTML -->
                </body>
                </html>
            """.formatted(backgroundColor, titleFontColor, backgroundColor, tableHeaderColor, headerFontColor, tableBackgroundColor, currentDayColor, today.getMonth(), calendarHtml);

            // Send response headers and body
            exchange.sendResponseHeaders(200, response.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }
}
