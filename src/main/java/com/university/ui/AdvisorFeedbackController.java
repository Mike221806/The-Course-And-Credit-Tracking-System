package com.university.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.university.roles.Advisor;
import com.university.data.AdvisorFeedbackDetails;
import com.university.data.AdvisorFeedbackSqlDetails;
import com.university.data.StudentDetails;
import com.university.data.StudentSqlDetails;
import com.university.roles.Student;

import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Controller for adding advisor feedback.
 * Allows advisors to add notes and feedback for students.
 */
public class AdvisorFeedbackController {

    @FXML
    private ComboBox<Student> studentComboBox;

    @FXML
    private TextArea feedbackTextArea;

    @FXML
    private Button addButton;

    @FXML
    private Button clearButton;

    @FXML
    private Label statusLabel;

    private Advisor currentAdvisor;
    private StudentDetails studentDetails;
    private AdvisorFeedbackDetails feedbackDetails;

    public void setAdvisor(Advisor advisor) {
        this.currentAdvisor = advisor;
        this.studentDetails = new StudentSqlDetails();
        this.feedbackDetails = new AdvisorFeedbackSqlDetails();
        loadStudents();
    }

    private void loadStudents() {
        try {
            List<Student> students = studentDetails.getAllStudents();
            studentComboBox.getItems().clear();
            studentComboBox.getItems().addAll(students);

            if (!students.isEmpty()) {
                studentComboBox.setValue(students.get(0));
            }
        } catch (Exception e) {
            statusLabel.setText("Error loading students: " + e.getMessage());
        }
    }

    @FXML
    private void handleAdd(ActionEvent event) {
        Student selectedStudent = studentComboBox.getValue();
        String feedback = feedbackTextArea.getText().trim();

        if (selectedStudent == null) {
            statusLabel.setText("Please select a student");
            return;
        }

        if (feedback.isEmpty()) {
            statusLabel.setText("Please enter feedback");
            return;
        }

        try {
            // Save feedback to database
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            boolean success = feedbackDetails.addFeedback(
                    currentAdvisor.getId(),
                    selectedStudent.getId(),
                    feedback,
                    timestamp
            );

            if (success) {
                statusLabel.setText("Feedback saved successfully for " + selectedStudent.getName());
                clearForm();
            } else {
                statusLabel.setText("Failed to save feedback");
            }

        } catch (Exception e) {
            System.err.println("Error adding feedback: " + e.getMessage());
            statusLabel.setText("Error adding feedback: " + e.getMessage());
        }
    }

    @FXML
    private void handleClear(ActionEvent event) {
        clearForm();
    }

    private void clearForm() {
        feedbackTextArea.clear();
        statusLabel.setText("");
    }
}