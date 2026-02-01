package com.university.data;

import com.university.courses.Course;

import java.util.List;
import java.util.Optional;

/**
 * Interface for course data access operations.
 * Demonstrates Interface Segregation Principle (ISP) by providing specific course-related operations.
 * Supports Dependency Inversion Principle (DIP) - services depend on this interface, not concrete implementations.
 */
public interface CourseDetails {

    /**
     * Creates a new course record.
     * @param course the course to create
     * @return true if successful, false otherwise
     */
    boolean createCourse(Course course);

    /**
     * Retrieves a course by course code.
     * @param courseCode the course code
     * @return Optional containing the course if found, empty otherwise
     */
    Optional<Course> getCourseByCode(String courseCode);

    /**
     * Retrieves all courses.
     * @return list of all courses
     */
    List<Course> getAllCourses();

    /**
     * Updates an existing course record.
     * @param course the course to update
     * @return true if successful, false otherwise
     */
    boolean updateCourse(Course course);

    /**
     * Deletes a course record.
     * @param courseCode the course code to delete
     * @return true if successful, false otherwise
     */
    boolean deleteCourse(String courseCode);

    /**
     * Checks if a course exists.
     * @param courseCode the course code to check
     * @return true if course exists, false otherwise
     */
    boolean courseExists(String courseCode);
}
