import { useState } from "react";
import FullCalendar from "@fullcalendar/react";
import dayGridPlugin from "@fullcalendar/daygrid";
import interactionPlugin from "@fullcalendar/interaction"; // allows clicking

export default function CalendarPage() {
  const [events, setEvents] = useState([
    { title: "HW1 Due", date: "2025-02-15" },
    { title: "Project Proposal", date: "2025-02-22" }
  ]);

  function handleDateClick(info) {
    const title = prompt("Event title?");
    if (title) {
      setEvents([...events, { title, date: info.dateStr }]);
    }
  }

  return (
    <div style={{ padding: "40px" }}>
      <h1>Calendar</h1>

      <FullCalendar
        plugins={[dayGridPlugin, interactionPlugin]}
        initialView="dayGridMonth"
        events={events}
        dateClick={handleDateClick}
        height="80vh"
      />
    </div>
  );
}
