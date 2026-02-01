package com.university.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import com.university.services.AuthService;
import com.university.data.StudentSqlDetails;
import com.university.data.AdvisorSqlDetails;
import com.university.roles.Student;
import com.university.roles.Advisor;

import java.io.IOException;
import java.util.Optional;

/**
 * Controller for the Login page.
 * Handles user authentication and navigation to appropriate dashboards.
 */
public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    private Button loginButton;

    @FXML
    private Button clearButton;

    @FXML
    private Label messageLabel;

    private AuthService authService;

    public LoginController() {
        this.authService = new AuthService(new StudentSqlDetails(), new AdvisorSqlDetails());
    }

    @FXML
    public void initialize() {
        // Initialize role combo box
        roleComboBox.getItems().addAll("Student", "Advisor");
        roleComboBox.setValue("Student");

        // Set default message
        messageLabel.setText("");
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String role = roleComboBox.getValue();

        // Validate input
        if (username.isEmpty() || password.isEmpty()) {
            showMessage("Please enter username and password", "error");
            return;
        }

        try {
            if ("Student".equals(role)) {
                Optional<Student> studentOpt = authService.authenticateStudent(username, password);
                if (studentOpt.isPresent()) {
                    showMessage("Login successful!", "success");
                    openStudentDashboard(studentOpt.get());
                } else {
                    showMessage("Invalid student credentials", "error");
                }
            } else if ("Advisor".equals(role)) {
                Optional<Advisor> advisorOpt = authService.authenticateAdvisor(username, password);
                if (advisorOpt.isPresent()) {
                    showMessage("Login successful!", "success");
                    openAdvisorDashboard(advisorOpt.get());
                } else {
                    showMessage("Invalid advisor credentials", "error");
                }
            } else {
                showMessage("Please select a role", "error");
            }
        } catch (Exception e) {
            showMessage("Login error: " + e.getMessage(), "error");
        }
    }

    @FXML
    private void handleClear(ActionEvent event) {
        usernameField.clear();
        passwordField.clear();
        roleComboBox.setValue("Student");
        messageLabel.setText("");
    }

    private void openStudentDashboard(Student student) {
        try {
            System.out.println("Loading StudentDashboard.fxml...");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/StudentDashboard.fxml"));
            Parent root = loader.load();

            System.out.println("Getting controller...");
            StudentDashboardController controller = loader.getController();

            System.out.println("Setting student data...");
            controller.setStudent(student);

            System.out.println("Setting up stage...");
            Stage stage = (Stage) loginButton.getScene().getWindow();
            Scene scene = new Scene(root, 800, 600);
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

            stage.setTitle("Student Dashboard - " + student.getName());
            stage.setScene(scene);
            stage.setResizable(true);
            stage.show();

            System.out.println("Student dashboard opened successfully!");

        } catch (IOException e) {
            System.err.println("Error opening student dashboard: " + e.getMessage());
            e.printStackTrace();
            showMessage("Error opening student dashboard: " + e.getMessage(), "error");
        } catch (Exception e) {
            System.err.println("Unexpected error opening student dashboard: " + e.getMessage());
            e.printStackTrace();
            showMessage("Unexpected error: " + e.getMessage(), "error");
        }
    }

    private void openAdvisorDashboard(Advisor advisor) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AdvisorDashboard.fxml"));
            Parent root = loader.load();

            AdvisorDashboardController controller = loader.getController();
            controller.setAdvisor(advisor);

            Stage stage = (Stage) loginButton.getScene().getWindow();
            Scene scene = new Scene(root, 900, 700);
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

            stage.setTitle("Advisor Dashboard - " + advisor.getName());
            stage.setScene(scene);
            stage.setResizable(true);
            stage.show();

        } catch (IOException e) {
            showMessage("Error opening advisor dashboard: " + e.getMessage(), "error");
        }
    }

    private void showMessage(String message, String type) {
        messageLabel.setText(message);
        messageLabel.getStyleClass().clear();
        messageLabel.getStyleClass().add("message-label");
        messageLabel.getStyleClass().add(type + "-message");
    }
}