package com.university.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.university.roles.Advisor;
import com.university.roles.Student;
import com.university.data.StudentDetails;
import com.university.data.StudentSqlDetails;
import com.university.services.PasswordUtil;

import java.util.List;

/**
 * Controller for managing students.
 * Allows advisors to add new students with temporary passwords and view existing students.
 */
public class AdvisorManageStudentsController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField programField;

    @FXML
    private TextField requiredCreditsField;

    @FXML
    private PasswordField tempPasswordField;

    @FXML
    private Button generatePasswordButton;

    @FXML
    private Label generatedPasswordLabel;

    @FXML
    private Button addStudentButton;

    @FXML
    private TableView<Student> studentsTable;

    @FXML
    private TableColumn<Student, Integer> idColumn;

    @FXML
    private TableColumn<Student, String> nameColumn;

    @FXML
    private TableColumn<Student, String> usernameColumn;

    @FXML
    private TableColumn<Student, String> programColumn;

    @FXML
    private TableColumn<Student, Integer> creditsColumn;

    @FXML
    private TableColumn<Student, Void> actionsColumn;

    @FXML
    private Label statusLabel;

    private StudentDetails studentDetails;
    private String generatedPassword;

    public void setAdvisor(Advisor advisor) {
        // Advisor reference not currently used, but kept for future functionality
        this.studentDetails = new StudentSqlDetails();
        initializeTable();
        loadStudents();
    }

    private void initializeTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        programColumn.setCellValueFactory(new PropertyValueFactory<>("program"));
        creditsColumn.setCellValueFactory(new PropertyValueFactory<>("completedCredits"));

        // Add actions column with reset password button
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button resetPasswordButton = new Button("Reset Password");

            {
                resetPasswordButton.setOnAction(event -> {
                    Student student = getTableView().getItems().get(getIndex());
                    resetStudentPassword(student);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(resetPasswordButton);
                }
            }
        });
    }

    private void loadStudents() {
        try {
            List<Student> students = studentDetails.getAllStudents();
            studentsTable.getItems().clear();
            studentsTable.getItems().addAll(students);
            statusLabel.setText("Loaded " + students.size() + " students");
        } catch (Exception e) {
            statusLabel.setText("Error loading students: " + e.getMessage());
        }
    }

    @FXML
    private void generateTempPassword(ActionEvent event) {
        generatedPassword = PasswordUtil.generateRandomPassword(12);
        tempPasswordField.setText(generatedPassword);
        generatedPasswordLabel.setText("Generated: " + generatedPassword);
        generatedPasswordLabel.setStyle("-fx-text-fill: #27ae60;");
    }

    @FXML
    private void addStudent(ActionEvent event) {
        try {
            // Validate input
            if (nameField.getText().trim().isEmpty() ||
                    usernameField.getText().trim().isEmpty() ||
                    programField.getText().trim().isEmpty() ||
                    requiredCreditsField.getText().trim().isEmpty() ||
                    tempPasswordField.getText().trim().isEmpty()) {

                statusLabel.setText("Please fill in all fields");
                return;
            }

            String name = nameField.getText().trim();
            String username = usernameField.getText().trim();
            String program = programField.getText().trim();
            int requiredCredits = Integer.parseInt(requiredCreditsField.getText().trim());
            String tempPassword = tempPasswordField.getText().trim();

            // Create new student
            Student newStudent = new Student();
            // Don't set ID - let database auto-generate it
            newStudent.setName(name);
            newStudent.setUsername(username);
            // Hash the password using the same method as login system
            newStudent.setPasswordHash(PasswordUtil.hashPassword(tempPassword));
            newStudent.setProgram(program);
            newStudent.setRequiredCredits(requiredCredits);
            newStudent.setCompletedCredits(0);

            // Save student to database
            if (studentDetails.createStudent(newStudent)) {
                statusLabel.setText("Student added successfully! Username: " + username + ", Temp Password: " + tempPassword);
                clearForm();
                loadStudents(); // Refresh the table
            } else {
                statusLabel.setText("Failed to add student. Username may already exist.");
            }

        } catch (NumberFormatException e) {
            statusLabel.setText("Invalid number format for Required Credits");
        } catch (Exception e) {
            statusLabel.setText("Error adding student: " + e.getMessage());
        }
    }

    private void resetStudentPassword(Student student) {
        try {
            String newPassword = PasswordUtil.generateRandomPassword(12);
            String hashedPassword = PasswordUtil.hashPassword(newPassword);

            if (studentDetails.updatePassword(student.getId(), hashedPassword)) {
                statusLabel.setText("Password reset for " + student.getName() + ". New password: " + newPassword);
            } else {
                statusLabel.setText("Failed to reset password for " + student.getName());
            }
        } catch (Exception e) {
            statusLabel.setText("Error resetting password: " + e.getMessage());
        }
    }

    private void clearForm() {
        nameField.clear();
        usernameField.clear();
        programField.clear();
        requiredCreditsField.clear();
        tempPasswordField.clear();
        generatedPasswordLabel.setText("");
    }
}