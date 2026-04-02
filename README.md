# Booktracker

Booktracker is a Java command-line application built with **Java**, **SQLite**, and **JDBC**.  
It allows Booktracker to manage information about users and their reading habits.

The program works with a SQLite database and provides the functionalities required in the assignment.

---

## Repository contents

- `src/Main.java` → Java source code
- `database/booktracker.db` → SQLite database
- `lib/sqlite-jdbc-3.51.3.0.jar` → SQLite JDBC driver
- `README.md` → project documentation

---

## Requirements

To run the project you need:

- Java JDK 17 or compatible
- the SQLite JDBC driver included in the `lib` folder

---

## How to compile

Open a terminal inside the `Booktracker` folder and run:

```bash
javac -cp ".:lib/sqlite-jdbc-3.51.3.0.jar" src/Main.java
