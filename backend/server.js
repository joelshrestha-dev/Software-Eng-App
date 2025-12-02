const express = require("express");
const cors = require("cors");
const mysql = require("mysql2");

const app = express();
app.use(cors());
app.use(express.json());

const db = mysql.createConnection({
    host: "localhost",
    user: "root",
    password: "197355",
    database: "aquarium_db"
});

db.connect(err => {
    if (err) throw err;
    console.log("MySQL connected");
});

app.get("/", (req, res) => {
    res.send("Backend running!");
});

app.listen(3001, () => console.log("Server running on http://localhost:3001"));
