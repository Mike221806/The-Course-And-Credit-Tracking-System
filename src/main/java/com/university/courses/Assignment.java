package com.university.courses;

import java.time.LocalDate;

/**
 * Represents an assignment coursework item.
 * Extends CourseworkItem and provides specific implementation for assignment type.
 * Demonstrates inheritance and polymorphism.
 */
public class Assignment extends CourseworkItem {

    public Assignment() {
        super();
    }

    public Assignment(int itemId, String courseCode, String title,
                      double totalMarks, double weight, LocalDate dueDate) {
        super(itemId, courseCode, title, totalMarks, weight, dueDate);
    }

    @Override
    public String getType() {
        return "ASSIGNMENT";
    }

    /**
     * Assignment-specific validation method.
     * @return true if assignment details are valid
     */
    public boolean isValidAssignment() {
        return getTitle() != null && !getTitle().trim().isEmpty() &&
                getTotalMarks() > 0 &&
                getWeight() >= 0 && getWeight() <= 100 &&
                getDueDate() != null;
    }

    @Override
    public String toString() {
        return "Assignment{" +
                "itemId=" + getItemId() +
                ", courseCode='" + getCourseCode() + '\'' +
                ", title='" + getTitle() + '\'' +
                ", totalMarks=" + getTotalMarks() +
                ", weight=" + getWeight() +
                ", dueDate=" + getDueDate() +
                '}';
    }
}