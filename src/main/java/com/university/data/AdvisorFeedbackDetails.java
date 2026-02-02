package com.university.data;

import java.util.List;
import java.util.Optional;

/**
 * Interface for advisor feedback data access operations.
 * Demonstrates Interface Segregation Principle (ISP) by focusing only on feedback operations.
 */
public interface AdvisorFeedbackDetails {

    /**
     * Adds feedback for a student from an advisor.
     * @param advisorId the ID of the advisor providing feedback
     * @param studentId the ID of the student receiving feedback
     * @param note the feedback note
     * @param createdAt the timestamp when feedback was created
     * @return true if feedback was added successfully, false otherwise
     */
    boolean addFeedback(int advisorId, int studentId, String note, String createdAt);

    /**
     * Retrieves all feedback for a specific student.
     * @param studentId the ID of the student
     * @return list of feedback entries for the student
     */
    List<AdvisorFeedback> getFeedbackByStudentId(int studentId);

    /**
     * Retrieves all feedback given by a specific advisor.
     * @param advisorId the ID of the advisor
     * @return list of feedback entries given by the advisor
     */
    List<AdvisorFeedback> getFeedbackByAdvisorId(int advisorId);

    /**
     * Retrieves a specific feedback entry by ID.
     * @param feedbackId the ID of the feedback
     * @return optional containing the feedback if found, empty otherwise
     */
    Optional<AdvisorFeedback> getFeedbackById(int feedbackId);

    /**
     * Updates an existing feedback entry.
     * @param feedback the feedback object with updated information
     * @return true if update was successful, false otherwise
     */
    boolean updateFeedback(AdvisorFeedback feedback);

    /**
     * Deletes a feedback entry.
     * @param feedbackId the ID of the feedback to delete
     * @return true if deletion was successful, false otherwise
     */
    boolean deleteFeedback(int feedbackId);
}
