package com.university.data;

import com.university.roles.Student;

import java.util.List;
import java.util.Optional;

/**
 * Interface for student data access operations.
 * Demonstrates Interface Segregation Principle (ISP) by providing specific student-related operations.
 * Supports Dependency Inversion Principle (DIP) - services depend on this interface, not concrete implementations.
 */
public interface StudentDetails {

    /**
     * Creates a new student record.
     * @param student the student to create
     * @return true if successful, false otherwise
     */
    boolean createStudent(Student student);

    /**
     * Retrieves a student by ID.
     * @param studentId the student ID
     * @return Optional containing the student if found, empty otherwise
     */
    Optional<Student> getStudentById(int studentId);

    /**
     * Retrieves a student by username.
     * @param username the username
     * @return Optional containing the student if found, empty otherwise
     */
    Optional<Student> getStudentByUsername(String username);

    /**
     * Retrieves all students.
     * @return list of all students
     */
    List<Student> getAllStudents();

    /**
     * Updates an existing student record.
     * @param student the student to update
     * @return true if successful, false otherwise
     */
    boolean updateStudent(Student student);

    /**
     * Deletes a student record.
     * @param studentId the student ID to delete
     * @return true if successful, false otherwise
     */
    boolean deleteStudent(int studentId);

    /**
     * Updates the completed credits for a student.
     * @param studentId the student ID
     * @param completedCredits the completed credits to set
     * @return true if successful, false otherwise
     */
    boolean updateCompletedCredits(int studentId, int completedCredits);

    /**
     * Updates the password for a student.
     * @param studentId the student ID
     * @param passwordHash the new password hash
     * @return true if successful, false otherwise
     */
    boolean updatePassword(int studentId, String passwordHash);

    /**
     * Validates student credentials for login.
     * @param username the username
     * @param passwordHash the password hash
     * @return Optional containing the student if credentials are valid, empty otherwise
     */
    Optional<Student> validateStudentCredentials(String username, String passwordHash);
}