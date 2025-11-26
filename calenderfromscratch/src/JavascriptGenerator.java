public class JavascriptGenerator {

    // ============================================================
    // Calendar notes JS
    // ============================================================
    public static String getIt(String tableBackgroundColor) {
        return """
            window.onload = function() {

                const cells = document.querySelectorAll("td[data-day]");

                cells.forEach(cell => {
                    // Check if the cell already has a saved note
                    let existingNoteDiv = cell.querySelector('.savedNote');
                    let noteText = existingNoteDiv ? existingNoteDiv.textContent : '';

                    // Only add one textarea per cell
                    if (!cell.querySelector('textarea')) {
                        const textarea = document.createElement('textarea');
                        textarea.rows = 3;
                        textarea.cols = 10;
                        textarea.placeholder = "Add note...";
                        textarea.value = noteText; // Pre-fill with saved note

                        // Styling
                        textarea.style.marginLeft = "20px";
                        textarea.style.marginTop = "10px";
                        textarea.style.backgroundColor = "%s";
                        textarea.style.border = "1px solid %s";
                        textarea.style.borderRadius = "4px";

                        cell.appendChild(textarea);

                        // Auto-save note when it changes
                        textarea.addEventListener("change", () => {
                            const note = textarea.value;
                            const day = cell.getAttribute("data-day");
                            const year = document.getElementById("currentYear").value;
                            const month = document.getElementById("currentMonth").value;

                            fetch("/savenote", {
                                method: "POST",
                                headers: { "Content-Type": "application/json" },
                                body: JSON.stringify({
                                    year: parseInt(year),
                                    month: parseInt(month),
                                    day: parseInt(day),
                                    note: note
                                })
                            }).catch(err => console.error("Error saving note:", err));
                        });
                    }
                });

                // Month navigation buttons
                let currentYear = parseInt(document.getElementById("currentYear").value);
                let currentMonth = parseInt(document.getElementById("currentMonth").value);

                document.getElementById("prevMonth").addEventListener("click", () => {
                    currentMonth--;
                    if (currentMonth < 1) { currentMonth = 12; currentYear--; }

                    document.getElementById("monthLabel").textContent =
                        new Date(currentYear, currentMonth - 1)
                        .toLocaleString('default', { month: 'long', year: 'numeric' });

                    window.location.href = "calendar?month=" + currentMonth + "&year=" + currentYear;
                });

                document.getElementById("nextMonth").addEventListener("click", () => {
                    currentMonth++;
                    if (currentMonth > 12) { currentMonth = 1; currentYear++; }

                    document.getElementById("monthLabel").textContent =
                        new Date(currentYear, currentMonth - 1)
                        .toLocaleString('default', { month: 'long', year: 'numeric' });

                    window.location.href = "calendar?month=" + currentMonth + "&year=" + currentYear;
                });

            };
        """.formatted(tableBackgroundColor, tableBackgroundColor);
    }

    // ============================================================
    // Planner JS stays the same
    // ============================================================
    public static String getPlannerJS() {
        return """
        (function() {
            const areas = document.querySelectorAll('.plannerInput');

            areas.forEach(area => {
                const dateKey = area.getAttribute('data-date');
                if (!dateKey) return;

                area.addEventListener('change', () => {
                    const parts = dateKey.split('-').map(s => parseInt(s, 10));
                    const year = parts[0];
                    const month = parts[1];
                    const day = parts[2];
                    const item = area.value;

                    const payload = { year: year, month: month, day: day, item: item };

                    fetch('/saveplanneritem', {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify(payload)
                    })
                    .then(res => {
                        if (!res.ok) throw new Error('Save failed: ' + res.status);
                        console.log('Planner saved for', dateKey);
                    })
                    .catch(err => console.error('Error saving planner item:', err));
                });
            });
        })();
        """;
    }

}
