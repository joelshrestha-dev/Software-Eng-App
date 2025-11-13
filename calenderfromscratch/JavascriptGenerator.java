public class JavascriptGenerator {

    public static String getIt(String tableBackgroundColor) {
        return """
            window.onload = function() {
                // Select all table cells with a data-day attribute
                const cells = document.querySelectorAll("td[data-day]");

                cells.forEach(cell => {
                    cell.addEventListener("click", () => {
                        // Only add textarea if one doesn't already exist
                        if (!cell.querySelector("textarea")) {
                            const textarea = document.createElement("textarea");
                            textarea.rows = 3;
                            textarea.cols = 10;
                            textarea.placeholder = "Add note...";

                            // Custom styling
                            textarea.style.marginLeft = "20px";        // Indent from left
                            textarea.style.marginTop = "10px";         // Slight spacing from top
                            textarea.style.backgroundColor = "%s"; // Example background color
                            textarea.style.border = "1px solid %s";   // Optional border color
                            textarea.style.borderRadius = "4px";       // Rounded corners

                            cell.appendChild(textarea);
                            textarea.focus(); // Automatically focus
                        }
                    });
                });
            };
        """.formatted(tableBackgroundColor, tableBackgroundColor);
    }
}
