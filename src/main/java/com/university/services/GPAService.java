package com.university.services;

import com.university.data.EnrollmentDetails;
import com.university.data.StudentDetails;
import com.university.data.CourseDetails;
import com.university.courses.Enrollment;
import com.university.courses.Course;

import java.util.List;
import java.util.Optional;

/**
 * Service for calculating and managing GPA and academic progress.
 * Demonstrates Single Responsibility Principle (SRP) by focusing on GPA calculations.
 * Follows Dependency Inversion Principle (DIP) by depending on interfaces.
 */
public class GPAService {

    private final EnrollmentDetails enrollmentDetails;
    private final StudentDetails studentDetails;
    private final CourseDetails courseDetails;

    public GPAService(EnrollmentDetails enrollmentDetails, StudentDetails studentDetails, CourseDetails courseDetails) {
        this.enrollmentDetails = enrollmentDetails;
        this.studentDetails = studentDetails;
        this.courseDetails = courseDetails;
    }

    /**
     * Calculates the GPA for a student for a specific semester.
     * @param studentId the student ID
     * @param semester the semester
     * @param year the year
     * @return the GPA (0.0 to 4.0)
     */
    public double calculateSemesterGPA(int studentId, String semester, int year) {
        List<Enrollment> enrollments = enrollmentDetails.getEnrollmentsByStudentId(studentId);

        double totalGradePoints = 0.0;
        int totalCredits = 0;

        for (Enrollment enrollment : enrollments) {
            if (enrollment.getSemester().equals(semester) && enrollment.getYear() == year
                    && enrollment.hasFinalGrade()) {

                Optional<Course> course = courseDetails.getCourseByCode(enrollment.getCourseCode());
                if (course.isPresent()) {
                    totalGradePoints += enrollment.getGradePoints() * course.get().getCreditHours();
                    totalCredits += course.get().getCreditHours();
                }
            }
        }

        return totalCredits > 0 ? totalGradePoints / totalCredits : 0.0;
    }

    /**
     * Calculates the cumulative GPA (CGPA) for a student.
     * @param studentId the student ID
     * @return the CGPA (0.0 to 4.0)
     */
    public double calculateCGPA(int studentId) {
        List<Enrollment> enrollments = enrollmentDetails.getEnrollmentsByStudentId(studentId);

        double totalGradePoints = 0.0;
        int totalCredits = 0;

        for (Enrollment enrollment : enrollments) {
            if (enrollment.hasFinalGrade()) {
                Optional<Course> course = courseDetails.getCourseByCode(enrollment.getCourseCode());
                if (course.isPresent()) {
                    totalGradePoints += enrollment.getGradePoints() * course.get().getCreditHours();
                    totalCredits += course.get().getCreditHours();
                }
            }
        }

        return totalCredits > 0 ? totalGradePoints / totalCredits : 0.0;
    }

    /**
     * Calculates the total completed credits for a student.
     * @param studentId the student ID
     * @return the total completed credits
     */
    public int calculateCompletedCredits(int studentId) {
        List<Enrollment> enrollments = enrollmentDetails.getEnrollmentsByStudentId(studentId);

        int totalCredits = 0;

        for (Enrollment enrollment : enrollments) {
            if (enrollment.hasFinalGrade() && !enrollment.getFinalGrade().equals("F")) {
                Optional<Course> course = courseDetails.getCourseByCode(enrollment.getCourseCode());
                if (course.isPresent()) {
                    totalCredits += course.get().getCreditHours();
                }
            }
        }

        return totalCredits;
    }

    /**
     * Updates the completed credits for a student.
     * @param studentId the student ID
     * @return true if successful, false otherwise
     */
    public boolean updateStudentCompletedCredits(int studentId) {
        int completedCredits = calculateCompletedCredits(studentId);
        return studentDetails.updateCompletedCredits(studentId, completedCredits);
    }

    /**
     * Gets the academic progress for a student.
     * @param studentId the student ID
     * @return formatted progress string
     */
    public String getAcademicProgress(int studentId) {
        Optional<com.university.roles.Student> student = studentDetails.getStudentById(studentId);
        if (student.isEmpty()) {
            return "Student not found";
        }

        com.university.roles.Student s = student.get();
        int completedCredits = calculateCompletedCredits(studentId);
        int requiredCredits = s.getRequiredCredits();
        double cgpa = calculateCGPA(studentId);

        return String.format("Progress: %d/%d credits completed (%.1f%%), CGPA: %.2f",
                completedCredits, requiredCredits,
                (double) completedCredits / requiredCredits * 100, cgpa);
    }

    /**
     * Checks if a student is on academic probation.
     * @param studentId the student ID
     * @return true if on probation, false otherwise
     */
    public boolean isOnAcademicProbation(int studentId) {
        double cgpa = calculateCGPA(studentId);
        return cgpa < 2.0;
    }

    /**
     * Checks if a student is eligible for graduation.
     * @param studentId the student ID
     * @return true if eligible, false otherwise
     */
    public boolean isEligibleForGraduation(int studentId) {
        Optional<com.university.roles.Student> student = studentDetails.getStudentById(studentId);
        if (student.isEmpty()) {
            return false;
        }

        com.university.roles.Student s = student.get();
        int completedCredits = calculateCompletedCredits(studentId);
        double cgpa = calculateCGPA(studentId);

        return completedCredits >= s.getRequiredCredits() && cgpa >= 2.0;
    }

    /**
     * Converts numeric GPA to letter grade.
     * @param gpa the GPA value
     * @return the corresponding letter grade
     */
    public String gpaToLetterGrade(double gpa) {
        if (gpa >= 3.7) return "A";
        if (gpa >= 3.3) return "B+";
        if (gpa >= 3.0) return "B";
        if (gpa >= 2.7) return "C+";
        if (gpa >= 2.0) return "C";
        if (gpa >= 1.7) return "D+";
        if (gpa >= 1.0) return "D";
        return "F";
    }
}