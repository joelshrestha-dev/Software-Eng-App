# AI for student planner 
# To run: 
# uvicorn chatbot:app --reload

from fastapi import FastAPI, Request, UploadFile, File, Form
from pydantic import BaseModel
from fastapi.middleware.cors import CORSMiddleware
from openai import OpenAI
from dotenv import load_dotenv
import os
import uuid
import pdfplumber

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
You are an AI student planner here to help students plan schedules, 
manage tasks, and provide motivational support.
Keep responses concise, clear, and encouraging.
"""

# Memory store per session (development only)
session_memory = {}

class MessageRequest(BaseModel):
    session_id: str
    message: str

def prompt(user_input, pdf=""):
    if pdf != "":
        system_input = f"""Here is my pdf in words: {pdf}. {user_input}"""
    else:
        system_input = user_input
        
    return system_input

@app.post("/chat")
async def chat(request: MessageRequest):
    session_id = request.session_id
    user_msg = prompt(request.message)

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

class PlanResponse(BaseModel):
    assignments: list

@app.post("/extract-plan")
async def extract_plan(
    file: UploadFile = File(...),
    instructions: str = Form("")
    ):
    # 1. Convert PDF to text
    with pdfplumber.open(file.file) as pdf:
        text = "\n".join(page.extract_text() or "" for page in pdf.pages)

    # 2. Prepare prompt
    messages = [
        {"role": "system", "content": "Extract assignments as JSON."},
        {"role": "user", "content": f"""
Here is a syllabus:

{text}

Here are the instructions about the syllabus: {instructions}

Return JSON ONLY in this shape:
{{
  "assignments": [
    {{ "title": "...", "dueDate": "YYYY-MM-DD", "estTime": number }}
  ]
}}
"""}
    ]

    # 3. Call AI backend
    response = client.chat.completions.create(
        model="gpt-4o-mini",
        messages=messages
    )

    json_output = response.choices[0].message.content.strip()

    # 4. Return parsed JSON
    import json
    if not json_output:
        return {"error": "OpenAI returned empty response."}

    try:
        parsed = json.loads(json_output)
    except json.JSONDecodeError as e:
        # log the output to debug
        print("Failed JSON decode. Raw output:", json_output)
        return {"error": f"Failed to parse JSON: {e}"}

    return parsed
