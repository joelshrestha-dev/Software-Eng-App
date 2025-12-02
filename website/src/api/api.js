// FASTAPI AI Backend
export async function extractAssignments(file, instructions) {
  const formData = new FormData();
  formData.append("file", file);
  formData.append("instructions", instructions);   // NEW

  const res = await fetch("http://localhost:8000/extract-plan", {
    method: "POST",
    body: formData,
  });

  return res.json();   // { assignments: [...] }
}
