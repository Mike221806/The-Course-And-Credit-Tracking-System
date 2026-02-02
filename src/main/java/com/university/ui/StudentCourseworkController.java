package com.university.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.university.roles.Student;
import com.university.services.CourseService;
import com.university.services.CourseworkService;
import com.university.courses.Enrollment;
import com.university.courses.CourseworkItem;
import com.university.courses.CourseworkGrade;
import com.university.courses.Assignment;
import com.university.courses.MidtermExam;
import com.university.courses.FinalExam;
import com.university.data.CourseSqlDetails;
import com.university.data.EnrollmentSqlDetails;
import com.university.data.CourseworkSqlDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for the Student Coursework view.
 * Displays coursework items and grades for selected courses.
 */
public class StudentCourseworkController {

    @FXML
    private ComboBox<String> courseComboBox;

    @FXML
    private Button loadButton;

    @FXML
    private TabPane courseworkTabPane;

    @FXML
    private TableView<CourseworkDisplayItem> assignmentsTable;

    @FXML
    private TableColumn<CourseworkDisplayItem, String> assignmentTitleColumn;

    @FXML
    private TableColumn<CourseworkDisplayItem, String> assignmentDueDateColumn;

    @FXML
    private TableColumn<CourseworkDisplayItem, Double> assignmentTotalMarksColumn;

    @FXML
    private TableColumn<CourseworkDisplayItem, Double> assignmentWeightColumn;

    @FXML
    private TableColumn<CourseworkDisplayItem, String> assignmentStatusColumn;

    @FXML
    private TableColumn<CourseworkDisplayItem, String> assignmentGradeColumn;

    @FXML
    private TableView<CourseworkDisplayItem> midExamsTable;

    @FXML
    private TableColumn<CourseworkDisplayItem, String> midExamTitleColumn;

    @FXML
    private TableColumn<CourseworkDisplayItem, String> midExamDateColumn;

    @FXML
    private TableColumn<CourseworkDisplayItem, Double> midExamTotalMarksColumn;

    @FXML
    private TableColumn<CourseworkDisplayItem, Double> midExamWeightColumn;

    @FXML
    private TableColumn<CourseworkDisplayItem, String> midExamStatusColumn;

    @FXML
    private TableColumn<CourseworkDisplayItem, String> midExamGradeColumn;

    @FXML
    private TableView<CourseworkDisplayItem> finalExamsTable;

    @FXML
    private TableColumn<CourseworkDisplayItem, String> finalExamTitleColumn;

    @FXML
    private TableColumn<CourseworkDisplayItem, String> finalExamDateColumn;

    @FXML
    private TableColumn<CourseworkDisplayItem, Double> finalExamTotalMarksColumn;

    @FXML
    private TableColumn<CourseworkDisplayItem, Double> finalExamWeightColumn;

    @FXML
    private TableColumn<CourseworkDisplayItem, String> finalExamStatusColumn;

    @FXML
    private TableColumn<CourseworkDisplayItem, String> finalExamGradeColumn;

    @FXML
    private TableView<CourseworkDisplayItem> allCourseworkTable;

    @FXML
    private TableColumn<CourseworkDisplayItem, String> courseworkTypeColumn;

    @FXML
    private TableColumn<CourseworkDisplayItem, String> courseworkTitleColumn;

    @FXML
    private TableColumn<CourseworkDisplayItem, String> courseworkDueDateColumn;

    @FXML
    private TableColumn<CourseworkDisplayItem, Double> courseworkTotalMarksColumn;

    @FXML
    private TableColumn<CourseworkDisplayItem, Double> courseworkWeightColumn;

    @FXML
    private TableColumn<CourseworkDisplayItem, String> courseworkStatusColumn;

    @FXML
    private TableColumn<CourseworkDisplayItem, String> courseworkGradeColumn;

    @FXML
    private Label overallGradeLabel;

    @FXML
    private Label totalItemsLabel;

    @FXML
    private Button refreshButton;

    @FXML
    private Label statusLabel;

    private Student currentStudent;
    private CourseService courseService;
    private CourseworkService courseworkService;

    public void setStudent(Student student) {
        this.currentStudent = student;
        this.courseService = new CourseService(new CourseSqlDetails(), new EnrollmentSqlDetails());
        this.courseworkService = new CourseworkService(new CourseworkSqlDetails(), new CourseSqlDetails());
        initializeTable();
        loadCourses();
    }

    private void initializeTable() {
        // Initialize Assignments table
        assignmentTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        assignmentDueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        assignmentTotalMarksColumn.setCellValueFactory(new PropertyValueFactory<>("totalMarks"));
        assignmentWeightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));
        assignmentStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        assignmentGradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));

        // Initialize Mid-Exams table
        midExamTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        midExamDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        midExamTotalMarksColumn.setCellValueFactory(new PropertyValueFactory<>("totalMarks"));
        midExamWeightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));
        midExamStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        midExamGradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));

        // Initialize Final Exams table
        finalExamTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        finalExamDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        finalExamTotalMarksColumn.setCellValueFactory(new PropertyValueFactory<>("totalMarks"));
        finalExamWeightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));
        finalExamStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        finalExamGradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));

        // Initialize All Coursework table
        courseworkTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        courseworkTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        courseworkDueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        courseworkTotalMarksColumn.setCellValueFactory(new PropertyValueFactory<>("totalMarks"));
        courseworkWeightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));
        courseworkStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        courseworkGradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));
    }

    @FXML
    private void handleLoadCoursework(ActionEvent event) {
        String selectedCourse = courseComboBox.getValue();
        if (selectedCourse == null) {
            statusLabel.setText("Please select a course");
            statusLabel.getStyleClass().clear();
            statusLabel.getStyleClass().add("status-label");
            statusLabel.getStyleClass().add("warning-text");
            return;
        }

        loadCoursework(selectedCourse);
    }

    @FXML
    private void handleRefreshCourses(ActionEvent event) {
        loadCourses();
    }

    private void loadCourses() {
        if (currentStudent == null) return;

        try {
            List<Enrollment> enrollments = courseService.getStudentEnrollments(currentStudent.getId());
            courseComboBox.getItems().clear();

            for (Enrollment enrollment : enrollments) {
                courseComboBox.getItems().add(enrollment.getCourseCode());
            }

            if (!courseComboBox.getItems().isEmpty()) {
                courseComboBox.setValue(courseComboBox.getItems().get(0));
                loadCoursework(courseComboBox.getValue());
            }

        } catch (Exception e) {
            statusLabel.setText("Error loading courses: " + e.getMessage());
            statusLabel.getStyleClass().clear();
            statusLabel.getStyleClass().add("status-label");
            statusLabel.getStyleClass().add("error-text");
        }
    }

    private void loadCoursework(String courseCode) {
        try {
            List<CourseworkItem> items = courseworkService.getCourseworkItemsByCourse(courseCode);
            List<CourseworkGrade> grades = courseworkService.getCourseworkGradesByStudentAndCourse(currentStudent.getId(), courseCode);

            // Clear all tables
            assignmentsTable.getItems().clear();
            midExamsTable.getItems().clear();
            finalExamsTable.getItems().clear();
            allCourseworkTable.getItems().clear();

            List<CourseworkDisplayItem> allDisplayItems = new ArrayList<>();

            for (CourseworkItem item : items) {
                CourseworkDisplayItem displayItem = new CourseworkDisplayItem(item);

                // Find corresponding grade
                for (CourseworkGrade grade : grades) {
                    if (grade.getItemId() == item.getItemId()) {
                        displayItem.setMarksObtained(grade.getMarksObtained());
                        displayItem.setPercentage(grade.calculatePercentage(item.getTotalMarks()));
                        break;
                    }
                }

                // Add to appropriate table based on type
                if (item instanceof Assignment) {
                    assignmentsTable.getItems().add(displayItem);
                } else if (item instanceof MidtermExam) {
                    midExamsTable.getItems().add(displayItem);
                } else if (item instanceof FinalExam) {
                    finalExamsTable.getItems().add(displayItem);
                }

                // Add to all coursework table
                allDisplayItems.add(displayItem);
            }

            allCourseworkTable.getItems().addAll(allDisplayItems);

            // Update summary - show overall course grade
            double overallPercentage = courseworkService.calculateOverallCourseworkGrade(currentStudent.getId(), courseCode);
            String overallLetterGrade = calculateLetterGrade(overallPercentage);
            overallGradeLabel.setText(String.format("Course Grade: %.1f%% (%s)", overallPercentage, overallLetterGrade));
            totalItemsLabel.setText("Total Items: " + items.size());

            statusLabel.setText("Loaded coursework for " + courseCode);
            statusLabel.getStyleClass().clear();
            statusLabel.getStyleClass().add("status-label");
            statusLabel.getStyleClass().add("success-text");

        } catch (Exception e) {
            statusLabel.setText("Error loading coursework: " + e.getMessage());
            statusLabel.getStyleClass().clear();
            statusLabel.getStyleClass().add("status-label");
            statusLabel.getStyleClass().add("error-text");
        }
    }

    private String calculateLetterGrade(double percentage) {
        // Convert percentage to letter grade (updated to be less generous)
        if (percentage >= 90) return "A+";
        if (percentage >= 85) return "A";
        if (percentage >= 80) return "A-";
        if (percentage >= 75) return "B+";
        if (percentage >= 70) return "B";
        if (percentage >= 65) return "B-";
        if (percentage >= 60) return "C+";
        if (percentage >= 55) return "C";
        if (percentage >= 50) return "C-";
        if (percentage >= 45) return "D";
        return "F";
    }

    /**
     * Display class for coursework items in the table.
     */
    public static class CourseworkDisplayItem {
        private final String title;
        private final String type;
        private final double totalMarks;
        private final double weight;
        private final String dueDate;
        private double marksObtained;
        private double percentage;
        private String status;
        private String grade;

        public CourseworkDisplayItem(CourseworkItem item) {
            this.title = item.getTitle();
            this.type = item.getType();
            this.totalMarks = item.getTotalMarks();
            this.weight = item.getWeight();
            this.dueDate = item.getDueDate().toString();
            this.marksObtained = 0.0;
            this.percentage = 0.0;
            this.status = "Pending";
            this.grade = "N/A";
        }

        // Getters for JavaFX properties
        public String getTitle() { return title; }
        public String getType() { return type; }
        public double getTotalMarks() { return totalMarks; }
        public double getWeight() { return weight; }
        public double getMarksObtained() { return marksObtained; }
        public double getPercentage() { return percentage; }
        public String getDueDate() { return dueDate; }
        public String getStatus() { return status; }
        public String getGrade() { return grade; }

        public void setMarksObtained(double marksObtained) {
            this.marksObtained = marksObtained;
            updateStatusAndGrade();
        }

        public void setPercentage(double percentage) {
            this.percentage = percentage;
            updateStatusAndGrade();
        }

        private void updateStatusAndGrade() {
            if (marksObtained > 0) {
                this.status = "Completed";
                // Don't calculate individual grade - show marks instead
                this.grade = String.format("%.1f/%.0f", marksObtained, totalMarks);
            } else {
                this.status = "Pending";
                this.grade = "Not graded";
            }
        }

        private String calculateLetterGrade(double percentage) {
            // Convert percentage to letter grade (updated to be less generous)
            if (percentage >= 90) return "A+";
            if (percentage >= 85) return "A";
            if (percentage >= 80) return "A-";
            if (percentage >= 75) return "B+";
            if (percentage >= 70) return "B";
            if (percentage >= 65) return "B-";
            if (percentage >= 60) return "C+";
            if (percentage >= 55) return "C";
            if (percentage >= 50) return "C-";
            if (percentage >= 45) return "D";
            return "F";
        }
    }
}