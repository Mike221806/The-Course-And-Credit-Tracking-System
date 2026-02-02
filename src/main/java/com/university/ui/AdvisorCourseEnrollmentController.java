package com.university.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.university.roles.Advisor;
import com.university.roles.Student;
import com.university.courses.Course;
import com.university.courses.Enrollment;
import com.university.data.StudentDetails;
import com.university.data.CourseDetails;
import com.university.data.EnrollmentDetails;
import com.university.data.StudentSqlDetails;
import com.university.data.CourseSqlDetails;
import com.university.data.EnrollmentSqlDetails;

import java.util.List;
import java.util.Optional;

/**
 * Controller for managing course enrollments.
 * Allows advisors to enroll students in courses and manage existing enrollments.
 */
public class AdvisorCourseEnrollmentController {

    @FXML
    private ComboBox<Student> studentComboBox;

    @FXML
    private Button loadEnrollmentsButton;

    @FXML
    private ComboBox<Course> courseComboBox;

    @FXML
    private ComboBox<String> semesterComboBox;

    @FXML
    private TextField yearField;

    @FXML
    private Button enrollButton;

    @FXML
    private TableView<Enrollment> enrollmentsTable;

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
    private TableColumn<Enrollment, Void> actionsColumn;

    @FXML
    private Label statusLabel;

    private Advisor currentAdvisor;
    private StudentDetails studentDetails;
    private CourseDetails courseDetails;
    private EnrollmentDetails enrollmentDetails;

    public void setAdvisor(Advisor advisor) {
        this.currentAdvisor = advisor;
        this.studentDetails = new StudentSqlDetails();
        this.courseDetails = new CourseSqlDetails();
        this.enrollmentDetails = new EnrollmentSqlDetails();
        initializeControls();
        loadStudents();
        loadCourses();
        initializeSemesters();
        initializeTable();
    }

    private void initializeControls() {
        // Set current year as default
        yearField.setText(String.valueOf(java.time.Year.now().getValue()));
    }

    private void initializeSemesters() {
        semesterComboBox.getItems().addAll("Fall", "Spring", "Summer");
        if (!semesterComboBox.getItems().isEmpty()) {
            semesterComboBox.setValue("Fall");
        }
    }

    private void loadStudents() {
        try {
            List<Student> students = studentDetails.getAllStudents();
            studentComboBox.getItems().clear();
            studentComboBox.getItems().addAll(students);

            if (!students.isEmpty()) {
                studentComboBox.setValue(students.get(0));
            }
        } catch (Exception e) {
            statusLabel.setText("Error loading students: " + e.getMessage());
        }
    }

    private void loadCourses() {
        try {
            List<Course> courses = courseDetails.getAllCourses();
            courseComboBox.getItems().clear();
            courseComboBox.getItems().addAll(courses);

            if (!courses.isEmpty()) {
                courseComboBox.setValue(courses.get(0));
            }
        } catch (Exception e) {
            statusLabel.setText("Error loading courses: " + e.getMessage());
        }
    }

    private void initializeTable() {
        courseCodeColumn.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        courseTitleColumn.setCellValueFactory(cellData -> {
            String courseCode = cellData.getValue().getCourseCode();
            return javafx.beans.binding.Bindings.createStringBinding(() -> {
                return courseDetails.getCourseByCode(courseCode)
                        .map(Course::getTitle)
                        .orElse("Unknown Course");
            });
        });
        creditsColumn.setCellValueFactory(cellData -> {
            String courseCode = cellData.getValue().getCourseCode();
            return javafx.beans.binding.Bindings.createObjectBinding(() -> {
                return courseDetails.getCourseByCode(courseCode)
                        .map(Course::getCreditHours)
                        .orElse(0);
            });
        });
        semesterColumn.setCellValueFactory(new PropertyValueFactory<>("semester"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        gradeColumn.setCellValueFactory(new PropertyValueFactory<>("finalGrade"));

        // Add actions column with delete button
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Remove");

            {
                deleteButton.setOnAction(event -> {
                    Enrollment enrollment = getTableView().getItems().get(getIndex());
                    removeEnrollment(enrollment);
                });
                deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });
    }

    @FXML
    private void loadStudentEnrollments(ActionEvent event) {
        Student selectedStudent = studentComboBox.getValue();
        if (selectedStudent == null) {
            statusLabel.setText("Please select a student");
            return;
        }

        try {
            List<Enrollment> enrollments = enrollmentDetails.getEnrollmentsByStudentId(selectedStudent.getId());
            enrollmentsTable.getItems().clear();
            enrollmentsTable.getItems().addAll(enrollments);
            statusLabel.setText("Loaded " + enrollments.size() + " enrollments for " + selectedStudent.getName());
        } catch (Exception e) {
            statusLabel.setText("Error loading enrollments: " + e.getMessage());
        }
    }

    @FXML
    private void enrollStudent(ActionEvent event) {
        Student selectedStudent = studentComboBox.getValue();
        Course selectedCourse = courseComboBox.getValue();
        String selectedSemester = semesterComboBox.getValue();
        String yearText = yearField.getText();

        if (selectedStudent == null || selectedCourse == null ||
                selectedSemester == null || yearText.trim().isEmpty()) {
            statusLabel.setText("Please fill in all fields");
            return;
        }

        try {
            int year = Integer.parseInt(yearText.trim());

            // Check if student is already enrolled in this course
            List<Enrollment> existingEnrollments = enrollmentDetails.getEnrollmentsByStudentId(selectedStudent.getId());
            for (Enrollment enrollment : existingEnrollments) {
                if (enrollment.getCourseCode().equals(selectedCourse.getCourseCode()) &&
                        enrollment.getSemester().equals(selectedSemester) &&
                        enrollment.getYear() == year) {
                    statusLabel.setText("Student is already enrolled in this course for " + selectedSemester + " " + year);
                    return;
                }
            }

            // Create new enrollment
            Enrollment newEnrollment = new Enrollment();
            newEnrollment.setStudentId(selectedStudent.getId());
            newEnrollment.setCourseCode(selectedCourse.getCourseCode());
            newEnrollment.setSemester(selectedSemester);
            newEnrollment.setYear(year);
            newEnrollment.setFinalGrade(""); // No grade initially

            // Save enrollment
            if (enrollmentDetails.createEnrollment(newEnrollment)) {
                statusLabel.setText("Successfully enrolled " + selectedStudent.getName() + " in " +
                        selectedCourse.getCourseCode() + " - " + selectedCourse.getTitle());
                loadStudentEnrollments(null); // Refresh the table
            } else {
                statusLabel.setText("Failed to enroll student");
            }

        } catch (NumberFormatException e) {
            statusLabel.setText("Invalid year format");
        } catch (Exception e) {
            statusLabel.setText("Error enrolling student: " + e.getMessage());
        }
    }

    private void removeEnrollment(Enrollment enrollment) {
        try {
            if (enrollmentDetails.deleteEnrollment(enrollment.getEnrollmentId())) {
                statusLabel.setText("Removed enrollment for " + enrollment.getCourseCode());
                loadStudentEnrollments(null); // Refresh the table
            } else {
                statusLabel.setText("Failed to remove enrollment");
            }
        } catch (Exception e) {
            statusLabel.setText("Error removing enrollment: " + e.getMessage());
        }
    }
}