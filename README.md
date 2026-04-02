# Booktracker

Booktracker is a Java command-line application that uses SQLite and JDBC to manage users and reading habits.

## Project structure

- `src/Main.java` → Java source code
- `database/booktracker.db` → SQLite database
- `lib/sqlite-jdbc-3.51.3.0.jar` → SQLite JDBC driver

## Requirements

- Java JDK 17 or compatible
- SQLite JDBC driver included in `lib/`

## How to compile

From the project folder:

```bash
javac -cp ".:lib/sqlite-jdbc-3.51.3.0.jar" src/Main.java