import { BrowserRouter, Routes, Route, Link } from "react-router-dom";
import UploadPage from "./pages/UploadPage";
import PlanPage from "./pages/PlanPage";
import CalendarPage from "./pages/CalendarPage";
import ChatbotPage from "./pages/ChatbotPage";

export default function App() {
  return (
    <BrowserRouter>
      <nav style={{ 
        padding: "10px",
        background: "#eee",
        display: "flex",
        gap: "20px"
      }}>
        <Link to="/">Upload</Link>
        <Link to="/calendar">Calendar</Link>
        <Link to="/chatbot">Chatbot</Link>
      </nav>

      <Routes>
        <Route path="/" element={<UploadPage />} />
        <Route path="/plan/:id" element={<PlanPage />} />
        <Route path="/calendar" element={<CalendarPage />} />
        <Route path="/chatbot" element={<ChatbotPage />} />
      </Routes>
    </BrowserRouter>
  );
}
