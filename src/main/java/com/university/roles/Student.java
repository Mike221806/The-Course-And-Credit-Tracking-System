package com.university.roles;

/**
 * Represents a student in the university system.
 * Inherits from Person and adds student-specific attributes.
 * Demonstrates inheritance and can be used polymorphically as a Person.
 */
public class Student extends Person {
    private String program;
    private int requiredCredits;
    private int completedCredits;

    public Student() {
        super();
        setRole("STUDENT");
    }

    public Student(int id, String name, String username, String passwordHash,
                   String program, int requiredCredits, int completedCredits) {
        super(id, name, username, passwordHash, "STUDENT");
        this.program = program;
        this.requiredCredits = requiredCredits;
        this.completedCredits = completedCredits;
    }

    // Getters and Setters
    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public int getRequiredCredits() {
        return requiredCredits;
    }

    public void setRequiredCredits(int requiredCredits) {
        this.requiredCredits = requiredCredits;
    }

    public int getCompletedCredits() {
        return completedCredits;
    }

    public void setCompletedCredits(int completedCredits) {
        this.completedCredits = completedCredits;
    }

    /**
     * Calculates remaining credits needed for graduation.
     * @return remaining credits (0 if completed)
     */
    public int getRemainingCredits() {
        return Math.max(0, requiredCredits - completedCredits);
    }

    /**
     * Checks if student has completed all required credits.
     * @return true if completed, false otherwise
     */
    public boolean isGraduationComplete() {
        return completedCredits >= requiredCredits;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", username='" + getUsername() + '\'' +
                ", program='" + program + '\'' +
                ", requiredCredits=" + requiredCredits +
                ", completedCredits=" + completedCredits +
                '}';
    }
}