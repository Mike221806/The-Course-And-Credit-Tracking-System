package com.university.data;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * SQLite implementation of AdvisorFeedbackDetails interface.
 * Demonstrates Dependency Inversion Principle (DIP) by implementing the interface.
 */
public class AdvisorFeedbackSqlDetails implements AdvisorFeedbackDetails {

    private final DatabaseConnection dbConnection;

    public AdvisorFeedbackSqlDetails() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    @Override
    public boolean addFeedback(int advisorId, int studentId, String note, String createdAt) {
        String sql = "INSERT INTO advisor_feedback (advisorId, studentId, note, createdAt) VALUES (?, ?, ?, ?)";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, advisorId);
            pstmt.setInt(2, studentId);
            pstmt.setString(3, note);
            pstmt.setString(4, createdAt);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding feedback: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<AdvisorFeedback> getFeedbackByStudentId(int studentId) {
        List<AdvisorFeedback> feedbackList = new ArrayList<>();
        String sql = "SELECT * FROM advisor_feedback WHERE studentId = ? ORDER BY createdAt DESC";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                feedbackList.add(mapResultSetToAdvisorFeedback(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting feedback by student ID: " + e.getMessage());
        }

        return feedbackList;
    }

    @Override
    public List<AdvisorFeedback> getFeedbackByAdvisorId(int advisorId) {
        List<AdvisorFeedback> feedbackList = new ArrayList<>();
        String sql = "SELECT * FROM advisor_feedback WHERE advisorId = ? ORDER BY createdAt DESC";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, advisorId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                feedbackList.add(mapResultSetToAdvisorFeedback(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting feedback by advisor ID: " + e.getMessage());
        }

        return feedbackList;
    }

    @Override
    public Optional<AdvisorFeedback> getFeedbackById(int feedbackId) {
        String sql = "SELECT * FROM advisor_feedback WHERE feedbackId = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, feedbackId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToAdvisorFeedback(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting feedback by ID: " + e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public boolean updateFeedback(AdvisorFeedback feedback) {
        String sql = "UPDATE advisor_feedback SET note = ?, createdAt = ? WHERE feedbackId = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, feedback.getNote());
            pstmt.setString(2, feedback.getCreatedAt());
            pstmt.setInt(3, feedback.getFeedbackId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating feedback: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteFeedback(int feedbackId) {
        String sql = "DELETE FROM advisor_feedback WHERE feedbackId = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, feedbackId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting feedback: " + e.getMessage());
            return false;
        }
    }

    /**
     * Maps a ResultSet to an AdvisorFeedback object.
     * @param rs the ResultSet to map
     * @return the mapped AdvisorFeedback object
     * @throws SQLException if a database access error occurs
     */
    private AdvisorFeedback mapResultSetToAdvisorFeedback(ResultSet rs) throws SQLException {
        AdvisorFeedback feedback = new AdvisorFeedback();
        feedback.setFeedbackId(rs.getInt("feedbackId"));
        feedback.setAdvisorId(rs.getInt("advisorId"));
        feedback.setStudentId(rs.getInt("studentId"));
        feedback.setNote(rs.getString("note"));
        feedback.setCreatedAt(rs.getString("createdAt"));
        return feedback;
    }
}