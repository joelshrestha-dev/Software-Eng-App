const BASE_URL = process.env.REACT_APP_API_URL ?? "http://localhost:8080/api";


// Upload the syllabus file
export async function uploadSyllabus(file) {
  const formData = new FormData();
  formData.append("file", file);

  const res = await fetch(`${BASE_URL}/upload`, {
    method: "POST",
    body: formData,
  });

  return res.json(); // expected { syllabusId: "123" }
}

// Fetch plan (dummy for now)
export async function getPlan(id) {
  // Return dummy JSON 
  return {
    assignments: [
      { title: "HW1", dueDate: "2025-02-15", estTime: 120 },
      { title: "Project Proposal", dueDate: "2025-02-22", estTime: 240 }
    ]
  };
}
