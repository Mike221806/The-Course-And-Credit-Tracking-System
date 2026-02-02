package com.university.data;

/**
 * Represents advisor feedback for a student.
 * Demonstrates proper encapsulation with private fields and public getters/setters.
 */
public class AdvisorFeedback {

    private int feedbackId;
    private int advisorId;
    private int studentId;
    private String note;
    private String createdAt;

    public AdvisorFeedback() {}

    public AdvisorFeedback(int feedbackId, int advisorId, int studentId, String note, String createdAt) {
        this.feedbackId = feedbackId;
        this.advisorId = advisorId;
        this.studentId = studentId;
        this.note = note;
        this.createdAt = createdAt;
    }

    public int getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(int feedbackId) {
        this.feedbackId = feedbackId;
    }

    public int getAdvisorId() {
        return advisorId;
    }

    public void setAdvisorId(int advisorId) {
        this.advisorId = advisorId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "AdvisorFeedback{" +
                "feedbackId=" + feedbackId +
                ", advisorId=" + advisorId +
                ", studentId=" + studentId +
                ", note='" + note + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}