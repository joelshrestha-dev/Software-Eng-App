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


//-----------------------------------------------------------
// 1.3 — Create User (POST /users)
//-----------------------------------------------------------
app.post("/users", (req, res) => {
    const { username, email, password_hash } = req.body;

    const sql = `
        INSERT INTO users (username, email, password_hash, created_at)
        VALUES (?, ?, ?, NOW())
    `;

    db.query(sql, [username, email, password_hash], (err, result) => {
        if (err) return res.status(500).json({ error: err.message });

        res.json({
            message: "User created successfully",
            user_id: result.insertId
        });
    });
});


//-----------------------------------------------------------
// 1.4 — Update User (PUT /users/:id)
//-----------------------------------------------------------
app.put("/users/:id", (req, res) => {
    const { id } = req.params;
    const { username, email, password_hash } = req.body;

    const sql = `
        UPDATE users
        SET username = ?, email = ?, password_hash = ?
        WHERE user_id = ?
    `;

    db.query(sql, [username, email, password_hash, id], (err, result) => {
        if (err) return res.status(500).json({ error: err.message });

        res.json({
            message: "User updated successfully"
        });
    });
});


//-----------------------------------------------------------
// 1.5 — Delete User (DELETE /users/:id)
//-----------------------------------------------------------
app.delete("/users/:id", (req, res) => {
    const { id } = req.params;

    const sql = "DELETE FROM users WHERE user_id = ?";

    db.query(sql, [id], (err, result) => {
        if (err) return res.status(500).json({ error: err.message });

        res.json({
            message: "User deleted successfully"
        });
    });
});


//-----------------------------------------------------------
// 2.9 — Create Calendar (POST /calendars)
//-----------------------------------------------------------
app.post("/calendars", (req, res) => {
    const { user_id, name, description } = req.body;

    const sql = `
        INSERT INTO calendars (user_id, name, description, created_at)
        VALUES (?, ?, ?, NOW())
    `;

    db.query(sql, [user_id, name, description], (err, result) => {
        if (err) return res.status(500).json({ error: err.message });

        res.json({
            message: "Calendar created successfully",
            calendar_id: result.insertId
        });
    });
});


//-----------------------------------------------------------
// 2.10 — Update Calendar (PUT /calendars/:id)
//-----------------------------------------------------------
app.put("/calendars/:id", (req, res) => {
    const { id } = req.params;
    const { name, description } = req.body;

    const sql = `
        UPDATE calendars
        SET name = ?, description = ?
        WHERE calendar_id = ?
    `;

    db.query(sql, [name, description, id], (err, result) => {
        if (err) return res.status(500).json({ error: err.message });

        res.json({
            message: "Calendar updated successfully"
        });
    });
});


//-----------------------------------------------------------
// 2.11 — Delete Calendar (DELETE /calendars/:id)
//-----------------------------------------------------------
app.delete("/calendars/:id", (req, res) => {
    const { id } = req.params;

    const sql = "DELETE FROM calendars WHERE calendar_id = ?";

    db.query(sql, [id], (err, result) => {
        if (err) return res.status(500).json({ error: err.message });

        res.json({
            message: "Calendar deleted successfully"
        });
    });
});


//-----------------------------------------------------------
// 3.1 — Get All Tasks (GET /tasks)
//-----------------------------------------------------------
app.get("/tasks", (req, res) => {
    const sql = "SELECT * FROM tasks";

    db.query(sql, (err, results) => {
        if (err) return res.status(500).json({ error: err.message });
        res.json(results);
    });
});


//-----------------------------------------------------------
// 3.4 — Get Tasks by Calendar ID (GET /tasks/calendar/:calendar_id)
//-----------------------------------------------------------
app.get("/tasks/calendar/:calendar_id", (req, res) => {
    const { calendar_id } = req.params;

    const sql = `
        SELECT * FROM tasks
        WHERE calendar_id = ?
    `;

    db.query(sql, [calendar_id], (err, results) => {
        if (err) return res.status(500).json({ error: err.message });
        res.json(results);
    });
});


//-----------------------------------------------------------
// 3.5 — Create Task (POST /tasks)
//-----------------------------------------------------------
app.post("/tasks", (req, res) => {
    const { calendar_id, title, description, due_datetime, status, priority } = req.body;

    const sql = `
        INSERT INTO tasks (calendar_id, title, description, due_datetime, status, priority, created_at, updated_at)
        VALUES (?, ?, ?, ?, ?, ?, NOW(), NOW())
    `;

    db.query(
        sql,
        [calendar_id, title, description, due_datetime, status, priority],
        (err, result) => {
            if (err) return res.status(500).json({ error: err.message });

            res.json({
                message: "Task created successfully",
                task_id: result.insertId
            });
        }
    );
});


//-----------------------------------------------------------
// 3.6 — Update Task (PUT /tasks/:id)
//-----------------------------------------------------------
app.put("/tasks/:id", (req, res) => {
    const { id } = req.params;
    const { title, description, due_datetime, status, priority } = req.body;

    const sql = `
        UPDATE tasks
        SET title = ?, description = ?, due_datetime = ?, status = ?, priority = ?, updated_at = NOW()
        WHERE task_id = ?
    `;

    db.query(
        sql,
        [title, description, due_datetime, status, priority, id],
        (err, result) => {
            if (err) return res.status(500).json({ error: err.message });

            res.json({
                message: "Task updated successfully"
            });
        }
    );
});


//-----------------------------------------------------------
// 4.1 — Get All Events (GET /events)
//-----------------------------------------------------------
app.get("/events", (req, res) => {
    const sql = "SELECT * FROM events";

    db.query(sql, (err, results) => {
        if (err) return res.status(500).json({ error: err.message });
        res.json(results);
    });
});


//-----------------------------------------------------------
// 4.4 — Get Events by Calendar ID (GET /events/calendar/:calendar_id)
//-----------------------------------------------------------
app.get("/events/calendar/:calendar_id", (req, res) => {
    const { calendar_id } = req.params;

    const sql = `
        SELECT * FROM events
        WHERE calendar_id = ?
    `;

    db.query(sql, [calendar_id], (err, results) => {
        if (err) return res.status(500).json({ error: err.message });
        res.json(results);
    });
});


//-----------------------------------------------------------
// 4.5 — Create Event (POST /events)
//-----------------------------------------------------------
app.post("/events", (req, res) => {
    const { calendar_id, title, description, start_datetime, end_datetime, location } = req.body;

    const sql = `
        INSERT INTO events (calendar_id, title, description, start_datetime, end_datetime, location, created_at, updated_at)
        VALUES (?, ?, ?, ?, ?, ?, NOW(), NOW())
    `;

    db.query(
        sql,
        [calendar_id, title, description, start_datetime, end_datetime, location],
        (err, result) => {
            if (err) return res.status(500).json({ error: err.message });

            res.json({
                message: "Event created successfully",
                event_id: result.insertId
            });
        }
    );
});


//-----------------------------------------------------------
// 4.6 — Update Event (PUT /events/:id)
//-----------------------------------------------------------
app.put("/events/:id", (req, res) => {
    const { id } = req.params;
    const { title, description, start_datetime, end_datetime, location } = req.body;

    const sql = `
        UPDATE events
        SET title = ?, description = ?, start_datetime = ?, end_datetime = ?, location = ?, updated_at = NOW()
        WHERE event_id = ?
    `;

    db.query(
        sql,
        [title, description, start_datetime, end_datetime, location, id],
        (err, result) => {
            if (err) return res.status(500).json({ error: err.message });

            res.json({
                message: "Event updated successfully"
            });
        }
    );
});


//-----------------------------------------------------------
// 5.1 — Get All Deadlines (GET /deadlines)
//-----------------------------------------------------------
app.get("/deadlines", (req, res) => {
    const sql = "SELECT * FROM deadlines";

    db.query(sql, (err, results) => {
        if (err) return res.status(500).json({ error: err.message });
        res.json(results);
    });
});


//-----------------------------------------------------------
// 5.4 — Get Deadlines by Calendar ID (GET /deadlines/calendar/:calendar_id)
//-----------------------------------------------------------
app.get("/deadlines/calendar/:calendar_id", (req, res) => {
    const { calendar_id } = req.params;

    const sql = `
        SELECT * FROM deadlines
        WHERE calendar_id = ?
    `;

    db.query(sql, [calendar_id], (err, results) => {
        if (err) return res.status(500).json({ error: err.message });
        res.json(results);
    });
});


//-----------------------------------------------------------
// 5.5 — Create Deadline (POST /deadlines)
//-----------------------------------------------------------
app.post("/deadlines", (req, res) => {
    const { calendar_id, title, description, due_datetime, priority } = req.body;

    const sql = `
        INSERT INTO deadlines (calendar_id, title, description, due_datetime, priority, created_at, updated_at)
        VALUES (?, ?, ?, ?, ?, NOW(), NOW())
    `;

    db.query(
        sql,
        [calendar_id, title, description, due_datetime, priority],
        (err, result) => {
            if (err) return res.status(500).json({ error: err.message });

            res.json({
                message: "Deadline created successfully",
                deadline_id: result.insertId
            });
        }
    );
});


//-----------------------------------------------------------
// 5.6 — Update Deadline (PUT /deadlines/:id)
//-----------------------------------------------------------
app.put("/deadlines/:id", (req, res) => {
    const { id } = req.params;
    const { title, description, due_datetime, priority } = req.body;

    const sql = `
        UPDATE deadlines
        SET title = ?, description = ?, due_datetime = ?, priority = ?, updated_at = NOW()
        WHERE deadline_id = ?
    `;

    db.query(
        sql,
        [title, description, due_datetime, priority, id],
        (err, result) => {
            if (err) return res.status(500).json({ error: err.message });

            res.json({
                message: "Deadline updated successfully"
            });
        }
    );
});


//-----------------------------------------------------------
// 6.1 — Get All Assignments (GET /assignments)
//-----------------------------------------------------------
app.get("/assignments", (req, res) => {
    const sql = "SELECT * FROM assignments";

    db.query(sql, (err, results) => {
        if (err) return res.status(500).json({ error: err.message });
        res.json(results);
    });
});


//-----------------------------------------------------------
// 6.5 — Create Assignment (POST /assignments)
//-----------------------------------------------------------
app.post("/assignments", (req, res) => {
    const { calendar_id, title, description, due_datetime, est_time, priority } = req.body;

    const sql = `
        INSERT INTO assignments (calendar_id, title, description, due_datetime, est_time, priority, created_at, updated_at)
        VALUES (?, ?, ?, ?, ?, ?, NOW(), NOW())
    `;

    db.query(
        sql,
        [calendar_id, title, description, due_datetime, est_time, priority],
        (err, result) => {
            if (err) return res.status(500).json({ error: err.message });

            res.json({
                message: "Assignment created successfully",
                assignment_id: result.insertId
            });
        }
    );
});


//-----------------------------------------------------------
// 6.6 — Create Multiple Assignments (POST /assignments/bulk)
//-----------------------------------------------------------
app.post("/assignments/bulk", (req, res) => {
    const { assignments } = req.body;

    if (!assignments || assignments.length === 0) {
        return res.status(400).json({ error: "No assignments provided" });
    }

    const values = assignments.map(a => [
        a.calendar_id,
        a.title,
        a.description || "",
        a.due_datetime,
        a.est_time || 0,
        a.priority || 1
    ]);

    const sql = `
        INSERT INTO assignments (calendar_id, title, description, due_datetime, est_time, priority, created_at, updated_at)
        VALUES ?
    `;

    db.query(sql, [values], (err, result) => {
        if (err) return res.status(500).json({ error: err.message });

        res.json({
            message: "Assignments inserted successfully",
            created_count: result.affectedRows
        });
    });
});


//-----------------------------------------------------------
// 6.7 — Update Assignment (PUT /assignments/:id)
//-----------------------------------------------------------
app.put("/assignments/:id", (req, res) => {
    const { id } = req.params;
    const { title, description, due_datetime, est_time, priority } = req.body;

    const sql = `
        UPDATE assignments
        SET title = ?, description = ?, due_datetime = ?, est_time = ?, priority = ?, updated_at = NOW()
        WHERE assignment_id = ?
    `;

    db.query(
        sql,
        [title, description, due_datetime, est_time, priority, id],
        (err, result) => {
            if (err) return res.status(500).json({ error: err.message });

            res.json({
                message: "Assignment updated successfully"
            });
        }
    );
});


//-----------------------------------------------------------
// 6.8 — Delete Assignment (DELETE /assignments/:id)
//-----------------------------------------------------------
app.delete("/assignments/:id", (req, res) => {
    const { id } = req.params;

    const sql = `
        DELETE FROM assignments
        WHERE assignment_id = ?
    `;

    db.query(sql, [id], (err, result) => {
        if (err) return res.status(500).json({ error: err.message });

        res.json({
            message: "Assignment deleted successfully"
        });
    });
});
