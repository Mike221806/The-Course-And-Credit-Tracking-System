package com.university.courses;

import java.time.LocalDate;

/**
 * Abstract base class for coursework items (assignments, exams).
 * Demonstrates abstraction and provides common functionality for all coursework types.
 * Supports Open/Closed Principle - new coursework types can be added without modifying existing code.
 */
public abstract class CourseworkItem {
    private int itemId;
    private String courseCode;
    private String title;
    private double totalMarks;
    private double weight;
    private LocalDate dueDate;

    public CourseworkItem() {}

    public CourseworkItem(int itemId, String courseCode, String title,
                          double totalMarks, double weight, LocalDate dueDate) {
        this.itemId = itemId;
        this.courseCode = courseCode;
        this.title = title;
        this.totalMarks = totalMarks;
        this.weight = weight;
        this.dueDate = dueDate;
    }

    // Getters and Setters
    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(double totalMarks) {
        this.totalMarks = totalMarks;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * Abstract method to get the type of coursework.
     * Must be implemented by subclasses to demonstrate polymorphism.
     * @return the type of coursework as a string
     */
    public abstract String getType();

    /**
     * Calculates the weighted contribution to final grade.
     * @param marksObtained the marks obtained by the student
     * @return weighted contribution (0.0 to 1.0)
     */
    public double calculateWeightedContribution(double marksObtained) {
        if (totalMarks <= 0) return 0.0;
        double percentage = (marksObtained / totalMarks) * 100;
        return (percentage * weight) / 100;
    }

    @Override
    public String toString() {
        return "CourseworkItem{" +
                "itemId=" + itemId +
                ", courseCode='" + courseCode + '\'' +
                ", title='" + title + '\'' +
                ", totalMarks=" + totalMarks +
                ", weight=" + weight +
                ", dueDate=" + dueDate +
                ", type='" + getType() + '\'' +
                '}';
    }
}