import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;

public class Server {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new java.net.InetSocketAddress(8080), 0);
        server.createContext("/", new CalendarPageHandler());
        System.out.println("Server running at http://localhost:8080");
        server.start();
    }

    static class CalendarPageHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            LocalDate today = LocalDate.now();
            String calendarHtml = CalendarGenerator.generateCalendarHtml(today.getYear(), today.getMonthValue());

            String response = """
                <html>
                <head>
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        background-color: #26a798ff;
                        margin: 0;
                        padding: 20px;
                        display: flex;
                        flex-direction: column; /* Stack title above calendar */
                        align-items: center; /* Center horizontally */
                    }

                    h1 {
                        margin-bottom: 20px;
                    }

                    table {
                        border-collapse: collapse;
                        background-color: white;
                        max-width: 120vw;
                        table-layout: fixed;
                        box-shadow: 0 0 10px rgba(0,0,0,0.2);
                    }

                    th, td {
                        border: 1px solid #333;
                        padding: 15px;
                        text-align: center;
                        font-size: 1.5em;
                    }

                    th {
                        background-color: #444;
                        color: white;
                    }

                    td {
                        background-color: #f2f2f2;
                    }

                    .today {
                        background-color: #87CEFA;
                        font-weight: bold;
                    }
                </style>
                </head>

                <body>
                    <h1>%s</h1>
                    %s
                </body>
                </html>
            """.formatted(today.getMonth(),calendarHtml);

            exchange.sendResponseHeaders(200, response.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }
}
