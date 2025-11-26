import { useState, useEffect } from "react";
import { uploadSyllabus } from "../api/api";
import { useNavigate } from "react-router-dom";

export default function UploadPage() {
  useEffect(() => {
    fetch("http://localhost:8080/api/hello")
      .then((res) => res.text())
      .then((data) => console.log(data));
  }, []);

  const [file, setFile] = useState(null);
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  async function handleSubmit(e) {
    e.preventDefault();
    if (!file) return;

    setLoading(true);
    const result = await uploadSyllabus(file);

    // expect { syllabusId: "123" }
    navigate(`/plan/${result.syllabusId}`);
  }

  return (
    <div style={{ padding: "40px" }}>
      <h1>Upload Syllabus</h1>

      <form onSubmit={handleSubmit}>
        <input 
          type="file" 
          accept=".pdf,.doc,.docx" 
          onChange={(e) => setFile(e.target.files[0])}
        />

        <button style={{ marginLeft: "10px" }} type="submit">
          {loading ? "Uploading..." : "Submit"}
        </button>
      </form>
    </div>
  );
}
