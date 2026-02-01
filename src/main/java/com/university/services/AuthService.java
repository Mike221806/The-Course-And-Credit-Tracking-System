package com.university.services;

import com.university.data.StudentDetails;
import com.university.data.AdvisorDetails;
import com.university.roles.Student;
import com.university.roles.Advisor;


import java.util.Optional;

/**
 * Authentication service for user login and validation.
 * Demonstrates Single Responsibility Principle (SRP) by focusing only on authentication.
 * Follows Dependency Inversion Principle (DIP) by depending on interfaces.
 */
public class AuthService {

    private final StudentDetails studentDetails;
    private final AdvisorDetails advisorDetails;

    public AuthService(StudentDetails studentDetails, AdvisorDetails advisorDetails) {
        this.studentDetails = studentDetails;
        this.advisorDetails = advisorDetails;
    }

    /**
     * Authenticates a student user.
     * @param username the student's username
     * @param password the plain text password
     * @return Optional containing the authenticated student if successful, empty otherwise
     */
    public Optional<Student> authenticateStudent(String username, String password) {
        if (username == null || password == null) {
            return Optional.empty();
        }

        // First get the student by username
        Optional<Student> studentOpt = studentDetails.getStudentByUsername(username);
        if (!studentOpt.isPresent()) {
            return Optional.empty();
        }

        // Then verify the password
        Student student = studentOpt.get();
        if (PasswordUtil.verifyPassword(password, student.getPasswordHash())) {
            return Optional.of(student);
        }

        return Optional.empty();
    }

    /**
     * Authenticates an advisor user.
     * @param username the advisor's username
     * @param password the plain text password
     * @return Optional containing the authenticated advisor if successful, empty otherwise
     */
    public Optional<Advisor> authenticateAdvisor(String username, String password) {
        if (username == null || password == null) {
            return Optional.empty();
        }

        // First get the advisor by username
        Optional<Advisor> advisorOpt = advisorDetails.getAdvisorByUsername(username);
        if (!advisorOpt.isPresent()) {
            return Optional.empty();
        }

        // Then verify the password
        Advisor advisor = advisorOpt.get();
        if (PasswordUtil.verifyPassword(password, advisor.getPasswordHash())) {
            return Optional.of(advisor);
        }

        return Optional.empty();
    }

    /**
     * Validates username format.
     * @param username the username to validate
     * @return true if username is valid, false otherwise
     */
    public boolean isValidUsername(String username) {
        return username != null && username.length() >= 3 && username.matches("^[a-zA-Z0-9_]+$");
    }

    /**
     * Validates password format.
     * @param password the password to validate
     * @return true if password is valid, false otherwise
     */
    public boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    /**
     * Checks if a username already exists for students.
     * @param username the username to check
     * @return true if username exists, false otherwise
     */
    public boolean studentUsernameExists(String username) {
        return studentDetails.getStudentByUsername(username).isPresent();
    }

    /**
     * Checks if a username already exists for advisors.
     * @param username the username to check
     * @return true if username exists, false otherwise
     */
    public boolean advisorUsernameExists(String username) {
        return advisorDetails.getAdvisorByUsername(username).isPresent();
    }
}