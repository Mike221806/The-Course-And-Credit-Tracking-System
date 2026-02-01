package com.university.data;

import com.university.courses.*;
import com.university.courses.Assignment;
import com.university.courses.FinalExam;
import com.university.courses.MidtermExam;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * SQLite implementation of CourseworkDetails interface.
 * Demonstrates concrete implementation of persistence layer following the DIP principle.
 * Provides CRUD operations for coursework data using SQLite database.
 */
public class CourseworkSqlDetails implements CourseworkDetails {

    private final DatabaseConnection dbConnection;

    public CourseworkSqlDetails() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    @Override
    public boolean createCourseworkItem(CourseworkItem courseworkItem) {
        String sql = "INSERT INTO coursework_items (courseCode, type, title, totalMarks, weight, dueDate) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, courseworkItem.getCourseCode());
            pstmt.setString(2, courseworkItem.getType());
            pstmt.setString(3, courseworkItem.getTitle());
            pstmt.setDouble(4, courseworkItem.getTotalMarks());
            pstmt.setDouble(5, courseworkItem.getWeight());
            pstmt.setString(6, courseworkItem.getDueDate().toString());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error creating coursework item: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Optional<CourseworkItem> getCourseworkItemById(int itemId) {
        String sql = "SELECT * FROM coursework_items WHERE itemId = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, itemId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToCourseworkItem(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting coursework item by ID: " + e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public List<CourseworkItem> getCourseworkItemsByCourse(String courseCode) {
        List<CourseworkItem> items = new ArrayList<>();
        String sql = "SELECT * FROM coursework_items WHERE courseCode = ? ORDER BY dueDate";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, courseCode);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                items.add(mapResultSetToCourseworkItem(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting coursework items by course: " + e.getMessage());
        }

        return items;
    }

    @Override
    public List<CourseworkItem> getCourseworkItemsByType(String type) {
        List<CourseworkItem> items = new ArrayList<>();
        String sql = "SELECT * FROM coursework_items WHERE type = ? ORDER BY dueDate";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, type);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                items.add(mapResultSetToCourseworkItem(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting coursework items by type: " + e.getMessage());
        }

        return items;
    }

    @Override
    public boolean updateCourseworkItem(CourseworkItem courseworkItem) {
        String sql = "UPDATE coursework_items SET courseCode = ?, type = ?, title = ?, totalMarks = ?, weight = ?, dueDate = ? WHERE itemId = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, courseworkItem.getCourseCode());
            pstmt.setString(2, courseworkItem.getType());
            pstmt.setString(3, courseworkItem.getTitle());
            pstmt.setDouble(4, courseworkItem.getTotalMarks());
            pstmt.setDouble(5, courseworkItem.getWeight());
            pstmt.setString(6, courseworkItem.getDueDate().toString());
            pstmt.setInt(7, courseworkItem.getItemId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating coursework item: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteCourseworkItem(int itemId) {
        String sql = "DELETE FROM coursework_items WHERE itemId = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, itemId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting coursework item: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean createCourseworkGrade(CourseworkGrade courseworkGrade) {
        String sql = "INSERT OR IGNORE INTO coursework_grades (studentId, courseCode, itemId, marksObtained) VALUES (?, ?, ?, ?)";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, courseworkGrade.getStudentId());
            pstmt.setString(2, courseworkGrade.getCourseCode());
            pstmt.setInt(3, courseworkGrade.getItemId());
            pstmt.setDouble(4, courseworkGrade.getMarksObtained());

            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            if (!e.getMessage().contains("UNIQUE constraint failed")) {
                System.err.println("Error creating coursework grade: " + e.getMessage());
            }
            return false;
        }
    }

    @Override
    public List<CourseworkGrade> getCourseworkGradesByStudent(int studentId) {
        List<CourseworkGrade> grades = new ArrayList<>();
        String sql = "SELECT * FROM coursework_grades WHERE studentId = ? ORDER BY courseCode, itemId";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                grades.add(mapResultSetToCourseworkGrade(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting coursework grades by student: " + e.getMessage());
        }

        return grades;
    }

    @Override
    public List<CourseworkGrade> getCourseworkGradesByStudentAndCourse(int studentId, String courseCode) {
        List<CourseworkGrade> grades = new ArrayList<>();
        String sql = "SELECT * FROM coursework_grades WHERE studentId = ? AND courseCode = ? ORDER BY itemId";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            pstmt.setString(2, courseCode);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                grades.add(mapResultSetToCourseworkGrade(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting coursework grades by student and course: " + e.getMessage());
        }

        return grades;
    }

    @Override
    public boolean updateCourseworkGrade(CourseworkGrade courseworkGrade) {
        String sql = "UPDATE coursework_grades SET studentId = ?, courseCode = ?, itemId = ?, marksObtained = ? WHERE gradeId = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, courseworkGrade.getStudentId());
            pstmt.setString(2, courseworkGrade.getCourseCode());
            pstmt.setInt(3, courseworkGrade.getItemId());
            pstmt.setDouble(4, courseworkGrade.getMarksObtained());
            pstmt.setInt(5, courseworkGrade.getGradeId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating coursework grade: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteCourseworkGrade(int gradeId) {
        String sql = "DELETE FROM coursework_grades WHERE gradeId = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, gradeId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting coursework grade: " + e.getMessage());
            return false;
        }
    }

    /**
     * Maps a ResultSet to a CourseworkItem object.
     * @param rs the ResultSet to map
     * @return the mapped CourseworkItem object
     * @throws SQLException if a database access error occurs
     */
    private CourseworkItem mapResultSetToCourseworkItem(ResultSet rs) throws SQLException {
        String type = rs.getString("type");
        CourseworkItem item;

        switch (type) {
            case "ASSIGNMENT":
                item = new Assignment();
                break;
            case "MIDTERM_EXAM":
                item = new MidtermExam();
                break;
            case "FINAL_EXAM":
                item = new FinalExam();
                break;
            default:
                item = new CourseworkItem() {
                    @Override
                    public String getType() {
                        return type;
                    }
                };
        }

        item.setItemId(rs.getInt("itemId"));
        item.setCourseCode(rs.getString("courseCode"));
        item.setTitle(rs.getString("title"));
        item.setTotalMarks(rs.getDouble("totalMarks"));
        item.setWeight(rs.getDouble("weight"));
        item.setDueDate(LocalDate.parse(rs.getString("dueDate")));

        return item;
    }

    /**
     * Maps a ResultSet to a CourseworkGrade object.
     * @param rs the ResultSet to map
     * @return the mapped CourseworkGrade object
     * @throws SQLException if a database access error occurs
     */
    private CourseworkGrade mapResultSetToCourseworkGrade(ResultSet rs) throws SQLException {
        CourseworkGrade grade = new CourseworkGrade();
        grade.setGradeId(rs.getInt("gradeId"));
        grade.setStudentId(rs.getInt("studentId"));
        grade.setCourseCode(rs.getString("courseCode"));
        grade.setItemId(rs.getInt("itemId"));
        grade.setMarksObtained(rs.getDouble("marksObtained"));
        return grade;
    }
}
