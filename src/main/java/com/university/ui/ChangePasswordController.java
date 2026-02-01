package com.university.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.university.roles.Student;
import com.university.data.StudentDetails;
import com.university.data.StudentSqlDetails;
import com.university.services.PasswordUtil;

/**
 * Controller for the Change Password dialog.
 * Allows students to change their own password.
 */
public class ChangePasswordController {

    @FXML
    private PasswordField currentPasswordField;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label messageLabel;

    @FXML
    private Button changeButton;

    @FXML
    private Button cancelButton;

    private Student currentStudent;
    private StudentDetails studentDetails;
    private boolean passwordChanged = false;

    public ChangePasswordController() {
        this.studentDetails = new StudentSqlDetails();
    }

    public void setStudent(Student student) {
        this.currentStudent = student;
    }

    @FXML
    private void handleChangePassword(ActionEvent event) {
        String currentPassword = currentPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Validate input
        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showMessage("Please fill in all fields", "error");
            return;
        }

        // Validate current password
        if (!PasswordUtil.verifyPassword(currentPassword, currentStudent.getPasswordHash())) {
            showMessage("Current password is incorrect", "error");
            return;
        }

        // Validate new password
        if (newPassword.length() < 6) {
            showMessage("New password must be at least 6 characters long", "error");
            return;
        }

        // Validate password confirmation
        if (!newPassword.equals(confirmPassword)) {
            showMessage("New passwords do not match", "error");
            return;
        }

        // Check if new password is same as current
        if (PasswordUtil.verifyPassword(newPassword, currentStudent.getPasswordHash())) {
            showMessage("New password cannot be the same as current password", "error");
            return;
        }

        try {
            // Hash the new password
            String hashedNewPassword = PasswordUtil.hashPassword(newPassword);

            // Update the password in database
            if (studentDetails.updatePassword(currentStudent.getId(), hashedNewPassword)) {
                // Update the student object
                currentStudent.setPasswordHash(hashedNewPassword);
                passwordChanged = true;
                showMessage("Password changed successfully!", "success");

                // Close the dialog after a short delay
                javafx.application.Platform.runLater(() -> {
                    try {
                        Thread.sleep(1500);
                        closeDialog();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            } else {
                showMessage("Failed to change password. Please try again.", "error");
            }
        } catch (Exception e) {
            showMessage("Error changing password: " + e.getMessage(), "error");
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        closeDialog();
    }

    private void closeDialog() {
        // Get the stage (window) and close it
        if (changeButton.getScene() != null && changeButton.getScene().getWindow() != null) {
            javafx.stage.Stage stage = (javafx.stage.Stage) changeButton.getScene().getWindow();
            stage.close();
        }
    }

    private void showMessage(String message, String type) {
        messageLabel.setText(message);
        messageLabel.getStyleClass().clear();
        messageLabel.getStyleClass().add("message-label");
        messageLabel.getStyleClass().add(type + "-message");
    }

    public boolean isPasswordChanged() {
        return passwordChanged;
    }
}
