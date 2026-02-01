package com.university.data;

import com.university.courses.Enrollment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * SQLite implementation of EnrollmentDetails interface.
 * Demonstrates concrete implementation of persistence layer following the DIP principle.
 * Provides CRUD operations for enrollment data using SQLite database.
*/
public class EnrollmentSqlDetails implements EnrollmentDetails {

    private final DatabaseConnection dbConnection;

    public EnrollmentSqlDetails() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    @Override
    public boolean createEnrollment(Enrollment enrollment) {
        String sql = "INSERT OR IGNORE INTO enrollments (studentId, courseCode, semester, year, finalGrade) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, enrollment.getStudentId());
            pstmt.setString(2, enrollment.getCourseCode());
            pstmt.setString(3, enrollment.getSemester());
            pstmt.setInt(4, enrollment.getYear());
            pstmt.setString(5, enrollment.getFinalGrade());

            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            if (!e.getMessage().contains("UNIQUE constraint failed")) {
                System.err.println("Error creating enrollment: " + e.getMessage());
            }
            return false;
        }
    }

    @Override
    public Optional<Enrollment> getEnrollmentById(int enrollmentId) {
        String sql = "SELECT * FROM enrollments WHERE enrollmentId = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, enrollmentId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToEnrollment(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting enrollment by ID: " + e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public List<Enrollment> getEnrollmentsByStudentId(int studentId) {
        List<Enrollment> enrollments = new ArrayList<>();
        String sql = "SELECT * FROM enrollments WHERE studentId = ? ORDER BY year DESC, semester DESC";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                enrollments.add(mapResultSetToEnrollment(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting enrollments by student ID: " + e.getMessage());
        }

        return enrollments;
    }

    @Override
    public List<Enrollment> getEnrollmentsByCourseCode(String courseCode) {
        List<Enrollment> enrollments = new ArrayList<>();
        String sql = "SELECT * FROM enrollments WHERE courseCode = ? ORDER BY year DESC, semester DESC";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, courseCode);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                enrollments.add(mapResultSetToEnrollment(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting enrollments by course code: " + e.getMessage());
        }

        return enrollments;
    }

    @Override
    public boolean updateEnrollment(Enrollment enrollment) {
        String sql = "UPDATE enrollments SET studentId = ?, courseCode = ?, semester = ?, year = ?, finalGrade = ? WHERE enrollmentId = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, enrollment.getStudentId());
            pstmt.setString(2, enrollment.getCourseCode());
            pstmt.setString(3, enrollment.getSemester());
            pstmt.setInt(4, enrollment.getYear());
            pstmt.setString(5, enrollment.getFinalGrade());
            pstmt.setInt(6, enrollment.getEnrollmentId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating enrollment: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteEnrollment(int enrollmentId) {
        String sql = "DELETE FROM enrollments WHERE enrollmentId = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, enrollmentId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting enrollment: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateFinalGrade(int enrollmentId, String finalGrade) {
        String sql = "UPDATE enrollments SET finalGrade = ? WHERE enrollmentId = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, finalGrade);
            pstmt.setInt(2, enrollmentId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating final grade: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean isStudentEnrolled(int studentId, String courseCode) {
        String sql = "SELECT COUNT(*) FROM enrollments WHERE studentId = ? AND courseCode = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            pstmt.setString(2, courseCode);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking if student is enrolled: " + e.getMessage());
        }

        return false;
    }

    /**
     * Maps a ResultSet to an Enrollment object.
     * @param rs the ResultSet to map
     * @return the mapped Enrollment object
     * @throws SQLException if a database access error occurs
     */
    private Enrollment mapResultSetToEnrollment(ResultSet rs) throws SQLException {
        Enrollment enrollment = new Enrollment();
        enrollment.setEnrollmentId(rs.getInt("enrollmentId"));
        enrollment.setStudentId(rs.getInt("studentId"));
        enrollment.setCourseCode(rs.getString("courseCode"));
        enrollment.setSemester(rs.getString("semester"));
        enrollment.setYear(rs.getInt("year"));
        enrollment.setFinalGrade(rs.getString("finalGrade"));
        return enrollment;
    }
}