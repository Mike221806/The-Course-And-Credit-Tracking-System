package com.university.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.university.roles.Advisor;
import com.university.data.StudentDetails;
import com.university.data.StudentSqlDetails;
import com.university.roles.Student;
import com.university.services.GPAService;
import com.university.data.EnrollmentSqlDetails;
import com.university.data.CourseSqlDetails;

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
    private Button loadAllButton;

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
    private TableColumn<Student, String> cgpaColumn;

    @FXML
    private Label statusLabel;

    private Advisor currentAdvisor;
    private StudentDetails studentDetails;
    private GPAService gpaService;

    public void setAdvisor(Advisor advisor) {
        this.currentAdvisor = advisor;
        this.studentDetails = new StudentSqlDetails();
        this.gpaService = new GPAService(new EnrollmentSqlDetails(), new StudentSqlDetails(), new CourseSqlDetails());
        initializeTable();
        loadAllStudents();
    }

    private void initializeTable() {
        System.out.println("DEBUG: Initializing table columns...");

        studentIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        programColumn.setCellValueFactory(new PropertyValueFactory<>("program"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        // Custom cell factory for CGPA column
        cgpaColumn.setCellValueFactory(param -> {
            Student student = param.getValue();
            double cgpa = gpaService.calculateCGPA(student.getId());
            return javafx.beans.binding.Bindings.createStringBinding(() ->
                    String.format("%.2f", cgpa));
        });

        System.out.println("DEBUG: Table columns initialized");

        searchTypeComboBox.getItems().addAll("Name", "Program", "Username");
        searchTypeComboBox.setValue("Name");

        System.out.println("DEBUG: Search combo box initialized");
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
            System.out.println("DEBUG: Loading all students...");
            List<Student> students = studentDetails.getAllStudents();
            System.out.println("DEBUG: Found " + students.size() + " students");

            // Debug print each student
            for (int i = 0; i < students.size(); i++) {
                Student s = students.get(i);
                System.out.println("DEBUG: Student " + i + ": ID=" + s.getId() +
                        ", Name=" + s.getName() + ", Program=" + s.getProgram() +
                        ", Username=" + s.getUsername());
            }

            studentsTable.getItems().clear();
            studentsTable.getItems().addAll(students);
            System.out.println("DEBUG: Added " + students.size() + " students to table");
            System.out.println("DEBUG: Table item count: " + studentsTable.getItems().size());

            statusLabel.setText("Loaded " + students.size() + " student(s)");

        } catch (Exception e) {
            System.err.println("Error loading students: " + e.getMessage());
            e.printStackTrace();
            statusLabel.setText("Error loading students: " + e.getMessage());
        }
    }
}