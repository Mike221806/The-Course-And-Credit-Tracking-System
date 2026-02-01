package com.university.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.university.roles.Advisor;
import com.university.data.StudentDetails;
import com.university.data.StudentSqlDetails;
import com.university.roles.Student;

import java.util.List;

/**
 * Controller for searching students.
 * Allows advisors to find and select students.
 */
public class AdvisorSearchStudentController {

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<String> searchTypeComboBox;

    @FXML
    private Button searchButton;

    @FXML
    private TableView<Student> studentsTable;

    @FXML
    private TableColumn<Student, Integer> studentIdColumn;

    @FXML
    private TableColumn<Student, String> nameColumn;

    @FXML
    private TableColumn<Student, String> programColumn;

    @FXML
    private TableColumn<Student, String> usernameColumn;

    @FXML
    private Label statusLabel;

    private Advisor currentAdvisor;
    private StudentDetails studentDetails;

    public void setAdvisor(Advisor advisor) {
        this.currentAdvisor = advisor;
        this.studentDetails = new StudentSqlDetails();
        initializeTable();
        loadAllStudents();
    }

    private void initializeTable() {
        studentIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        programColumn.setCellValueFactory(new PropertyValueFactory<>("program"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        searchTypeComboBox.getItems().addAll("Name", "Program", "Username");
        searchTypeComboBox.setValue("Name");
    }

    @FXML
    private void handleSearch(ActionEvent event) {
        String searchText = searchField.getText().trim();
        String searchType = searchTypeComboBox.getValue();

        if (searchText.isEmpty()) {
            loadAllStudents();
            return;
        }

        try {
            List<Student> allStudents = studentDetails.getAllStudents();
            List<Student> filteredStudents = allStudents.stream()
                    .filter(student -> {
                        switch (searchType) {
                            case "Name":
                                return student.getName().toLowerCase().contains(searchText.toLowerCase());
                            case "Program":
                                return student.getProgram().toLowerCase().contains(searchText.toLowerCase());
                            case "Username":
                                return student.getUsername().toLowerCase().contains(searchText.toLowerCase());
                            default:
                                return false;
                        }
                    })
                    .toList();

            studentsTable.getItems().clear();
            studentsTable.getItems().addAll(filteredStudents);

            statusLabel.setText("Found " + filteredStudents.size() + " student(s)");

        } catch (Exception e) {
            statusLabel.setText("Error searching students: " + e.getMessage());
        }
    }

    @FXML
    private void handleLoadAll(ActionEvent event) {
        loadAllStudents();
    }

    private void loadAllStudents() {
        try {
            System.out.println("Loading all students...");
            List<Student> students = studentDetails.getAllStudents();
            System.out.println("Found " + students.size() + " students");

            studentsTable.getItems().clear();
            studentsTable.getItems().addAll(students);
            statusLabel.setText("Loaded " + students.size() + " student(s)");

        } catch (Exception e) {
            System.err.println("Error loading students: " + e.getMessage());
            e.printStackTrace();
            statusLabel.setText("Error loading students: " + e.getMessage());
        }
    }
}