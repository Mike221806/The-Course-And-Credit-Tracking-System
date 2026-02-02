package com.university.data;

import com.university.courses.CourseworkItem;
import com.university.courses.CourseworkGrade;

import java.util.List;
import java.util.Optional;

/**
 * Interface for coursework data access operations.
 * Demonstrates Interface Segregation Principle (ISP) by providing specific coursework-related operations.
 * Supports Dependency Inversion Principle (DIP) - services depend on this interface, not concrete implementations.
 */
public interface CourseworkDetails {

    /**
     * Creates a new coursework item.
     * @param courseworkItem the coursework item to create
     * @return true if successful, false otherwise
     */
    boolean createCourseworkItem(CourseworkItem courseworkItem);

    /**
     * Retrieves a coursework item by ID.
     * @param itemId the item ID
     * @return Optional containing the coursework item if found, empty otherwise
     */
    Optional<CourseworkItem> getCourseworkItemById(int itemId);

    /**
     * Retrieves all coursework items for a specific course.
     * @param courseCode the course code
     * @return list of coursework items for the course
     */
    List<CourseworkItem> getCourseworkItemsByCourse(String courseCode);

    /**
     * Retrieves all coursework items of a specific type.
     * @param type the coursework type
     * @return list of coursework items of the specified type
     */
    List<CourseworkItem> getCourseworkItemsByType(String type);

    /**
     * Updates an existing coursework item.
     * @param courseworkItem the coursework item to update
     * @return true if successful, false otherwise
     */
    boolean updateCourseworkItem(CourseworkItem courseworkItem);

    /**
     * Deletes a coursework item.
     * @param itemId the item ID to delete
     * @return true if successful, false otherwise
     */
    boolean deleteCourseworkItem(int itemId);

    /**
     * Creates a new coursework grade record.
     * @param courseworkGrade the coursework grade to create
     * @return true if successful, false otherwise
     */
    boolean createCourseworkGrade(CourseworkGrade courseworkGrade);

    /**
     * Retrieves coursework grades for a specific student.
     * @param studentId the student ID
     * @return list of coursework grades for the student
     */
    List<CourseworkGrade> getCourseworkGradesByStudent(int studentId);

    /**
     * Retrieves coursework grades for a specific student in a course.
     * @param studentId the student ID
     * @param courseCode the course code
     * @return list of coursework grades for the student in the course
     */
    List<CourseworkGrade> getCourseworkGradesByStudentAndCourse(int studentId, String courseCode);

    /**
     * Updates an existing coursework grade.
     * @param courseworkGrade the coursework grade to update
     * @return true if successful, false otherwise
     */
    boolean updateCourseworkGrade(CourseworkGrade courseworkGrade);

    /**
     * Deletes a coursework grade record.
     * @param gradeId the grade ID to delete
     * @return true if successful, false otherwise
     */
    boolean deleteCourseworkGrade(int gradeId);
}