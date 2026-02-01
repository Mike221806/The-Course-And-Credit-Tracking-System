package com.university.courses;

/**
 * Represents a grade for a specific coursework item for a student.
 * Demonstrates encapsulation and links students to their coursework performance.
 */
public class CourseworkGrade {
    private int gradeId;
    private int studentId;
    private String courseCode;
    private int itemId;
    private double marksObtained;

    public CourseworkGrade() {}

    public CourseworkGrade(int gradeId, int studentId, String courseCode,
                           int itemId, double marksObtained) {
        this.gradeId = gradeId;
        this.studentId = studentId;
        this.courseCode = courseCode;
        this.itemId = itemId;
        this.marksObtained = marksObtained;
    }

    // Getters and Setters
    public int getGradeId() {
        return gradeId;
    }

    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
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

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public double getMarksObtained() {
        return marksObtained;
    }

    public void setMarksObtained(double marksObtained) {
        this.marksObtained = marksObtained;
    }

    /**
     * Calculates the percentage score for this coursework item.
     * @param totalMarks the total marks for the coursework item
     * @return percentage (0.0 to 100.0)
     */
    public double calculatePercentage(double totalMarks) {
        if (totalMarks <= 0) return 0.0;
        return (marksObtained / totalMarks) * 100;
    }

    /**
     * Validates that the marks obtained are within valid range.
     * @param totalMarks the total marks for the coursework item
     * @return true if marks are valid (0 <= marks <= totalMarks)
     */
    public boolean isValidMarks(double totalMarks) {
        return marksObtained >= 0 && marksObtained <= totalMarks;
    }

    @Override
    public String toString() {
        return "CourseworkGrade{" +
                "gradeId=" + gradeId +
                ", studentId=" + studentId +
                ", courseCode='" + courseCode + '\'' +
                ", itemId=" + itemId +
                ", marksObtained=" + marksObtained +
                '}';
    }
}
