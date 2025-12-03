import { useState } from "react";
import FullCalendar from "@fullcalendar/react";
import dayGridPlugin from "@fullcalendar/daygrid";
import interactionPlugin from "@fullcalendar/interaction"; // allows clicking

export default function CalendarPage() {
  const [events, setEvents] = useState([
    { title: "HW1 Due", date: "2025-02-15" },
    { title: "Project Proposal", date: "2025-02-22" },
    { title: "Ruthy's Birthday", date: "2025-12-22" }
  ]);

  const [showConfirm, setShowConfirm] = useState(false);

  function handleDateClick(info) {
    const title = prompt("Event title?");
    if (title) {
      setEvents([...events, { title, date: info.dateStr }]);
    }
  }

  // --- ICS export function ---
  function exportToICS(events) {
    function toICSDate(date) {
      return date.replace(/-/g, ""); // 2025-02-15 → 20250215
    }

    let ics = `BEGIN:VCALENDAR
VERSION:2.0
CALSCALE:GREGORIAN
METHOD:PUBLISH
`;

    events.forEach(event => {
      const start = toICSDate(event.date);
      const endDateObj = new Date(event.date);
      endDateObj.setDate(endDateObj.getDate() + 1);
      const end = toICSDate(endDateObj.toISOString().slice(0, 10));

      ics += `BEGIN:VEVENT
SUMMARY:${event.title}
DTSTART;VALUE=DATE:${start}
DTEND;VALUE=DATE:${end}
END:VEVENT
`;
    });

    ics += "END:VCALENDAR";

    const blob = new Blob([ics], { type: "text/calendar" });
    const url = URL.createObjectURL(blob);

    const link = document.createElement("a");
    link.href = url;
    link.download = "calendar_export.ics";
    link.click();

    URL.revokeObjectURL(url);
  }

  function handleDownloadClick() {
    setShowConfirm(true); // show confirmation popup
  }

  function confirmYes() {
    setShowConfirm(false);
    exportToICS(events);
  }

  function confirmNo() {
    setShowConfirm(false);
  }

  return (
    <div style={{ padding: "40px", position: "relative" }}>
      <h1>Calendar</h1>

      <div style={{ filter: showConfirm ? "blur(3px)" : "none" }}>
        <FullCalendar
          plugins={[dayGridPlugin, interactionPlugin]}
          initialView="dayGridMonth"
          events={events}
          dateClick={handleDateClick}
          height="80vh"
          headerToolbar={{
            left: "title",
            center: "",
            right: "download prev,next"
          }}
          customButtons={{
            download: {
              text: "Download Calendar",
              click: handleDownloadClick
            }
          }}
        />
      </div>

      {/* CONFIRMATION POPUP */}
      {showConfirm && (
        <div style={{
          position: "fixed",
          top: 0, left: 0, right: 0, bottom: 0,
          background: "rgba(0,0,0,0.5)",
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          zIndex: 9999 // ensures popup is on top
        }}>
          <div style={{
            background: "white",
            padding: "25px",
            borderRadius: "8px",
            width: "350px",
            textAlign: "center",
            zIndex: 10000
          }}>
            <h3>Download Calendar?</h3>
            <p>
              This will download all events in your calendar as an .ics file. 
              You can then import this file into Google Calendar or another calendar app.
            </p>
            <div style={{ marginTop: "20px", display: "flex", gap: "20px", justifyContent: "center" }}>
              <button onClick={confirmNo} style={{ padding: "8px 18px" }}>No</button>
              <button onClick={confirmYes} style={{ padding: "8px 18px", background: "#e66", color: "white", border: "none", borderRadius: "4px" }}>Yes</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
