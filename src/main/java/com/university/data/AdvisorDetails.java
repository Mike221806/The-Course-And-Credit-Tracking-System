package com.university.data;

import com.university.roles.Advisor;

import java.util.List;
import java.util.Optional;

/**
 * Interface for advisor data access operations.
 * Demonstrates Interface Segregation Principle (ISP) by providing specific advisor-related operations.
 * Supports Dependency Inversion Principle (DIP) - services depend on this interface, not concrete implementations.
 */
public interface AdvisorDetails {

    /**
     * Creates a new advisor record.
     * @param advisor the advisor to create
     * @return true if successful, false otherwise
     */
    boolean createAdvisor(Advisor advisor);

    /**
     * Retrieves an advisor by ID.
     * @param advisorId the advisor ID
     * @return Optional containing the advisor if found, empty otherwise
     */
    Optional<Advisor> getAdvisorById(int advisorId);

    /**
     * Retrieves an advisor by username.
     * @param username the username
     * @return Optional containing the advisor if found, empty otherwise
     */
    Optional<Advisor> getAdvisorByUsername(String username);

    /**
     * Retrieves all advisors.
     * @return list of all advisors
     */
    List<Advisor> getAllAdvisors();

    /**
     * Updates an existing advisor record.
     * @param advisor the advisor to update
     * @return true if successful, false otherwise
     */
    boolean updateAdvisor(Advisor advisor);

    /**
     * Deletes an advisor record.
     * @param advisorId the advisor ID to delete
     * @return true if successful, false otherwise
     */
    boolean deleteAdvisor(int advisorId);

    /**
     * Validates advisor credentials for login.
     * @param username the username
     * @param passwordHash the password hash
     * @return Optional containing the advisor if credentials are valid, empty otherwise
     */
    Optional<Advisor> validateAdvisorCredentials(String username, String passwordHash);
}