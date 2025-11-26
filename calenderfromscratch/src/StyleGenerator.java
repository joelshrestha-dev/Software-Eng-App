public class StyleGenerator {

    // ============================================================
    // 1. Calendar CSS
    // ============================================================
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

        .dateLabel {
            font-weight: bold;
            margin-bottom: 4px;
        }

        .note {
            margin-top: 8px;
            font-size: 0.9em;
            word-wrap: break-word;
        }
        """.formatted(backgroundColor, titleFontColor, backgroundColor,
                        tableHeaderColor, headerFontColor, tableBackgroundColor, currentDayColor);
    }

    // ============================================================
    // 2. Planner CSS
    // ============================================================
    public static String getPlannerCSS() {
        return """
        body {
            font-family: Arial, sans-serif;
            background-color: #f9f9f9;
            margin: 0;
            padding: 5px;
            display: flex;
            flex-direction: column;
            align-items: center;
        }

        h1 {
            margin-bottom: 20px;
            color: #050505ff;
            font-size: 2.5em;
        }

        table {
            width: 1000px;
            border-collapse: collapse;
            table-layout: fixed;
        }

        th, td {
            width: 140px;
            min-height: 150px;
            border: 1px solid #280303ff;
            vertical-align: top;
            padding: 5px;
            font-size: 0.9em;
            position: relative;
        }

        th {
            background-color: #6d3460ff;
            color: white;
            height: 30px;
            padding: 5px;
            text-align: center;
        }

        td {
            background-color: #b593aaff;
        }

        .plannerDate {
            font-weight: bold;
            margin-bottom: 4px;
        }

        .plannerItems {
            margin-top: 5px;
            padding-left: 16px;
        }

        textarea {
            width: 95%;
            margin-top: 8px;
            margin-bottom: 5px;
            padding: 3px;
            border: 1px solid #333;
            border-radius: 3px;
            resize: none;
        }
        """;
    }
}
