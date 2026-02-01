package com.university.data;

import com.university.roles.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * SQLite implementation of StudentDetails interface.
 * Demonstrates concrete implementation of persistence layer following the DIP principle.
 * Provides CRUD operations for student data using SQLite database.
 */
public class StudentSqlDetails implements StudentDetails {

    private final DatabaseConnection dbConnection;

    public StudentSqlDetails() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    @Override
    public boolean createStudent(Student student) {
        String sql = "INSERT OR IGNORE INTO students (name, username, passwordHash, program, requiredCredits, completedCredits) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getUsername());
            pstmt.setString(3, student.getPasswordHash());
            pstmt.setString(4, student.getProgram());
            pstmt.setInt(5, student.getRequiredCredits());
            pstmt.setInt(6, student.getCompletedCredits());

            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            if (!e.getMessage().contains("UNIQUE constraint failed")) {
                System.err.println("Error creating student: " + e.getMessage());
            }
            return false;
        }
    }

    @Override
    public Optional<Student> getStudentById(int studentId) {
        String sql = "SELECT * FROM students WHERE studentId = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToStudent(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting student by ID: " + e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public Optional<Student> getStudentByUsername(String username) {
        String sql = "SELECT * FROM students WHERE username = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToStudent(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting student by username: " + e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY name";

        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                students.add(mapResultSetToStudent(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all students: " + e.getMessage());
        }

        return students;
    }

    @Override
    public boolean updateStudent(Student student) {
        String sql = "UPDATE students SET name = ?, username = ?, passwordHash = ?, program = ?, requiredCredits = ?, completedCredits = ? WHERE studentId = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getUsername());
            pstmt.setString(3, student.getPasswordHash());
            pstmt.setString(4, student.getProgram());
            pstmt.setInt(5, student.getRequiredCredits());
            pstmt.setInt(6, student.getCompletedCredits());
            pstmt.setInt(7, student.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating student: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteStudent(int studentId) {
        String sql = "DELETE FROM students WHERE studentId = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting student: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateCompletedCredits(int studentId, int completedCredits) {
        String sql = "UPDATE students SET completedCredits = ? WHERE studentId = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, completedCredits);
            pstmt.setInt(2, studentId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating completed credits: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updatePassword(int studentId, String passwordHash) {
        String sql = "UPDATE students SET passwordHash = ? WHERE studentId = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, passwordHash);
            pstmt.setInt(2, studentId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating password: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Optional<Student> validateStudentCredentials(String username, String passwordHash) {
        String sql = "SELECT * FROM students WHERE username = ? AND passwordHash = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, passwordHash);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToStudent(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error validating student credentials: " + e.getMessage());
        }

        return Optional.empty();
    }

    /**
     * Maps a ResultSet to a Student object.
     * @param rs the ResultSet to map
     * @return the mapped Student object
     * @throws SQLException if a database access error occurs
     */
    private Student mapResultSetToStudent(ResultSet rs) throws SQLException {
        Student student = new Student();
        student.setId(rs.getInt("studentId"));
        student.setName(rs.getString("name"));
        student.setUsername(rs.getString("username"));
        student.setPasswordHash(rs.getString("passwordHash"));
        student.setProgram(rs.getString("program"));
        student.setRequiredCredits(rs.getInt("requiredCredits"));
        student.setCompletedCredits(rs.getInt("completedCredits"));
        return student;
    }
}