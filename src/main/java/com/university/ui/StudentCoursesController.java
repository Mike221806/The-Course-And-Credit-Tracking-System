package com.university.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.university.roles.Student;
import com.university.services.CourseService;
import com.university.courses.Enrollment;
import com.university.data.CourseSqlDetails;
import com.university.data.EnrollmentSqlDetails;

import java.util.List;
import java.util.ArrayList;

/**
 * Controller for the Student Courses view.
 * Displays all courses the student is enrolled in with grades.
 */
public class StudentCoursesController {

    @FXML
    private TableView<Enrollment> coursesTable;

    @FXML
    private TableColumn<Enrollment, String> courseCodeColumn;

    @FXML
    private TableColumn<Enrollment, String> courseTitleColumn;

    @FXML
    private TableColumn<Enrollment, Integer> creditsColumn;

    @FXML
    private TableColumn<Enrollment, String> semesterColumn;

    @FXML
    private TableColumn<Enrollment, Integer> yearColumn;

    @FXML
    private TableColumn<Enrollment, String> gradeColumn;

    @FXML
    private Button refreshButton;

    @FXML
    private Label statusLabel;

    private Student currentStudent;
    private CourseService courseService;

    public void setStudent(Student student) {
        System.out.println("DEBUG: StudentCoursesController.setStudent() called with: " + student);
        this.currentStudent = student;
        this.courseService = new CourseService(new CourseSqlDetails(), new EnrollmentSqlDetails());

        // Initialize table with simple test data first
        initializeTable();

        // Load real data
        loadCourses();
    }

    private void initializeTable() {
        System.out.println("DEBUG: Initializing table columns");

        // Simple PropertyValueFactory bindings
        courseCodeColumn.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        courseTitleColumn.setCellValueFactory(new PropertyValueFactory<>("courseTitle"));
        creditsColumn.setCellValueFactory(new PropertyValueFactory<>("credits"));
        semesterColumn.setCellValueFactory(new PropertyValueFactory<>("semester"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        gradeColumn.setCellValueFactory(new PropertyValueFactory<>("finalGrade"));

        System.out.println("DEBUG: Table columns initialized");
    }

    @FXML
    private void handleRefresh(ActionEvent event) {
        System.out.println("DEBUG: Refresh button clicked");
        loadCourses();
    }

    private void loadCourses() {
        System.out.println("DEBUG: loadCourses() called");

        if (currentStudent == null) {
            System.out.println("DEBUG: currentStudent is null");
            statusLabel.setText("No student selected");
            return;
        }

        try {
            System.out.println("DEBUG: Loading courses for student ID: " + currentStudent.getId());

            // First try to load from database
            List<Enrollment> enrollments = courseService.getStudentEnrollments(currentStudent.getId());
            System.out.println("DEBUG: Found " + enrollments.size() + " enrollments from database");

            // If no enrollments found, create test data
            if (enrollments.isEmpty()) {
                System.out.println("DEBUG: No enrollments found, creating test data");
                enrollments = createTestData();
                System.out.println("DEBUG: Created " + enrollments.size() + " test enrollments");
            }

            // Debug print each enrollment
            for (int i = 0; i < enrollments.size(); i++) {
                Enrollment e = enrollments.get(i);
                System.out.println("DEBUG: Enrollment " + i + ": " + e.getCourseCode() +
                        " - " + e.getCourseTitle() + " - " + e.getCredits() + " credits");
            }

            // Clear and populate table
            coursesTable.getItems().clear();
            coursesTable.getItems().addAll(enrollments);
            System.out.println("DEBUG: Added " + enrollments.size() + " items to table");
            System.out.println("DEBUG: Table item count: " + coursesTable.getItems().size());

            statusLabel.setText("Loaded " + enrollments.size() + " course(s)");
            statusLabel.getStyleClass().clear();
            statusLabel.getStyleClass().add("status-label");
            statusLabel.getStyleClass().add("success-text");

        } catch (Exception e) {
            System.out.println("DEBUG: ERROR loading courses: " + e.getMessage());
            e.printStackTrace();
            statusLabel.setText("Error loading courses: " + e.getMessage());
            statusLabel.getStyleClass().clear();
            statusLabel.getStyleClass().add("status-label");
            statusLabel.getStyleClass().add("error-text");
        }
    }

    private List<Enrollment> createTestData() {
        List<Enrollment> testData = new ArrayList<>();

        // Create test enrollments with all required properties
        testData.add(new Enrollment(1, currentStudent.getId(), "CS101", "Introduction to Computer Science", 3, "Fall", 2023, "A"));
        testData.add(new Enrollment(2, currentStudent.getId(), "CS201", "Data Structures", 4, "Fall", 2023, "B"));
        testData.add(new Enrollment(3, currentStudent.getId(), "MATH101", "Calculus I", 4, "Fall", 2023, "A-"));
        testData.add(new Enrollment(4, currentStudent.getId(), "ENG101", "English Composition", 3, "Fall", 2023, "B+"));

        return testData;
    }
}