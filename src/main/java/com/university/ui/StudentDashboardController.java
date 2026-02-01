package com.university.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import com.university.roles.Student;

import java.io.IOException;

/**
 * Controller for the Student Dashboard.
 * Provides navigation to different student functions and displays student information.
 */
public class StudentDashboardController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private StackPane contentPane;

    private Student currentStudent;

    public void setStudent(Student student) {
        this.currentStudent = student;

        welcomeLabel.setText("Welcome, " + student.getName() + " (" + student.getProgram() + ")");
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginPage.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

            stage.setTitle("Course & Credit Tracking System");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            showAlert("Error", "Failed to logout: " + e.getMessage());
        }
    }

    @FXML
    private void showProfile(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/StudentProfile.fxml"));
            Parent root = loader.load();

            StudentProfileController controller = loader.getController();
            controller.setStudent(currentStudent);

            // Wrap content in ScrollPane
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setContent(root);
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

            contentPane.getChildren().clear();
            contentPane.getChildren().add(scrollPane);

        } catch (IOException e) {
            showAlert("Error", "Failed to load profile: " + e.getMessage());
        }
    }

    @FXML
    private void showCourses(ActionEvent event) {
        System.out.println("DEBUG: showCourses() method called in StudentDashboardController");
        try {
            System.out.println("DEBUG: Loading StudentCourses.fxml...");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/StudentCourses.fxml"));
            Parent root = loader.load();
            System.out.println("DEBUG: FXML loaded successfully");

            StudentCoursesController controller = loader.getController();
            System.out.println("DEBUG: Got controller: " + controller);
            controller.setStudent(currentStudent);
            System.out.println("DEBUG: Student set in controller");

            // Wrap content in ScrollPane
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setContent(root);
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

            contentPane.getChildren().clear();
            contentPane.getChildren().add(scrollPane);
            System.out.println("DEBUG: Courses view added to content pane");

        } catch (IOException e) {
            System.out.println("DEBUG: IOException loading courses: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "Failed to load courses: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("DEBUG: General error loading courses: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "Failed to load courses: " + e.getMessage());
        }
    }

    @FXML
    private void showCoursework(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/StudentCoursework.fxml"));
            Parent root = loader.load();

            StudentCourseworkController controller = loader.getController();
            controller.setStudent(currentStudent);

            // Wrap content in ScrollPane
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setContent(root);
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

            contentPane.getChildren().clear();
            contentPane.getChildren().add(scrollPane);

        } catch (IOException e) {
            showAlert("Error", "Failed to load coursework: " + e.getMessage());
        }
    }

    @FXML
    private void showGPA(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/StudentGPA.fxml"));
            Parent root = loader.load();

            StudentGPAController controller = loader.getController();
            controller.setStudent(currentStudent);

            // Wrap content in ScrollPane
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setContent(root);
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

            contentPane.getChildren().clear();
            contentPane.getChildren().add(scrollPane);

        } catch (IOException e) {
            showAlert("Error", "Failed to load GPA information: " + e.getMessage());
        }
    }

    @FXML
    private void showFeedback(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/StudentFeedback.fxml"));
            Parent root = loader.load();

            StudentFeedbackController controller = loader.getController();
            controller.setStudent(currentStudent);

            // Wrap content in ScrollPane
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setContent(root);
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

            contentPane.getChildren().clear();
            contentPane.getChildren().add(scrollPane);

        } catch (IOException e) {
            showAlert("Error", "Failed to load feedback: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}