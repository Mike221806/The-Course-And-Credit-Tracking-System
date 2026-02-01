package com.university.services;

import com.university.data.CourseDetails;
import com.university.data.EnrollmentDetails;
import com.university.courses.Course;
import com.university.courses.Enrollment;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing courses and enrollments.
 * Demonstrates Single Responsibility Principle (SRP) by focusing on course and enrollment operations.
 * Follows Dependency Inversion Principle (DIP) by depending on interfaces.
 */
public class CourseService {

    private final CourseDetails courseDetails;
    private final EnrollmentDetails enrollmentDetails;

    public CourseService(CourseDetails courseDetails, EnrollmentDetails enrollmentDetails) {
        this.courseDetails = courseDetails;
        this.enrollmentDetails = enrollmentDetails;
    }

    /**
     * Creates a new course.
     * @param course the course to create
     * @return true if successful, false otherwise
     */
    public boolean createCourse(Course course) {
        if (course == null || course.getCourseCode() == null || course.getTitle() == null) {
            return false;
        }

        if (course.getCourseCode().trim().isEmpty() || course.getTitle().trim().isEmpty()) {
            return false;
        }

        if (course.getCreditHours() <= 0) {
            return false;
        }

        return courseDetails.createCourse(course);
    }

    /**
     * Retrieves a course by its code.
     * @param courseCode the course code
     * @return Optional containing the course if found, empty otherwise
     */
    public Optional<Course> getCourseByCode(String courseCode) {
        if (courseCode == null || courseCode.trim().isEmpty()) {
            return Optional.empty();
        }

        return courseDetails.getCourseByCode(courseCode);
    }

    /**
     * Retrieves all courses.
     * @return list of all courses
     */
    public List<Course> getAllCourses() {
        return courseDetails.getAllCourses();
    }

    /**
     * Updates an existing course.
     * @param course the course to update
     * @return true if successful, false otherwise
     */
    public boolean updateCourse(Course course) {
        if (course == null || course.getCourseCode() == null || course.getTitle() == null) {
            return false;
        }

        if (!courseDetails.courseExists(course.getCourseCode())) {
            return false;
        }

        return courseDetails.updateCourse(course);
    }

    /**
     * Deletes a course.
     * @param courseCode the course code to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteCourse(String courseCode) {
        if (courseCode == null || courseCode.trim().isEmpty()) {
            return false;
        }

        return courseDetails.deleteCourse(courseCode);
    }

    /**
     * Enrolls a student in a course.
     * @param enrollment the enrollment to create
     * @return true if successful, false otherwise
     */
    public boolean enrollStudent(Enrollment enrollment) {
        if (enrollment == null) {
            return false;
        }

        if (!courseDetails.courseExists(enrollment.getCourseCode())) {
            return false;
        }

        if (enrollmentDetails.isStudentEnrolled(enrollment.getStudentId(), enrollment.getCourseCode())) {
            return false;
        }

        return enrollmentDetails.createEnrollment(enrollment);
    }

    /**
     * Retrieves all enrollments for a student.
     * @param studentId the student ID
     * @return list of enrollments for the student
     */
    public List<Enrollment> getStudentEnrollments(int studentId) {
        return enrollmentDetails.getEnrollmentsByStudentId(studentId);
    }

    /**
     * Retrieves all enrollments for a course.
     * @param courseCode the course code
     * @return list of enrollments for the course
     */
    public List<Enrollment> getCourseEnrollments(String courseCode) {
        if (courseCode == null || courseCode.trim().isEmpty()) {
            return List.of();
        }

        return enrollmentDetails.getEnrollmentsByCourseCode(courseCode);
    }

    /**
     * Updates the final grade for an enrollment.
     * @param enrollmentId the enrollment ID
     * @param finalGrade the final grade to set
     * @return true if successful, false otherwise
     */
    public boolean updateFinalGrade(int enrollmentId, String finalGrade) {
        if (finalGrade == null || finalGrade.trim().isEmpty()) {
            return false;
        }

        if (!isValidGrade(finalGrade)) {
            return false;
        }

        return enrollmentDetails.updateFinalGrade(enrollmentId, finalGrade);
    }

    /**
     * Validates if a grade is in the correct format.
     * @param grade the grade to validate
     * @return true if valid, false otherwise
     */
    private boolean isValidGrade(String grade) {
        return grade.matches("^[ABCDF][+-]?$") || grade.matches("^[ABCDF]$");
    }
}