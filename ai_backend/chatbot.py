# AI for student planner 
# To run: 
# uvicorn chatbot:app --reload

from fastapi import FastAPI, Request
from pydantic import BaseModel
from fastapi.middleware.cors import CORSMiddleware
from openai import OpenAI
from dotenv import load_dotenv
import os
import uuid

load_dotenv()
client = OpenAI(api_key=os.getenv("OPENAI_API_KEY"))

app = FastAPI()

app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:3000"],
    allow_methods=["*"],
    allow_headers=["*"],
)

SYSTEM_ROLE = """
You are an AI student planner here to help students plan schedules, manage tasks, and provide motivational support.
Keep responses concise, clear, and encouraging.
"""

# Memory store per session (development only)
session_memory = {}

class MessageRequest(BaseModel):
    session_id: str
    message: str

@app.post("/chat")
async def chat(request: MessageRequest):
    session_id = request.session_id
    user_msg = request.message

    if session_id not in session_memory:
        session_memory[session_id] = [{"role": "system", "content": SYSTEM_ROLE}]
    
    # Append user message
    session_memory[session_id].append({"role": "user", "content": user_msg})

    # Call OpenAI API
    response = client.chat.completions.create(
        model="gpt-3.5-turbo",
        messages=session_memory[session_id]
    )
    ai_msg = response.choices[0].message.content.strip()

    # Append AI response to memory
    session_memory[session_id].append({"role": "assistant", "content": ai_msg})

    return {"response": ai_msg}
