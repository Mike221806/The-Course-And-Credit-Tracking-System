package com.university.courses;

/**
 * Represents an enrollment record for a student in a course.
 * Demonstrates encapsulation and provides data for tracking student course enrollment.
 */
public class Enrollment {
    private int enrollmentId;
    private int studentId;
    private String courseCode;
    private String courseTitle;
    private int credits;
    private String semester;
    private int year;
    private String finalGrade;

    public Enrollment() {}

    public Enrollment(int enrollmentId, int studentId, String courseCode,
                      String courseTitle, int credits, String semester, int year, String finalGrade) {
        this.enrollmentId = enrollmentId;
        this.studentId = studentId;
        this.courseCode = courseCode;
        this.courseTitle = courseTitle;
        this.credits = credits;
        this.semester = semester;
        this.year = year;
        this.finalGrade = finalGrade;
    }

    // Getters and Setters
    public int getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(int enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getFinalGrade() {
        return finalGrade;
    }

    public void setFinalGrade(String finalGrade) {
        this.finalGrade = finalGrade;
    }

    /**
     * Checks if the enrollment has a final grade assigned.
     * @return true if final grade is not null and not empty
     */
    public boolean hasFinalGrade() {
        return finalGrade != null && !finalGrade.trim().isEmpty();
    }

    /**
     * Converts letter grade to grade points for GPA calculation.
     * @return grade points (0.0 to 4.0)
     */
    public double getGradePoints() {
        if (finalGrade == null) return 0.0;

        switch (finalGrade.toUpperCase()) {
            case "A+": return 4.0;  // Same as A for GPA calculation
            case "A": return 4.0;
            case "A-": return 3.75;
            case "B+": return 3.5;
            case "B": return 3.0;
            case "B-": return 2.75;
            case "C+": return 2.5;
            case "C": return 2.0;
            case "C-": return 1.7;
            case "D+": return 1.3;
            case "D": return 1.0;
            case "D-": return 0.7;
            case "F": return 0.0;
            default: return 0.0;
        }
    }

    @Override
    public String toString() {
        return "Enrollment{" +
                "enrollmentId=" + enrollmentId +
                ", studentId=" + studentId +
                ", courseCode='" + courseCode + '\'' +
                ", semester='" + semester + '\'' +
                ", year=" + year +
                ", finalGrade='" + finalGrade + '\'' +
                '}';
    }
}