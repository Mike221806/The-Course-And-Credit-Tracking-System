package com.university.courses;

/**
 * Represents a course in the university system.
 * Demonstrates encapsulation with private fields and provides getters/setters.
 */
public class Course {
    private String courseCode;
    private String title;
    private int creditHours;

    public Course() {}

    public Course(String courseCode, String title, int creditHours) {
        this.courseCode = courseCode;
        this.title = title;
        this.creditHours = creditHours;
    }

    // Getters and Setters
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

    public int getCreditHours() {
        return creditHours;
    }

    public void setCreditHours(int creditHours) {
        this.creditHours = creditHours;
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseCode='" + courseCode + '\'' +
                ", title='" + title + '\'' +
                ", creditHours=" + creditHours +
                '}';
    }
}