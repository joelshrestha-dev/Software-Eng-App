public class JavascriptGenerator {

    public static String getIt(String tableBackgroundColor) {
        return """
            window.onload = function() {

                // ============================================================
                // 1. CLICK A CELL → ADD TEXTAREA
                // ============================================================
                const cells = document.querySelectorAll("td[data-day]");

                cells.forEach(cell => {
                    cell.addEventListener("click", () => {
                        if (!cell.querySelector("textarea")) {
                            const textarea = document.createElement("textarea");
                            textarea.rows = 3;
                            textarea.cols = 10;
                            textarea.placeholder = "Add note...";

                            textarea.style.marginLeft = "20px";
                            textarea.style.marginTop = "10px";
                            textarea.style.backgroundColor = "%s";
                            textarea.style.border = "1px solid %s";
                            textarea.style.borderRadius = "4px";

                            cell.appendChild(textarea);
                            textarea.focus();
                        }
                    });
                });


                // ============================================================
                // 2. MONTH NAVIGATION BUTTONS
                // ============================================================

                // Read current year/month from hidden elements in your HTML
                let currentYear = parseInt(document.getElementById("currentYear").value);
                let currentMonth = parseInt(document.getElementById("currentMonth").value);

                // Previous month button
                document.getElementById("prevMonth").addEventListener("click", () => {
                    currentMonth--;
                    if (currentMonth < 1) {
                        currentMonth = 12;
                        currentYear--;
                    }

                    // Update the month label immediately
                    const monthLabel = document.getElementById("monthLabel");
                    monthLabel.textContent = new Date(currentYear, currentMonth - 1).toLocaleString('default', { month: 'long', year: 'numeric' });

                    // Navigate to your Java calendar page with updated params
                    window.location.href = "calendar?month=" + currentMonth + "&year=" + currentYear;
                });

                // Next month button
                document.getElementById("nextMonth").addEventListener("click", () => {
                    currentMonth++;
                    if (currentMonth > 12) {
                        currentMonth = 1;
                        currentYear++;
                    }

                    // Update the month label immediately
                    const monthLabel = document.getElementById("monthLabel");
                    monthLabel.textContent = new Date(currentYear, currentMonth - 1).toLocaleString('default', { month: 'long', year: 'numeric' });

                    // Navigate to your Java calendar page with updated params
                    window.location.href = "calendar?month=" + currentMonth + "&year=" + currentYear;
                });

            };
        """.formatted(tableBackgroundColor, tableBackgroundColor);
    }
}
