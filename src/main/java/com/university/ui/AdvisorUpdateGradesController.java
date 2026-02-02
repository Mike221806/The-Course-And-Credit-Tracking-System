package com.university.ui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DoubleStringConverter;
import com.university.data.StudentDetails;
import com.university.data.CourseDetails;
import com.university.data.EnrollmentDetails;
import com.university.data.CourseworkDetails;
import com.university.data.StudentSqlDetails;
import com.university.data.CourseSqlDetails;
import com.university.data.EnrollmentSqlDetails;
import com.university.data.CourseworkSqlDetails;
import com.university.roles.Student;
import com.university.roles.Advisor;
import com.university.courses.Enrollment;
import com.university.courses.Course;
import com.university.courses.CourseworkGrade;
import com.university.courses.CourseworkItem;
import com.university.services.GPAService;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller for updating student grades.
 * Allows advisors to modify course and coursework grades.
 */
public class AdvisorUpdateGradesController {

    @FXML
    private ComboBox<Student> studentComboBox;

    @FXML
    private TabPane gradeTabPane;

    @FXML
    private TableView<Enrollment> courseGradesTable;

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
    private TableView<CourseworkGradeItem> courseworkGradesTable;

    @FXML
    private TableColumn<CourseworkGradeItem, String> courseCodeCourseworkColumn;

    @FXML
    private TableColumn<CourseworkGradeItem, String> itemTitleColumn;

    @FXML
    private TableColumn<CourseworkGradeItem, String> itemTypeColumn;

    @FXML
    private TableColumn<CourseworkGradeItem, Double> marksColumn;

    @FXML
    private TableColumn<CourseworkGradeItem, Double> totalMarksColumn;

    @FXML
    private Button loadButton;

    @FXML
    private Button saveButton;

    @FXML
    private Label statusLabel;

    private Advisor currentAdvisor;
    private StudentDetails studentDetails;
    private CourseDetails courseDetails;
    private EnrollmentDetails enrollmentDetails;
    private CourseworkDetails courseworkDetails;
    private GPAService gpaService;

    public void setAdvisor(Advisor advisor) {
        this.currentAdvisor = advisor;
        this.studentDetails = new StudentSqlDetails();
        this.courseDetails = new CourseSqlDetails();
        this.enrollmentDetails = new EnrollmentSqlDetails();
        this.courseworkDetails = new CourseworkSqlDetails();
        this.gpaService = new GPAService(enrollmentDetails, studentDetails, courseDetails);
        initializeTables();
        loadStudents();
    }

    private void initializeTables() {
        // Course grades table (read-only - calculated from coursework)
        courseGradesTable.setEditable(false);
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
        gradeColumn.setCellValueFactory(new PropertyValueFactory<>("finalGrade"));
        // Remove editing capability for course grades - they will be calculated

        // Coursework grades table
        courseworkGradesTable.setEditable(true);

        courseCodeCourseworkColumn.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        itemTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        itemTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        marksColumn.setCellValueFactory(new PropertyValueFactory<>("marksObtained"));
        marksColumn.setCellFactory(TextFieldTableCell.forTableColumn(new javafx.util.converter.DoubleStringConverter()));

        marksColumn.setOnEditCommit(event -> {
            CourseworkGradeItem item = event.getRowValue();
            double newMarks = event.getNewValue();

            // Validate that marks don't exceed total marks and are not negative
            if (newMarks < 0) {
                showAlert("Invalid Input", "Marks cannot be negative. Please enter a value between 0 and " + item.getTotalMarks());
                return;
            }

            if (newMarks > item.getTotalMarks()) {
                showAlert("Invalid Input", "Marks cannot exceed the total possible marks (" + item.getTotalMarks() + "). Please enter a value between 0 and " + item.getTotalMarks());
                return;
            }

            // Update both the CourseworkGradeItem and the underlying CourseworkGrade
            item.setMarksObtained(newMarks);

            // Update the original grade object in memory
            if (item.getOriginalGrade() != null) {
                item.getOriginalGrade().setMarksObtained(newMarks);
            } else {
                // If there's no original grade, create one
                CourseworkGrade newGrade = new CourseworkGrade();
                newGrade.setStudentId(studentComboBox.getValue().getId());
                newGrade.setItemId(item.getItemId());
                newGrade.setCourseCode(item.getCourseCode());
                newGrade.setMarksObtained(newMarks);
                item.setOriginalGrade(newGrade);
            }

            // Update status but don't save immediately
            statusLabel.setText("Updated " + item.getTitle() + " (" + newMarks + "/" + item.getTotalMarks() + ") - Click Save to persist changes");
        });
        totalMarksColumn.setCellValueFactory(new PropertyValueFactory<>("totalMarks"));
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

    @FXML
    private void handleLoad(ActionEvent event) {
        Student selectedStudent = studentComboBox.getValue();
        if (selectedStudent == null) {
            statusLabel.setText("Please select a student");
            return;
        }

        loadGrades(selectedStudent);
    }

    private void recalculateCourseGrade(String courseCode) {
        try {
            // Find the enrollment for the current student and course
            Student selectedStudent = studentComboBox.getValue();
            if (selectedStudent == null) return;

            List<Enrollment> enrollments = enrollmentDetails.getEnrollmentsByStudentId(selectedStudent.getId());
            for (Enrollment enrollment : enrollments) {
                if (enrollment.getCourseCode().equals(courseCode)) {
                    // Get coursework grades for this course
                    List<CourseworkGrade> courseworkGrades = courseworkDetails.getCourseworkGradesByStudentAndCourse(
                            selectedStudent.getId(), courseCode);

                    // Calculate new grade
                    String calculatedGrade = calculateFinalGrade(courseworkGrades);

                    // Update enrollment
                    enrollment.setFinalGrade(calculatedGrade);
                    enrollmentDetails.updateFinalGrade(enrollment.getEnrollmentId(), calculatedGrade);

                    // Refresh the course grades table
                    courseGradesTable.refresh();

                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("Error recalculating grade for " + courseCode + ": " + e.getMessage());
        }
    }

    @FXML
    private void handleSave(ActionEvent event) {
        Student selectedStudent = studentComboBox.getValue();
        if (selectedStudent == null) return;

        try {
            // Save all coursework grades to database (both existing and new)
            for (CourseworkGradeItem item : courseworkGradesTable.getItems()) {
                CourseworkGrade grade = item.getOriginalGrade();
                if (grade != null) {
                    if (grade.getGradeId() == 0) {
                        // This is a new grade, create it in database
                        courseworkDetails.createCourseworkGrade(grade);
                    } else {
                        // This is an existing grade, update it
                        courseworkDetails.updateCourseworkGrade(grade);
                    }
                }
            }

            // Recalculate all course grades to ensure they're up to date
            List<Enrollment> enrollments = enrollmentDetails.getEnrollmentsByStudentId(selectedStudent.getId());
            updateCourseGradesFromCoursework(enrollments);

            // Recalculate GPA and update student's completed credits
            double newCGPA = gpaService.calculateCGPA(selectedStudent.getId());
            gpaService.updateStudentCompletedCredits(selectedStudent.getId());

            // Refresh the course grades table to show updated grades
            courseGradesTable.getItems().clear();
            courseGradesTable.getItems().addAll(enrollments);

            statusLabel.setText(String.format("All grades saved and calculated! New CGPA for %s: %.2f",
                    selectedStudent.getName(), newCGPA));

        } catch (Exception e) {
            statusLabel.setText("Error saving grades: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadGrades(Student student) {
        try {
            // Load course grades
            List<Enrollment> enrollments = enrollmentDetails.getEnrollmentsByStudentId(student.getId());
            courseGradesTable.getItems().clear();
            courseGradesTable.getItems().addAll(enrollments);

            // Load all coursework items for each enrolled course and create grade records if needed
            List<CourseworkGradeItem> allCourseworkItems = new ArrayList<>();

            for (Enrollment enrollment : enrollments) {
                String courseCode = enrollment.getCourseCode();

                // Get all coursework items for this course
                List<CourseworkItem> courseItems = courseworkDetails.getCourseworkItemsByCourse(courseCode);

                // Get existing grades for this student in this course
                List<CourseworkGrade> existingGrades = courseworkDetails.getCourseworkGradesByStudentAndCourse(student.getId(), courseCode);
                System.out.println("Found " + existingGrades.size() + " existing grades for student " + student.getId() + " in course " + courseCode);

                // Create a map of existing grades by item ID for quick lookup
                Map<Integer, CourseworkGrade> existingGradeMap = existingGrades.stream()
                        .collect(Collectors.toMap(CourseworkGrade::getItemId, grade -> grade));

                System.out.println("Coursework items for " + courseCode + ":");
                for (CourseworkItem item : courseItems) {
                    System.out.println("  Item ID: " + item.getItemId() + ", Title: " + item.getTitle());
                }

                System.out.println("Existing grades map:");
                existingGradeMap.forEach((itemId, grade) -> {
                    System.out.println("  Item ID: " + itemId + ", Marks: " + grade.getMarksObtained());
                });

                // For each coursework item, either use existing grade or create a new one
                for (CourseworkItem item : courseItems) {
                    CourseworkGrade grade = existingGradeMap.get(item.getItemId());

                    if (grade == null) {
                        System.out.println("No existing grade found for item ID " + item.getItemId() + ", creating new grade with 0.0");
                        // Create a new grade record in memory only (don't save to database yet)
                        grade = new CourseworkGrade();
                        grade.setStudentId(student.getId());
                        grade.setItemId(item.getItemId());
                        grade.setCourseCode(courseCode);
                        grade.setMarksObtained(0.0); // Default to 0
                        // Don't save to database here - only save when user clicks Save button
                    } else {
                        System.out.println("Found existing grade for item ID " + item.getItemId() + ": " + grade.getMarksObtained());
                    }

                    // Create the display item
                    CourseworkGradeItem displayItem = createCourseworkGradeItem(grade);
                    allCourseworkItems.add(displayItem);
                }
            }

            courseworkGradesTable.getItems().clear();
            courseworkGradesTable.getItems().addAll(allCourseworkItems);

            statusLabel.setText("Loaded " + allCourseworkItems.size() + " coursework items for " + student.getName());

        } catch (Exception e) {
            statusLabel.setText("Error loading grades: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateCourseGradesFromCoursework(List<Enrollment> enrollments) {
        for (Enrollment enrollment : enrollments) {
            try {
                // Get all coursework grades for this student in this course
                List<CourseworkGrade> courseworkGrades = courseworkDetails.getCourseworkGradesByStudentAndCourse(
                        enrollment.getStudentId(), enrollment.getCourseCode());

                // Calculate final grade based on coursework marks
                String calculatedGrade = calculateFinalGrade(courseworkGrades);

                // Update the enrollment with the calculated grade
                enrollment.setFinalGrade(calculatedGrade);
                enrollmentDetails.updateFinalGrade(enrollment.getEnrollmentId(), calculatedGrade);

            } catch (Exception e) {
                System.err.println("Error calculating grade for " + enrollment.getCourseCode() + ": " + e.getMessage());
            }
        }

        // Refresh the course grades table to show updated calculated grades
        courseGradesTable.refresh();
    }

    private String calculateFinalGrade(List<CourseworkGrade> courseworkGrades) {
        if (courseworkGrades.isEmpty()) {
            return "IP"; // In Progress
        }

        double totalMarks = 0.0;
        double totalPossible = 0.0;

        for (CourseworkGrade grade : courseworkGrades) {
            // Get the coursework item to find total marks
            try {
                var courseworkItem = courseworkDetails.getCourseworkItemById(grade.getItemId());
                if (courseworkItem.isPresent()) {
                    CourseworkItem item = courseworkItem.get();
                    totalMarks += grade.getMarksObtained();
                    totalPossible += item.getTotalMarks();
                }
            } catch (Exception e) {
                System.err.println("Error getting coursework item " + grade.getItemId() + ": " + e.getMessage());
            }
        }

        if (totalPossible == 0) {
            return "IP"; // In Progress
        }

        double percentage = (totalMarks / totalPossible) * 100;

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

    private CourseworkGradeItem createCourseworkGradeItem(CourseworkGrade grade) {
        CourseworkGradeItem item = new CourseworkGradeItem();
        item.setOriginalGrade(grade);
        item.setMarksObtained(grade.getMarksObtained());
        item.setCourseCode(grade.getCourseCode());
        item.setItemId(grade.getItemId());

        // Get coursework item details
        try {
            Optional<CourseworkItem> courseworkItemOpt = courseworkDetails.getCourseworkItemById(grade.getItemId());
            if (courseworkItemOpt.isPresent()) {
                CourseworkItem courseworkItem = courseworkItemOpt.get();
                item.setTitle(courseworkItem.getTitle());
                item.setType(courseworkItem.getType());
                item.setTotalMarks(courseworkItem.getTotalMarks());
            }
        } catch (Exception e) {
            System.err.println("Error loading coursework item details: " + e.getMessage());
            item.setTitle("Unknown");
            item.setType("Unknown");
            item.setTotalMarks(100);
        }

        return item;
    }

    /**
     * Shows an alert dialog to the user.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Display class for coursework grades.
     */
    public static class CourseworkGradeItem {
        private CourseworkGrade originalGrade;
        private String courseCode;
        private String title;
        private String type;
        private double marksObtained;
        private double totalMarks;
        private int itemId;

        // Getters for JavaFX properties
        public String getCourseCode() { return courseCode; }
        public String getTitle() { return title; }
        public String getType() { return type; }
        public Double getMarksObtained() { return marksObtained; }
        public Double getTotalMarks() { return totalMarks; }
        public int getItemId() { return itemId; }

        // Setters
        public void setOriginalGrade(CourseworkGrade originalGrade) { this.originalGrade = originalGrade; }
        public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
        public void setTitle(String title) { this.title = title; }
        public void setType(String type) { this.type = type; }
        public void setItemId(int itemId) { this.itemId = itemId; }
        public void setMarksObtained(double marksObtained) {
            this.marksObtained = marksObtained;
            if (originalGrade != null) {
                originalGrade.setMarksObtained(marksObtained);
            }
        }
        public void setTotalMarks(double totalMarks) { this.totalMarks = totalMarks; }

        public CourseworkGrade getOriginalGrade() { return originalGrade; }
    }
}