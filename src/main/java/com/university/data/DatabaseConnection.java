package com.university.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.File;

/**
 * Database connection utility class.
 * Demonstrates Singleton pattern for managing database connections.
 * Provides centralized database configuration and connection management.
 */
public class DatabaseConnection {
    private static String DB_URL;
    private static Connection connection;
    private static final DatabaseConnection instance = new DatabaseConnection();

    /**
     * Private constructor to prevent instantiation.
     */
    private DatabaseConnection() {
        initializeDatabasePath();
    }

    /**
     * Initializes the database path to be in the same directory as the JAR file.
     */
    private void initializeDatabasePath() {
        try {
            // Check if we're running from a JAR file
            boolean isRunningFromJar = DatabaseConnection.class.getResource("DatabaseConnection.class").toString().startsWith("jar:");

            if (isRunningFromJar) {
                // Running from JAR - create database in same directory as JAR
                String jarPath = DatabaseConnection.class.getProtectionDomain()
                        .getCodeSource().getLocation().toURI().getPath();

                File jarDir = new File(jarPath).getParentFile();
                if (jarDir == null) {
                    jarDir = new File(".");
                }

                File dbFile = new File(jarDir, "university.db");
                DB_URL = "jdbc:sqlite:" + dbFile.getAbsolutePath();
                System.out.println("JAR detected - Database path: " + DB_URL);
            } else {
                // Running from IDE - use project directory
                File dbFile = new File("university.db");
                DB_URL = "jdbc:sqlite:" + dbFile.getAbsolutePath();
                System.out.println("IDE detected - Database path: " + DB_URL);
            }

        } catch (Exception e) {
            System.err.println("Error determining database path: " + e.getMessage());
            // Fallback to current directory
            DB_URL = "jdbc:sqlite:university.db";
        }
    }

    /**
     * Gets the singleton instance of DatabaseConnection.
     * @return the singleton instance
     */
    public static DatabaseConnection getInstance() {
        return instance;
    }

    /**
     * Gets a database connection.
     * @return the database connection
     * @throws SQLException if a database access error occurs
     */
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL);
        }
        return connection;
    }

    /**
     * Closes the database connection.
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }

    /**
     * Initializes the database by creating all necessary tables.
     * @return true if initialization was successful, false otherwise
     */
    public boolean initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // Enable foreign key support
            stmt.execute("PRAGMA foreign_keys = ON");

            // Create advisors table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS advisors (
                    advisorId INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    username TEXT UNIQUE NOT NULL,
                    passwordHash TEXT NOT NULL,
                    department TEXT NOT NULL,
                    email TEXT
                )
            """);

            // Create students table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS students (
                    studentId INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    username TEXT UNIQUE NOT NULL,
                    passwordHash TEXT NOT NULL,
                    program TEXT NOT NULL,
                    requiredCredits INTEGER NOT NULL DEFAULT 120,
                    completedCredits INTEGER NOT NULL DEFAULT 0
                )
            """);

            // Create courses table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS courses (
                    courseCode TEXT PRIMARY KEY,
                    title TEXT NOT NULL,
                    creditHours INTEGER NOT NULL
                )
            """);

            // Create enrollments table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS enrollments (
                    enrollmentId INTEGER PRIMARY KEY AUTOINCREMENT,
                    studentId INTEGER NOT NULL,
                    courseCode TEXT NOT NULL,
                    semester TEXT NOT NULL,
                    year INTEGER NOT NULL,
                    finalGrade TEXT,
                    FOREIGN KEY (studentId) REFERENCES students(studentId),
                    FOREIGN KEY (courseCode) REFERENCES courses(courseCode),
                    UNIQUE(studentId, courseCode, semester, year)
                )
            """);

            // Create coursework_items table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS coursework_items (
                    itemId INTEGER PRIMARY KEY AUTOINCREMENT,
                    courseCode TEXT NOT NULL,
                    type TEXT NOT NULL,
                    title TEXT NOT NULL,
                    totalMarks REAL NOT NULL,
                    weight REAL NOT NULL,
                    dueDate TEXT NOT NULL,
                    FOREIGN KEY (courseCode) REFERENCES courses(courseCode)
                )
            """);

            // Create coursework_grades table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS coursework_grades (
                    gradeId INTEGER PRIMARY KEY AUTOINCREMENT,
                    studentId INTEGER NOT NULL,
                    courseCode TEXT NOT NULL,
                    itemId INTEGER NOT NULL,
                    marksObtained REAL NOT NULL,
                    FOREIGN KEY (studentId) REFERENCES students(studentId),
                    FOREIGN KEY (courseCode) REFERENCES courses(courseCode),
                    FOREIGN KEY (itemId) REFERENCES coursework_items(itemId),
                    UNIQUE(studentId, itemId)
                )
            """);

            // Create advisor_feedback table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS advisor_feedback (
                    feedbackId INTEGER PRIMARY KEY AUTOINCREMENT,
                    advisorId INTEGER NOT NULL,
                    studentId INTEGER NOT NULL,
                    note TEXT NOT NULL,
                    createdAt TEXT NOT NULL,
                    FOREIGN KEY (advisorId) REFERENCES advisors(advisorId),
                    FOREIGN KEY (studentId) REFERENCES students(studentId)
                )
            """);

            return true;
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            return false;
        }
    }
}