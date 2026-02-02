package com.university.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import com.university.roles.Student;
import com.university.data.AdvisorFeedbackDetails;
import com.university.data.AdvisorFeedbackSqlDetails;
import com.university.data.AdvisorDetails;
import com.university.data.AdvisorSqlDetails;
import com.university.data.AdvisorFeedback;
import com.university.roles.Advisor;

import java.util.List;
import java.util.Optional;

/**
 * Controller for displaying advisor feedback to students.
 * Shows all feedback that advisors have provided for the current student.
 */
public class StudentFeedbackController {

    @FXML
    private VBox feedbackContainer;

    private Student currentStudent;
    private AdvisorFeedbackDetails feedbackDetails;
    private AdvisorDetails advisorDetails;

    public void setStudent(Student student) {
        this.currentStudent = student;
        this.feedbackDetails = new AdvisorFeedbackSqlDetails();
        this.advisorDetails = new AdvisorSqlDetails();
        loadFeedback();
    }

    private void loadFeedback() {
        try {
            feedbackContainer.getChildren().clear();

            List<AdvisorFeedback> feedbackList = feedbackDetails.getFeedbackByStudentId(currentStudent.getId());

            if (feedbackList.isEmpty()) {
                Label noFeedbackLabel = new Label("No feedback from your advisors yet.");
                noFeedbackLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #666;");
                feedbackContainer.getChildren().add(noFeedbackLabel);
                return;
            }

            for (AdvisorFeedback feedback : feedbackList) {
                VBox feedbackCard = createFeedbackCard(feedback);
                feedbackContainer.getChildren().add(feedbackCard);
            }

        } catch (Exception e) {
            System.err.println("Error loading feedback: " + e.getMessage());
            Label errorLabel = new Label("Error loading feedback: " + e.getMessage());
            errorLabel.setStyle("-fx-text-fill: #d32f2f;");
            feedbackContainer.getChildren().add(errorLabel);
        }
    }

    private VBox createFeedbackCard(AdvisorFeedback feedback) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; " +
                "-fx-border-radius: 8px; -fx-background-radius: 8px; -fx-padding: 15px;");

        // Header with advisor name and date
        HBox header = new HBox();
        header.setSpacing(10);
        header.setStyle("-fx-alignment: center-left;");

        try {
            int advisorId = feedback.getAdvisorId();
            Optional<Advisor> advisorOpt = advisorDetails.getAdvisorById(advisorId);

            if (advisorOpt.isPresent()) {
                Advisor advisor = advisorOpt.get();
                Label advisorLabel = new Label("From: " + advisor.getName());
                advisorLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #2c3e50;");

                String timestamp = feedback.getCreatedAt();
                Label dateLabel = new Label(timestamp);
                dateLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #6c757d;");

                header.getChildren().addAll(advisorLabel, dateLabel);
            } else {
                Label advisorLabel = new Label("From: Advisor");
                advisorLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
                header.getChildren().add(advisorLabel);
            }

        } catch (Exception e) {
            Label advisorLabel = new Label("From: Advisor");
            advisorLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
            header.getChildren().add(advisorLabel);
        }

        // Feedback content
        String feedbackText = feedback.getNote();
        TextFlow feedbackContent = new TextFlow();
        Text feedbackTextElement = new Text(feedbackText);
        feedbackTextElement.setStyle("-fx-font-size: 14px; -fx-line-spacing: 1.4;");
        feedbackContent.getChildren().add(feedbackTextElement);

        // Make the feedback content wrap properly
        feedbackContent.setPrefWidth(600);
        feedbackContent.setMaxWidth(600);

        card.getChildren().addAll(header, feedbackContent);
        return card;
    }

    @FXML
    private void refreshFeedback(ActionEvent event) {
        loadFeedback();
    }
}