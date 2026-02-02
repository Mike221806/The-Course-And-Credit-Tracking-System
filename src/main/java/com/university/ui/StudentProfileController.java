package com.university.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import com.university.roles.Student;
import com.university.services.GPAService;
import com.university.data.StudentSqlDetails;
import com.university.data.CourseSqlDetails;
import com.university.data.EnrollmentSqlDetails;

/**
 * Controller for the Student Profile view.
 * Displays student information and academic progress.
 */
public class StudentProfileController {

    @FXML
    private Label studentIdLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label programLabel;

    @FXML
    private Label requiredCreditsLabel;

    @FXML
    private Label completedCreditsLabel;

    @FXML
    private Label remainingCreditsLabel;

    @FXML
    private Label graduationStatusLabel;

    @FXML
    private Button refreshButton;

    @FXML
    private Button changePasswordButton;

    private Student currentStudent;
    private GPAService gpaService;

    public void setStudent(Student student) {
        this.currentStudent = student;
        this.gpaService = new GPAService(new EnrollmentSqlDetails(), new StudentSqlDetails(), new CourseSqlDetails());
        updateProfile();
    }

    @FXML
    private void handleRefresh(ActionEvent event) {
        updateProfile();
    }

    @FXML
    private void handleChangePassword(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ChangePasswordDialog.fxml"));
            Parent root = loader.load();

            ChangePasswordController controller = loader.getController();
            controller.setStudent(currentStudent);

            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle("Change Password");
            dialog.setScene(new Scene(root, 400, 300));
            dialog.setResizable(false);

            // Show dialog and wait for it to close
            dialog.showAndWait();

            // Check if password was changed and show success message
            if (controller.isPasswordChanged()) {
                // Show success message in the main window
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Password Changed");
                alert.setHeaderText(null);
                alert.setContentText("Your password has been successfully changed!");
                alert.showAndWait();
            }

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error opening password change dialog: " + e.getMessage());
            alert.showAndWait();
        }
    }

    private void updateProfile() {
        if (currentStudent == null) return;

        // Update basic information
        studentIdLabel.setText(String.valueOf(currentStudent.getId()));
        nameLabel.setText(currentStudent.getName());
        usernameLabel.setText(currentStudent.getUsername());
        programLabel.setText(currentStudent.getProgram());
        requiredCreditsLabel.setText(String.valueOf(currentStudent.getRequiredCredits()));

        // Calculate and update completed credits
        int completedCredits = gpaService.calculateCompletedCredits(currentStudent.getId());
        completedCreditsLabel.setText(String.valueOf(completedCredits));

        // Update remaining credits
        int remainingCredits = currentStudent.getRemainingCredits();
        remainingCreditsLabel.setText(String.valueOf(remainingCredits));

        // Update graduation status
        boolean isEligible = gpaService.isEligibleForGraduation(currentStudent.getId());
        graduationStatusLabel.setText(isEligible ? "ELIGIBLE FOR GRADUATION" : "NOT ELIGIBLE");
        graduationStatusLabel.getStyleClass().clear();
        graduationStatusLabel.getStyleClass().add("field-value");
        graduationStatusLabel.getStyleClass().add(isEligible ? "success-text" : "warning-text");

        // Update student's completed credits in database
        gpaService.updateStudentCompletedCredits(currentStudent.getId());
    }
}
