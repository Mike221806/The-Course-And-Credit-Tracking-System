package com.university.courses;

import java.time.LocalDate;

/**
 * Represents a final exam.
 * Extends Exam and provides specific implementation for final exam type.
 * Demonstrates inheritance and polymorphism in the coursework hierarchy.
 */
public class FinalExam extends Exam {

    public FinalExam() {
        super();
    }

    public FinalExam(int itemId, String courseCode, String title,
                     double totalMarks, double weight, LocalDate dueDate) {
        super(itemId, courseCode, title, totalMarks, weight, dueDate);
    }

    @Override
    public String getType() {
        return "FINAL_EXAM";
    }

    @Override
    public String getExamType() {
        return "FINAL";
    }

    /**
     * Final exam-specific validation.
     * @return true if final exam details are valid
     */
    public boolean isValidFinalExam() {
        return isValidExam() && getWeight() >= 20; // Final exams typically have higher weight
    }

    @Override
    public String toString() {
        return "FinalExam{" +
                "itemId=" + getItemId() +
                ", courseCode='" + getCourseCode() + '\'' +
                ", title='" + getTitle() + '\'' +
                ", totalMarks=" + getTotalMarks() +
                ", weight=" + getWeight() +
                ", dueDate=" + getDueDate() +
                '}';
    }
}