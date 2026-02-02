package com.university.courses;

import java.time.LocalDate;

/**
 * Represents a midterm exam.
 * Extends Exam and provides specific implementation for midterm type.
 * Demonstrates inheritance and polymorphism in the coursework hierarchy.
 */
public class MidtermExam extends Exam {

    public MidtermExam() {
        super();
    }

    public MidtermExam(int itemId, String courseCode, String title,
                       double totalMarks, double weight, LocalDate dueDate) {
        super(itemId, courseCode, title, totalMarks, weight, dueDate);
    }

    @Override
    public String getType() {
        return "MIDTERM_EXAM";
    }

    @Override
    public String getExamType() {
        return "MIDTERM";
    }

    /**
     * Midterm-specific validation.
     * @return true if midterm details are valid
     */
    public boolean isValidMidterm() {
        return isValidExam() && getWeight() <= 40; // Midterms typically have lower weight
    }

    @Override
    public String toString() {
        return "MidtermExam{" +
                "itemId=" + getItemId() +
                ", courseCode='" + getCourseCode() + '\'' +
                ", title='" + getTitle() + '\'' +
                ", totalMarks=" + getTotalMarks() +
                ", weight=" + getWeight() +
                ", dueDate=" + getDueDate() +
                '}';
    }
}