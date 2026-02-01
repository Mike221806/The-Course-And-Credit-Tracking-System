package com.university.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.university.roles.Advisor;
import com.university.data.StudentDetails;
import com.university.data.CourseDetails;
import com.university.data.EnrollmentDetails;
import com.university.data.StudentSqlDetails;
import com.university.data.CourseSqlDetails;
import com.university.data.EnrollmentSqlDetails;
import com.university.roles.Student;
import com.university.courses.Enrollment;
import com.university.courses.Course;

import java.util.List;

/**
 * Controller for viewing student details.
 * Shows comprehensive student academic information.
 */
public class AdvisorStudentDetailsController {

    @FXML
    private ComboBox<Student> studentComboBox;

    @FXML
    private Button loadButton;

    @FXML
    private Label studentNameLabel;

    @FXML
    private Label studentProgramLabel;

    @FXML
    private Label studentIdLabel;

    @FXML
    private TableView<Enrollment> enrollmentsTable;

    @FXML
    private TableColumn<Enrollment, String> courseCodeColumn;

    @FXML
    private TableColumn<Enrollment, String> courseTitleColumn;

    @FXML
    private TableColumn<Enrollment, String> semesterColumn;

    @FXML
    private TableColumn<Enrollment, Integer> yearColumn;

    @FXML
    private TableColumn<Enrollment, String> gradeColumn;

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
        initializeTable();
        loadStudents();
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
        semesterColumn.setCellValueFactory(new PropertyValueFactory<>("semester"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        gradeColumn.setCellValueFactory(cellData -> {
            String grade = cellData.getValue().getFinalGrade();
            return javafx.beans.binding.Bindings.createStringBinding(() -> {
                return grade != null ? grade : "N/A";
            });
        });
    }

    private void loadStudents() {
        try {
            System.out.println("Loading students for advisor details...");
            List<Student> students = studentDetails.getAllStudents();
            System.out.println("Found " + students.size() + " students");

            studentComboBox.getItems().clear();
            studentComboBox.getItems().addAll(students);

            if (!students.isEmpty()) {
                studentComboBox.setValue(students.get(0));
            }
        } catch (Exception e) {
            System.err.println("Error loading students: " + e.getMessage());
            e.printStackTrace();
            statusLabel.setText("Error loading students: " + e.getMessage());
        }
    }

    @FXML
    private void handleLoad(ActionEvent event) {
        Student selectedStudent = studentComboBox.getValue();
        if (selectedStudent == null) {
            statusLabel.setText("Please select a student");
            return;
        }

        loadStudentDetails(selectedStudent);
    }

    private void loadStudentDetails(Student student) {
        try {
            // Update student info labels
            studentNameLabel.setText(student.getName());
            studentProgramLabel.setText(student.getProgram());
            studentIdLabel.setText(String.valueOf(student.getId()));

            // Load enrollments
            List<Enrollment> enrollments = enrollmentDetails.getEnrollmentsByStudentId(student.getId());
            enrollmentsTable.getItems().clear();
            enrollmentsTable.getItems().addAll(enrollments);

            statusLabel.setText("Loaded details for " + student.getName());

        } catch (Exception e) {
            statusLabel.setText("Error loading student details: " + e.getMessage());
        }
    }

    @FXML
    private void handleRefresh(ActionEvent event) {
        Student selectedStudent = studentComboBox.getValue();
        if (selectedStudent == null) {
            statusLabel.setText("Please select a student");
            return;
        }

        loadStudentDetails(selectedStudent);
    }
}