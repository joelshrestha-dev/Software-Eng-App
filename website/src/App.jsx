import { BrowserRouter, Routes, Route } from "react-router-dom";
import UploadPage from "./pages/UploadPage";
import PlanPage from "./pages/PlanPage";

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<UploadPage />} />
        <Route path="/plan/:id" element={<PlanPage />} />
      </Routes>
    </BrowserRouter>
  );
}
