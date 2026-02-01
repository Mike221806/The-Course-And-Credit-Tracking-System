package com.university.roles;

/**
 * Represents an academic advisor in the university system.
 * Inherits from Person and adds advisor-specific attributes.
 * Demonstrates inheritance and can be used polymorphically as a Person.
 */
public class Advisor extends Person {
    private String department;
    private String email;

    public Advisor() {
        super();
        setRole("ADVISOR");
    }

    public Advisor(int id, String name, String username, String passwordHash,
                   String department, String email) {
        super(id, name, username, passwordHash, "ADVISOR");
        this.department = department;
        this.email = email;
    }

    // Getters and Setters
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Advisor{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", username='" + getUsername() + '\'' +
                ", department='" + department + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}