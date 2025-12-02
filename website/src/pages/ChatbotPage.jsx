import { useState, useRef, useEffect } from "react";
import { v4 as uuidv4 } from "uuid";
import * as pdfjsLib from "pdfjs-dist/webpack"; // use webpack build

export default function ChatbotPage() {
  const [messages, setMessages] = useState([
    { sender: "bot", text: "Hi! I'm your AI assistant. How can I help?" }
  ]);
  const [input, setInput] = useState("");
  const [pdfName, setPdfName] = useState("");       // Store uploaded PDF name
  const [pdfText, setPdfText] = useState("");       // Store extracted PDF text
  const chatEndRef = useRef(null);
  const [sessionId] = useState(uuidv4());

  useEffect(() => {
    chatEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  async function handleSend(messageText = input) {
    if (!messageText.trim() && !pdfText) return;

    const combinedPrompt = pdfText ? pdfText + "\n" + messageText : messageText;

    const userMessage = { sender: "user", text: messageText };
    setMessages(prev => [...prev, userMessage]);
    setInput("");

    try {
      const response = await fetch("http://localhost:8000/chat", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ session_id: sessionId, message: combinedPrompt })
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

  async function handlePDFUpload(e) {
    const file = e.target.files[0];
    if (!file) return;
    if (file.type !== "application/pdf") {
      alert("Please upload a PDF file.");
      return;
    }

    setPdfName(file.name); // show filename next to button

    const arrayBuffer = await file.arrayBuffer();
    const pdf = await pdfjsLib.getDocument({ data: arrayBuffer }).promise;
    let fullText = "";

    for (let i = 1; i <= pdf.numPages; i++) {
      const page = await pdf.getPage(i);
      const textContent = await page.getTextContent();
      const pageText = textContent.items.map(item => item.str).join(" ");
      fullText += pageText + "\n";
    }

    setPdfText(fullText); // store PDF text for prompt
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

      {/* Input row */}
      <div style={{ display: "flex", gap: "10px" }}>
        <input
          style={{ flexGrow: 1, padding: "10px", borderRadius: "6px", border: "1px solid #ccc" }}
          value={input}
          onChange={e => setInput(e.target.value)}
          onKeyPress={handleKeyPress}
          placeholder="Type a message..."
        />
        <button onClick={() => handleSend()} style={{ padding: "10px 16px", borderRadius: "6px", border: "none", background: "#4a90e2", color: "white", cursor: "pointer" }}>
          Send
        </button>
      </div>

      {/* Upload PDF button below input */}
      <div style={{ marginTop: "10px", display: "flex", alignItems: "center", gap: "10px" }}>
        <label
          style={{
            padding: "8px 16px",
            borderRadius: "6px",
            border: "1px solid #ccc",
            background: "#f0f0f0",
            cursor: "pointer",
            display: "inline-block"
          }}
        >
          Upload PDF
          <input
            type="file"
            accept="application/pdf"
            onChange={handlePDFUpload}
            style={{ display: "none" }}
          />
        </label>
        {pdfName && <span>{pdfName}</span>}
      </div>
    </div>
  );
}
