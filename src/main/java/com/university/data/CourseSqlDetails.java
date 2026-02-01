package com.university.data;

import com.university.courses.Course;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * SQLite implementation of CourseDetails interface.
 * Demonstrates concrete implementation of persistence layer following the DIP principle.
 * Provides CRUD operations for course data using SQLite database.
 */
public class CourseSqlDetails implements CourseDetails {

    private final DatabaseConnection dbConnection;

    public CourseSqlDetails() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    @Override
    public boolean createCourse(Course course) {
        String sql = "INSERT OR IGNORE INTO courses (courseCode, title, creditHours) VALUES (?, ?, ?)";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, course.getCourseCode());
            pstmt.setString(2, course.getTitle());
            pstmt.setInt(3, course.getCreditHours());

            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            if (!e.getMessage().contains("PRIMARY KEY constraint failed") && !e.getMessage().contains("UNIQUE constraint failed")) {
                System.err.println("Error creating course: " + e.getMessage());
            }
            return false;
        }
    }

    @Override
    public Optional<Course> getCourseByCode(String courseCode) {
        String sql = "SELECT * FROM courses WHERE courseCode = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, courseCode);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToCourse(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting course by code: " + e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM courses ORDER BY courseCode";

        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                courses.add(mapResultSetToCourse(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all courses: " + e.getMessage());
        }

        return courses;
    }

    @Override
    public boolean updateCourse(Course course) {
        String sql = "UPDATE courses SET title = ?, creditHours = ? WHERE courseCode = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, course.getTitle());
            pstmt.setInt(2, course.getCreditHours());
            pstmt.setString(3, course.getCourseCode());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating course: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteCourse(String courseCode) {
        String sql = "DELETE FROM courses WHERE courseCode = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, courseCode);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting course: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean courseExists(String courseCode) {
        String sql = "SELECT COUNT(*) FROM courses WHERE courseCode = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, courseCode);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking if course exists: " + e.getMessage());
        }

        return false;
    }

    /**
     * Maps a ResultSet to a Course object.
     * @param rs the ResultSet to map
     * @return the mapped Course object
     * @throws SQLException if a database access error occurs
     */
    private Course mapResultSetToCourse(ResultSet rs) throws SQLException {
        Course course = new Course();
        course.setCourseCode(rs.getString("courseCode"));
        course.setTitle(rs.getString("title"));
        course.setCreditHours(rs.getInt("creditHours"));
        return course;
    }
}