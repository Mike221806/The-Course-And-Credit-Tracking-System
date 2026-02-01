package com.university.data;

import com.university.roles.Advisor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * SQLite implementation of AdvisorDetails interface.
 * Demonstrates concrete implementation of persistence layer following the DIP principle.
 * Provides CRUD operations for advisor data using SQLite database.
 */
public class AdvisorSqlDetails implements AdvisorDetails {

    private final DatabaseConnection dbConnection;

    public AdvisorSqlDetails() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    @Override
    public boolean createAdvisor(Advisor advisor) {
        String sql = "INSERT OR IGNORE INTO advisors (name, username, passwordHash, department, email) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, advisor.getName());
            pstmt.setString(2, advisor.getUsername());
            pstmt.setString(3, advisor.getPasswordHash());
            pstmt.setString(4, advisor.getDepartment());
            pstmt.setString(5, advisor.getEmail());

            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            if (!e.getMessage().contains("UNIQUE constraint failed")) {
                System.err.println("Error creating advisor: " + e.getMessage());
            }
            return false;
        }
    }

    @Override
    public Optional<Advisor> getAdvisorById(int advisorId) {
        String sql = "SELECT * FROM advisors WHERE advisorId = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, advisorId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToAdvisor(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting advisor by ID: " + e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public Optional<Advisor> getAdvisorByUsername(String username) {
        String sql = "SELECT * FROM advisors WHERE username = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToAdvisor(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting advisor by username: " + e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public List<Advisor> getAllAdvisors() {
        List<Advisor> advisors = new ArrayList<>();
        String sql = "SELECT * FROM advisors ORDER BY name";

        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                advisors.add(mapResultSetToAdvisor(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all advisors: " + e.getMessage());
        }

        return advisors;
    }

    @Override
    public boolean updateAdvisor(Advisor advisor) {
        String sql = "UPDATE advisors SET name = ?, username = ?, passwordHash = ?, department = ?, email = ? WHERE advisorId = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, advisor.getName());
            pstmt.setString(2, advisor.getUsername());
            pstmt.setString(3, advisor.getPasswordHash());
            pstmt.setString(4, advisor.getDepartment());
            pstmt.setString(5, advisor.getEmail());
            pstmt.setInt(6, advisor.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating advisor: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteAdvisor(int advisorId) {
        String sql = "DELETE FROM advisors WHERE advisorId = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, advisorId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting advisor: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Optional<Advisor> validateAdvisorCredentials(String username, String passwordHash) {
        String sql = "SELECT * FROM advisors WHERE username = ? AND passwordHash = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, passwordHash);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToAdvisor(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error validating advisor credentials: " + e.getMessage());
        }

        return Optional.empty();
    }

    /**
     * Maps a ResultSet to an Advisor object.
     * @param rs the ResultSet to map
     * @return the mapped Advisor object
     * @throws SQLException if a database access error occurs
     */
    private Advisor mapResultSetToAdvisor(ResultSet rs) throws SQLException {
        Advisor advisor = new Advisor();
        advisor.setId(rs.getInt("advisorId"));
        advisor.setName(rs.getString("name"));
        advisor.setUsername(rs.getString("username"));
        advisor.setPasswordHash(rs.getString("passwordHash"));
        advisor.setDepartment(rs.getString("department"));
        advisor.setEmail(rs.getString("email"));
        return advisor;
    }
}