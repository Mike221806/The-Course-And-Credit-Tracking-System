package com.university.data;

import com.university.courses.Enrollment;

import java.util.List;
import java.util.Optional;

/**
 * Interface for enrollment data access operations.
 * Demonstrates Interface Segregation Principle (ISP) by providing specific enrollment-related operations.
 * Supports Dependency Inversion Principle (DIP) - services depend on this interface, not concrete implementations.
 */
public interface EnrollmentDetails {

    /**
     * Creates a new enrollment record.
     * @param enrollment the enrollment to create
     * @return true if successful, false otherwise
     */
    boolean createEnrollment(Enrollment enrollment);

    /**
     * Retrieves an enrollment by ID.
     * @param enrollmentId the enrollment ID
     * @return Optional containing the enrollment if found, empty otherwise
     */
    Optional<Enrollment> getEnrollmentById(int enrollmentId);

    /**
     * Retrieves all enrollments for a specific student.
     * @param studentId the student ID
     * @return list of enrollments for the student
     */
    List<Enrollment> getEnrollmentsByStudentId(int studentId);

    /**
     * Retrieves all enrollments for a specific course.
     * @param courseCode the course code
     * @return list of enrollments for the course
     */
    List<Enrollment> getEnrollmentsByCourseCode(String courseCode);

    /**
     * Updates an existing enrollment record.
     * @param enrollment the enrollment to update
     * @return true if successful, false otherwise
     */
    boolean updateEnrollment(Enrollment enrollment);

    /**
     * Deletes an enrollment record.
     * @param enrollmentId the enrollment ID to delete
     * @return true if successful, false otherwise
     */
    boolean deleteEnrollment(int enrollmentId);

    /**
     * Updates the final grade for an enrollment.
     * @param enrollmentId the enrollment ID
     * @param finalGrade the final grade to set
     * @return true if successful, false otherwise
     */
    boolean updateFinalGrade(int enrollmentId, String finalGrade);

    /**
     * Checks if a student is enrolled in a specific course.
     * @param studentId the student ID
     * @param courseCode the course code
     * @return true if enrolled, false otherwise
     */
    boolean isStudentEnrolled(int studentId, String courseCode);
}