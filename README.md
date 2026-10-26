# Task & Habit Management System

**CSC2104 — Object Oriented Programming**
Manipal University Jaipur | Section T4

| Team Member | Registration No. |
|---|---|
| Aryan Kamboj | 2502060008 |
| Aaditya Kalme | 2502060005 |

---

## Project Overview

A menu-driven Java console application that helps students organise their academic tasks and personal daily habits. The application runs entirely locally with no internet connection or external dependencies.

## Problem Statement

Students often struggle to track their assignments, deadlines, and personal self-improvement habits in one place. This project provides a simple, text-based tool that covers both needs with proper data persistence across sessions.

## Objectives

- Allow users to create, view, complete, and delete tasks with priorities and due dates.
- Automatically identify overdue tasks.
- Allow users to define habits and record daily completions.
- Prevent recording the same habit twice on the same day.
- Provide a statistics dashboard showing task completion rate.
- Persist all data across application restarts using plain text files.

## Features

| # | Feature |
|---|---|
| 1 | Add a task with title, description, priority (HIGH/MEDIUM/LOW), and due date |
| 2 | View all tasks in a formatted table with status and overdue highlighting |
| 3 | Mark a task as completed |
| 4 | Delete a task |
| 5 | Add a named habit |
| 6 | View all habits with total completion count |
| 7 | Record today's habit completion (prevents duplicates) |
| 8 | View progress statistics (tasks and habits) |
| 9 | Save data to files |
| 0 | Exit (auto-saves before quitting) |

## Technologies Used

- **Language**: Java (Standard Edition)
- **Java Version**: Java 11+ (tested on Java 26)
- **Libraries**: Java standard library only (`java.time`, `java.io`, `java.util`)
- **Build**: `javac` command line — no Maven, Gradle, or external tools

## Java Version Requirements

- Minimum: Java 11 (uses `java.time.LocalDate`)
- Tested with: Java 26.0.2.1

To check your version:
```
java -version
```

## Project Folder Structure

```
TaskHabitManagementSystem/
├── src/
│   ├── Task.java          — Task data class
│   ├── Habit.java         — Habit data class
│   ├── TaskManager.java   — Task operations and statistics
│   ├── HabitManager.java  — Habit operations
│   ├── DataStorage.java   — File read/write
│   └── Main.java          — Entry point, menu, user interaction
├── data/
│   ├── tasks.txt          — Saved tasks (auto-created on first save)
│   └── habits.txt         — Saved habits (auto-created on first save)
├── docs/
│   ├── PROJECT_REPORT.md
│   ├── CLASS_DIAGRAM.md
│   ├── FLOWCHART.md
│   ├── VIVA_PREPARATION.md
│   ├── DEMO_SCRIPT.md
│   ├── PRESENTATION_CONTENT.md
│   └── TEST_CASES.md
├── README.md
├── .gitignore
└── RUN.bat
```

## Compilation and Execution

### Option A — Double-click (Windows)
Double-click `RUN.bat`. It compiles and launches the application automatically.

### Option B — Command prompt
```bat
cd C:\Users\aryan\Desktop\TaskHabitManagementSystem
javac -d . src\Task.java src\Habit.java src\TaskManager.java src\HabitManager.java src\DataStorage.java src\Main.java
java -cp . Main
```

## Data File Locations

| File | Location |
|---|---|
| Tasks | `data\tasks.txt` |
| Habits | `data\habits.txt` |

Both files use `|` (pipe) as a field delimiter. They are created automatically when you save for the first time. Do not edit them manually.

## Known Limitations

- The application is console-only — no graphical user interface.
- Tasks cannot be edited after creation; delete and re-add if needed.
- Date input must follow `YYYY-MM-DD` format (e.g. `2026-10-27`).
- Habit names must be unique (case-insensitive).
- Habit completion is tracked by calendar date; the system uses the machine clock.
- No reminder or notification system.
- Data files store one record per line and do not support `|` characters in field values.
