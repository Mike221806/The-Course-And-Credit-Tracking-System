package com.university.courses;

import java.time.LocalDate;

/**
 * Abstract base class for exam types.
 * Extends CourseworkItem and provides common functionality for all exam types.
 * Demonstrates abstraction and supports the inheritance hierarchy.
 */
public abstract class Exam extends CourseworkItem {

    public Exam() {
        super();
    }

    public Exam(int itemId, String courseCode, String title,
                double totalMarks, double weight, LocalDate dueDate) {
        super(itemId, courseCode, title, totalMarks, weight, dueDate);
    }

    /**
     * Abstract method to get the specific exam type.
     * Must be implemented by subclasses (MidtermExam, FinalExam).
     * @return the specific exam type
     */
    public abstract String getExamType();

    /**
     * Exam-specific validation method.
     * @return true if exam details are valid
     */
    public boolean isValidExam() {
        return getTitle() != null && !getTitle().trim().isEmpty() &&
                getTotalMarks() > 0 &&
                getWeight() >= 0 && getWeight() <= 100 &&
                getDueDate() != null;
    }

    @Override
    public String toString() {
        return "Exam{" +
                "itemId=" + getItemId() +
                ", courseCode='" + getCourseCode() + '\'' +
                ", title='" + getTitle() + '\'' +
                ", totalMarks=" + getTotalMarks() +
                ", weight=" + getWeight() +
                ", dueDate=" + getDueDate() +
                ", examType='" + getExamType() + '\'' +
                '}';
    }
}