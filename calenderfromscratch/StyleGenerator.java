public class StyleGenerator {

    public static String getCalendarCSS(String backgroundColor, String titleFontColor,
                                        String tableHeaderColor, String headerFontColor,
                                        String tableBackgroundColor, String currentDayColor) {

        return """
        body {
            font-family: Arial, sans-serif;
            background-color: %s;
            margin: 0;
            padding: 5px;
            display: flex;
            flex-direction: column;
            align-items: center;
        }

        h1 {
            margin-bottom: 20px;
            color: %s;
            font-size: 3em;
        }

        table {
            width: 800px;
            height: 600px;
            border-collapse: collapse;
            background-color: %s;
            table-layout: fixed;
        }

        th, td {
            width: 114px;
            height: 100px;
            border: 1.5px solid #280303ff;
            vertical-align: top;
            padding: 5px;
            font-size: 0.9em;
            position: relative;
            overflow: auto;
        }

        td::before {
            content: attr(data-day);
            position: absolute;
            top: 5px;
            left: 5px;
            font-weight: bold;
            font-size: 0.8em;
        }

        th {
            background-color: %s;
            color: %s;
            height: 30px;
            padding: 5px;
        }

        td {
            background-color: %s;
        }

        .today {
            background-color: %s;
            font-weight: bold;
        }
        """.formatted(backgroundColor, titleFontColor, backgroundColor,
                        tableHeaderColor, headerFontColor, tableBackgroundColor, currentDayColor);
    }
}
