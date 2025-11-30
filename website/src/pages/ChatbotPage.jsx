import { useState, useRef, useEffect } from "react";
import { v4 as uuidv4 } from "uuid";
import * as pdfjsLib from "pdfjs-dist";
import pdfjsWorker from "pdfjs-dist/build/pdf.worker.js"; // local worker

// Use the local worker for PDF.js
pdfjsLib.GlobalWorkerOptions.workerSrc = pdfjsWorker;

export default function ChatbotPage() {
  const [messages, setMessages] = useState([
    { sender: "bot", text: "Hi! I'm your AI assistant. How can I help?" }
  ]);
  const [input, setInput] = useState("");
  const chatEndRef = useRef(null);
  const [sessionId] = useState(uuidv4());

  useEffect(() => {
    chatEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  async function handleSend() {
    if (!input.trim()) return;

    const userMessage = { sender: "user", text: input };
    setMessages(prev => [...prev, userMessage]);
    setInput("");

    try {
      const response = await fetch("http://localhost:8000/chat", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ session_id: sessionId, message: input })
      });

      const data = await response.json();
      const aiMessage = { sender: "bot", text: data.response };
      setMessages(prev => [...prev, aiMessage]);
    } catch (error) {
      console.error("Error:", error);
      setMessages(prev => [...prev, { sender: "bot", text: "Sorry, something went wrong!" }]);
    }
  }

  function handleKeyPress(e) {
    if (e.key === "Enter") handleSend();
  }

  // Handle PDF upload
  async function handlePDFUpload(e) {
    const file = e.target.files[0];
    if (!file) return;
    if (file.type !== "application/pdf") {
      alert("Please upload a PDF file.");
      return;
    }

    const arrayBuffer = await file.arrayBuffer();
    const pdf = await pdfjsLib.getDocument({ data: arrayBuffer }).promise;
    let fullText = "";

    for (let i = 1; i <= pdf.numPages; i++) {
      const page = await pdf.getPage(i);
      const textContent = await page.getTextContent();
      const pageText = textContent.items.map(item => item.str).join(" ");
      fullText += pageText + "\n";
    }

    const aiMessage = { sender: "bot", text: "PDF content uploaded. You can now ask me questions about it!" };
    const userMessage = { sender: "user", text: fullText };
    setMessages(prev => [...prev, userMessage, aiMessage]);
  }

  return (
    <div style={{ padding: "20px", maxWidth: "600px", margin: "0 auto", display: "flex", flexDirection: "column", height: "80vh" }}>
      <h1>AI Chatbot</h1>

      <div style={{
        flexGrow: 1,
        border: "1px solid #ccc",
        padding: "10px",
        overflowY: "auto",
        borderRadius: "8px",
        marginBottom: "10px",
        background: "#fafafa",
        display: "flex",
        flexDirection: "column"
      }}>
        {messages.map((m, index) => (
          <div key={index} style={{ margin: "8px 0", textAlign: m.sender === "user" ? "right" : "left" }}>
            <strong>{m.sender === "user" ? "You" : "AI"}:</strong> {m.text}
          </div>
        ))}
        <div ref={chatEndRef} />
      </div>

      <div style={{ display: "flex", gap: "10px" }}>
        <input
          style={{ flexGrow: 1, padding: "10px", borderRadius: "6px", border: "1px solid #ccc" }}
          value={input}
          onChange={e => setInput(e.target.value)}
          onKeyPress={handleKeyPress}
          placeholder="Type a message..."
        />
        <button onClick={handleSend} style={{ padding: "10px 16px", borderRadius: "6px", border: "none", background: "#4a90e2", color: "white", cursor: "pointer" }}>
          Send
        </button>
        <input
          type="file"
          accept="application/pdf"
          onChange={handlePDFUpload}
          style={{ padding: "10px 16px", borderRadius: "6px", border: "1px solid #ccc", cursor: "pointer" }}
        />
      </div>
    </div>
  );
}
