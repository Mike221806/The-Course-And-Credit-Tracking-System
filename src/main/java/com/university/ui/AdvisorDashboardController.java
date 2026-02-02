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
import javafx.application.Platform;
import javafx.scene.Node;
import com.university.roles.Advisor;
import com.university.roles.Student;
import com.university.services.ReportService;
import com.university.services.GPAService;
import com.university.data.StudentDetails;
import com.university.data.StudentSqlDetails;
import com.university.data.CourseSqlDetails;
import com.university.data.EnrollmentSqlDetails;
import com.university.data.CourseworkSqlDetails;

import java.io.IOException;
import java.util.List;

/**
 * Controller for the Advisor Dashboard.
 * Provides navigation to different advisor functions and displays advisor information.
 */
public class AdvisorDashboardController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label summaryLabel;

    @FXML
    private StackPane contentPane;

    private Advisor currentAdvisor;
    private ReportService reportService;
    private GPAService gpaService;
    private StudentDetails studentDetails;

    public void setAdvisor(Advisor advisor) {
        this.currentAdvisor = advisor;
        this.studentDetails = new StudentSqlDetails();
        this.gpaService = new GPAService(new EnrollmentSqlDetails(), new StudentSqlDetails(), new CourseSqlDetails());
        this.reportService = new ReportService(new StudentSqlDetails(), new CourseSqlDetails(),
                new EnrollmentSqlDetails(), new CourseworkSqlDetails(), gpaService);

        welcomeLabel.setText("Welcome, " + advisor.getName() + " (" + advisor.getDepartment() + ")");
        loadSummary();
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
    private void showSearchStudent(ActionEvent event) {
        try {
            System.out.println("Loading AdvisorSearchStudent.fxml...");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AdvisorSearchStudent.fxml"));
            Parent root = loader.load();

            System.out.println("Getting AdvisorSearchStudentController...");
            AdvisorSearchStudentController controller = loader.getController();

            System.out.println("Setting advisor data...");
            controller.setAdvisor(currentAdvisor);

            System.out.println("Adding to content pane...");
            contentPane.getChildren().clear();

            // Wrap content in ScrollPane
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setContent(root);
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

            contentPane.getChildren().add(scrollPane);

            System.out.println("Search student tab loaded successfully!");

        } catch (IOException e) {
            System.err.println("Error loading student search: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "Failed to load student search: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error loading student search: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "Unexpected error: " + e.getMessage());
        }
    }

    @FXML
    private void showStudentDetails(ActionEvent event) {
        try {
            System.out.println("Loading AdvisorStudentDetails.fxml...");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AdvisorStudentDetails.fxml"));
            Parent root = loader.load();

            System.out.println("Getting AdvisorStudentDetailsController...");
            AdvisorStudentDetailsController controller = loader.getController();

            System.out.println("Setting advisor data...");
            controller.setAdvisor(currentAdvisor);

            System.out.println("Adding to content pane...");
            contentPane.getChildren().clear();

            // Wrap content in ScrollPane
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setContent(root);
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

            contentPane.getChildren().add(scrollPane);

            System.out.println("Student details tab loaded successfully!");

        } catch (IOException e) {
            System.err.println("Error loading student details: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "Failed to load student details: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error loading student details: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "Unexpected error: " + e.getMessage());
        }
    }

    @FXML
    private void showUpdateGrades(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AdvisorUpdateGrades.fxml"));
            Parent root = loader.load();

            AdvisorUpdateGradesController controller = loader.getController();
            controller.setAdvisor(currentAdvisor);

            contentPane.getChildren().clear();

            // Wrap content in ScrollPane
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setContent(root);
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

            contentPane.getChildren().add(scrollPane);

        } catch (IOException e) {
            showAlert("Error", "Failed to load grade update: " + e.getMessage());
        }
    }

    @FXML
    private void generateStudentReport(ActionEvent event) {
        // Create a dialog to select a student
        Dialog<Student> dialog = new Dialog<>();
        dialog.setTitle("Generate Student Report");
        dialog.setHeaderText("Select a student to generate report for:");

        // Set the button types
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Create combo box for student selection
        ComboBox<Student> studentComboBox = new ComboBox<>();
        try {
            List<Student> students = studentDetails.getAllStudents();
            studentComboBox.getItems().addAll(students);
            if (!students.isEmpty()) {
                studentComboBox.setValue(students.get(0));
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to load students: " + e.getMessage());
            return;
        }

        // Enable/disable OK button based on selection
        Node okButton = dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(false);

        // Set the content
        dialog.getDialogPane().setContent(studentComboBox);

        // Request focus on the combo box by default
        Platform.runLater(studentComboBox::requestFocus);

        // Convert the result to a student when the OK button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return studentComboBox.getValue();
            }
            return null;
        });

        // Show the dialog and process the result
        dialog.showAndWait().ifPresent(student -> {
            if (student != null) {
                try {
                    String report = reportService.generateStudentReport(student.getId());

                    // Show report in a dialog
                    TextArea reportArea = new TextArea(report);
                    reportArea.setWrapText(true);
                    reportArea.setEditable(false);

                    ScrollPane scrollPane = new ScrollPane(reportArea);
                    scrollPane.setFitToWidth(true);
                    scrollPane.setFitToHeight(true);
                    scrollPane.setPrefViewportWidth(700);
                    scrollPane.setPrefViewportHeight(500);

                    Dialog<ButtonType> reportDialog = new Dialog<>();
                    reportDialog.setTitle("Academic Report - " + student.getName());
                    reportDialog.getDialogPane().setContent(scrollPane);
                    reportDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, new ButtonType("Export", ButtonBar.ButtonData.OTHER));

                    reportDialog.showAndWait().ifPresent(result -> {
                        if (result.getButtonData() == ButtonBar.ButtonData.OTHER) {
                            // Export report
                            String filename = "student_report_" + student.getId() + "_" + student.getName().replaceAll(" ", "_") + ".txt";
                            if (reportService.exportReportToText(report, filename)) {
                                showAlert("Success", "Report exported to " + filename);
                            } else {
                                showAlert("Error", "Failed to export report");
                            }
                        }
                    });

                } catch (Exception e) {
                    showAlert("Error", "Failed to generate report: " + e.getMessage());
                }
            }
        });
    }

    @FXML
    private void manageStudents(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AdvisorManageStudents.fxml"));
            Parent root = loader.load();

            AdvisorManageStudentsController controller = loader.getController();
            controller.setAdvisor(currentAdvisor);

            contentPane.getChildren().clear();

            // Wrap content in ScrollPane
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setContent(root);
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

            contentPane.getChildren().add(scrollPane);

        } catch (IOException e) {
            showAlert("Error", "Failed to load student management: " + e.getMessage());
        }
    }

    @FXML
    private void manageCourseEnrollment(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AdvisorCourseEnrollment.fxml"));
            Parent root = loader.load();

            AdvisorCourseEnrollmentController controller = loader.getController();
            controller.setAdvisor(currentAdvisor);

            contentPane.getChildren().clear();

            // Wrap content in ScrollPane
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setContent(root);
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

            contentPane.getChildren().add(scrollPane);

        } catch (IOException e) {
            showAlert("Error", "Failed to load course enrollment: " + e.getMessage());
        }
    }

    @FXML
    private void addFeedback(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AdvisorFeedback.fxml"));
            Parent root = loader.load();

            AdvisorFeedbackController controller = loader.getController();
            controller.setAdvisor(currentAdvisor);

            contentPane.getChildren().clear();

            // Wrap content in ScrollPane
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setContent(root);
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

            contentPane.getChildren().add(scrollPane);

        } catch (IOException e) {
            showAlert("Error", "Failed to load feedback form: " + e.getMessage());
        }
    }

    private void loadSummary() {
        try {
            // Load basic summary information
            StudentDetails studentDetails = new StudentSqlDetails();
            int totalStudents = studentDetails.getAllStudents().size();

            int onProbation = 0;
            int eligibleForGraduation = 0;

            for (var student : studentDetails.getAllStudents()) {
                if (gpaService.isOnAcademicProbation(student.getId())) {
                    onProbation++;
                }
                if (gpaService.isEligibleForGraduation(student.getId())) {
                    eligibleForGraduation++;
                }
            }

            summaryLabel.setText(String.format("Total Students: %d | On Probation: %d | Eligible for Graduation: %d",
                    totalStudents, onProbation, eligibleForGraduation));

        } catch (Exception e) {
            summaryLabel.setText("Error loading summary information");
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
