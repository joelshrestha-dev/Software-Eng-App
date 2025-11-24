import fs from "fs";
import OpenAI from "openai";
const client = new OpenAI();

const file = await client.files.create({
    file: fs.createReadStream("Project 1 Description.pdf"),
    purpose: "user_data",
});

const response = await client.responses.create({
    model: "gpt-5",
    input: [
        {
            role: "user",
            content: [
                {
                    type: "input_file",
                    file_id: file.id,
                },
                {
                    type: "input_text",
                    text: "Return a json object that can be used for a planner app",
                },
            ],
        },
    ],
});

console.log(response.output_text);