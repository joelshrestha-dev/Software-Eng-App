import { useState } from "react";
import { extractAssignments } from "../api/api";

export default function UploadPage() {
  const [file, setFile] = useState(null);
  const [instructions, setInstructions] = useState("");
  const [assignments, setAssignments] = useState([]);
  const [loading, setLoading] = useState(false);

  async function handleUpload() {
    if (!file) return alert("Please upload a PDF first.");

    setLoading(true);
    setAssignments([]); // clear previous results

    try {
      const result = await extractAssignments(file, instructions);
      setAssignments(result.assignments || []);
    } catch (err) {
      console.error(err);
      alert("Error extracting assignments.");
    }

    setLoading(false);
  }

  return (
    <div style={{ display: "flex", gap: "50px", padding: "30px" }}>
      
      {/* LEFT SIDE: PDF UPLOAD */}
      <div style={{ flex: 1, maxWidth: "500px" }}>
        <h2 style={{ marginBottom: "15px" }}>Upload Syllabus PDF</h2>

        <div style={{ display: "flex", gap: "10px", alignItems: "center", marginTop: "10px" }}>
          <input 
            type="file" 
            accept="application/pdf"
            onChange={(e) => setFile(e.target.files[0])}
            style={{ height: "36px" }}
          />
          <button 
            onClick={handleUpload} 
            style={{ height: "36px" }}
          >
            Extract Assignments
          </button>
        </div>

        {loading && <p style={{ marginTop: "10px" }}>Extracting file...</p>}

        {/* Show assignments only when available */}
        {assignments.length > 0 && (
          <>
            <h3 style={{ marginTop: "20px" }}>Extracted Assignments</h3>
            <table style={{ width: "100%", borderCollapse: "collapse", marginTop: "20px" }}>
              <thead>
                <tr>
                  <th style={{ border: "1px solid #ccc", padding: "8px", textAlign: "left" }}>Title</th>
                  <th style={{ border: "1px solid #ccc", padding: "8px", textAlign: "left" }}>Due Date</th>
                  <th style={{ border: "1px solid #ccc", padding: "8px", textAlign: "left" }}>Estimated Time (min)</th>
                </tr>
              </thead>
              <tbody>
                {assignments.map((a, idx) => (
                  <tr key={idx}>
                    <td style={{ border: "1px solid #ccc", padding: "8px" }}>{a.title}</td>
                    <td style={{ border: "1px solid #ccc", padding: "8px" }}>{a.dueDate}</td>
                    <td style={{ border: "1px solid #ccc", padding: "8px" }}>{a.estTime}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </>
        )}
      </div>

      {/* RIGHT SIDE: INSTRUCTIONS */}
      <div style={{ flex: 1, maxWidth: "500px" }}>
        <h2 style={{ marginBottom: "15px" }}>Additional Instructions</h2>

        <label>
          Anything the scheduler should know about your PDF?
        </label>

        <textarea
          value={instructions}
          onChange={(e) => setInstructions(e.target.value)}
          placeholder="Example: I only care about assignments worth more than 5% of the grade..."
          style={{
            width: "100%",
            height: "200px",
            marginTop: "10px",
            padding: "10px",
            boxSizing: "border-box"
          }}
        />
      </div>
    </div>
  );
}
