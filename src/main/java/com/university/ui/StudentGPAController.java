package com.university.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.GridPane;
import com.university.roles.Student;
import com.university.services.CourseService;
import com.university.services.GPAService;
import com.university.courses.Enrollment;
import com.university.data.EnrollmentSqlDetails;
import com.university.data.StudentSqlDetails;
import com.university.data.CourseSqlDetails;
import com.university.ui.components.CircularGPAIndicator;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

/**
 * Controller for the Student GPA view.
 * Displays GPA information and academic progress.
 */
public class StudentGPAController {

    @FXML
    private Label cgpaLabel;

    @FXML
    private Label completedCreditsLabel;

    @FXML
    private Label requiredCreditsLabel;

    @FXML
    private Label remainingCreditsLabel;

    @FXML
    private Label standingLabel;

    @FXML
    private Label eligibilityLabel;

    @FXML
    private StackPane circularGPAContainer;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label progressLabel;

    @FXML
    private GridPane semesterGpaGrid;

    @FXML
    private TableColumn<SemesterGPAItem, String> semesterColumn;

    @FXML
    private TableColumn<SemesterGPAItem, Integer> yearColumn;

    @FXML
    private TableColumn<SemesterGPAItem, Double> semesterGpaColumn;

    @FXML
    private TableColumn<SemesterGPAItem, Integer> semesterCreditsColumn;

    @FXML
    private Button refreshButton;

    @FXML
    private Label statusLabel;

    private Student currentStudent;
    private GPAService gpaService;
    private CourseService courseService;
    private CircularGPAIndicator circularGPAIndicator;

    public void setStudent(Student student) {
        this.currentStudent = student;
        this.gpaService = new GPAService(new EnrollmentSqlDetails(), new StudentSqlDetails(), new CourseSqlDetails());
        this.courseService = new CourseService(new CourseSqlDetails(), new EnrollmentSqlDetails());

        // Initialize circular GPA indicator
        circularGPAIndicator = new CircularGPAIndicator();
        circularGPAContainer.getChildren().add(circularGPAIndicator);

        initializeTable();
        updateGPAInfo();
    }

    private void initializeTable() {
        // No test data - let loadSemesterGPA handle real data
        System.out.println("DEBUG: GridPane initialized, waiting for real data");
    }

    @FXML
    private void handleRefresh(ActionEvent event) {
        updateGPAInfo();
    }

    private void updateGPAInfo() {
        if (currentStudent == null) return;

        try {
            // Calculate CGPA
            double cgpa = gpaService.calculateCGPA(currentStudent.getId());
            cgpaLabel.setText(String.format("CGPA: %.2f", cgpa));

            // Update circular GPA indicator
            circularGPAIndicator.setGPA(cgpa);

            // Calculate completed credits
            int completedCredits = gpaService.calculateCompletedCredits(currentStudent.getId());
            completedCreditsLabel.setText(String.valueOf(completedCredits));

            // Required credits
            requiredCreditsLabel.setText(String.valueOf(currentStudent.getRequiredCredits()));

            // Credits remaining
            int remainingCredits = currentStudent.getRequiredCredits() - completedCredits;
            remainingCreditsLabel.setText(String.valueOf(remainingCredits));

            // Academic standing
            boolean onProbation = gpaService.isOnAcademicProbation(currentStudent.getId());
            standingLabel.setText(onProbation ? "⚠️ ACADEMIC PROBATION" : "✅ GOOD STANDING");
            standingLabel.getStyleClass().clear();
            standingLabel.getStyleClass().add("standing-label");
            standingLabel.getStyleClass().add(onProbation ? "warning-text" : "success-text");

            // Graduation eligibility
            boolean eligible = gpaService.isEligibleForGraduation(currentStudent.getId());
            eligibilityLabel.setText(eligible ? "🎓 ELIGIBLE FOR GRADUATION" : "📚 NOT ELIGIBLE YET");
            eligibilityLabel.getStyleClass().clear();
            eligibilityLabel.getStyleClass().add("eligibility-label");
            eligibilityLabel.getStyleClass().add(eligible ? "success-text" : "warning-text");

            // Progress bar
            double progress = (double) completedCredits / currentStudent.getRequiredCredits();
            progressBar.setProgress(progress);
            progressLabel.setText(String.format("%.1f%% Complete", progress * 100));

            // Load semester GPA data
            loadSemesterGPA();

            statusLabel.setText("GPA information updated successfully");
            statusLabel.getStyleClass().clear();
            statusLabel.getStyleClass().add("status-label");
            statusLabel.getStyleClass().add("success-text");

        } catch (Exception e) {
            statusLabel.setText("Error updating GPA information: " + e.getMessage());
            statusLabel.getStyleClass().clear();
            statusLabel.getStyleClass().add("status-label");
            statusLabel.getStyleClass().add("error-text");
        }
    }

    private void loadSemesterGPA() {
        try {
            System.out.println("DEBUG: Loading semester GPA for student ID: " + currentStudent.getId());
            List<Enrollment> enrollments = courseService.getStudentEnrollments(currentStudent.getId());
            System.out.println("DEBUG: Found " + enrollments.size() + " enrollments for student");

            // Group enrollments by semester and year
            Set<String> uniqueSemesters = new HashSet<>();
            List<SemesterGPAItem> semesterGPAItems = new ArrayList<>();

            for (Enrollment enrollment : enrollments) {
                System.out.println("DEBUG: Processing enrollment: " + enrollment.getCourseCode() +
                        " in " + enrollment.getSemester() + " " + enrollment.getYear() +
                        " with grade: " + enrollment.getFinalGrade());

                String semesterKey = enrollment.getSemester() + "_" + enrollment.getYear();
                if (!uniqueSemesters.contains(semesterKey)) {
                    uniqueSemesters.add(semesterKey);

                    // Calculate semester GPA for ALL enrollments in this semester (graded or not)
                    double semesterGPA = gpaService.calculateSemesterGPA(
                            currentStudent.getId(), enrollment.getSemester(), enrollment.getYear());
                    int semesterCredits = calculateSemesterCredits(enrollments,
                            enrollment.getSemester(), enrollment.getYear());

                    System.out.println("DEBUG: Semester " + enrollment.getSemester() + " " + enrollment.getYear() +
                            " - GPA: " + semesterGPA + ", Credits: " + semesterCredits);

                    // Only add to list if there are enrollments for this semester (graded or not)
                    if (semesterCredits > 0) {
                        semesterGPAItems.add(new SemesterGPAItem(
                                enrollment.getSemester(),
                                enrollment.getYear(),
                                semesterGPA,
                                semesterCredits
                        ));
                    }
                }
            }

            System.out.println("DEBUG: Total semester GPA items to display: " + semesterGPAItems.size());

            // Sort by year and semester
            semesterGPAItems.sort((a, b) -> {
                if (!a.getYear().equals(b.getYear())) {
                    return b.getYear().compareTo(a.getYear());
                }
                return b.getSemester().compareTo(a.getSemester());
            });

            // Clear GridPane except header row (row 0)
            semesterGpaGrid.getChildren().removeIf(node -> GridPane.getRowIndex(node) > 0);

            // Add data to GridPane
            for (int i = 0; i < semesterGPAItems.size(); i++) {
                SemesterGPAItem item = semesterGPAItems.get(i);
                int row = i + 1; // Start from row 1 (row 0 is header)

                semesterGpaGrid.add(new Label(item.getSemester()), 0, row);
                semesterGpaGrid.add(new Label(item.getYear().toString()), 1, row);
                semesterGpaGrid.add(new Label(String.format("%.2f", item.getGpa())), 2, row);
                semesterGpaGrid.add(new Label(item.getCredits().toString()), 3, row);
            }

            System.out.println("DEBUG: Added " + semesterGPAItems.size() + " rows to GridPane");

        } catch (Exception e) {
            System.err.println("Error loading semester GPA: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private int calculateSemesterCredits(List<Enrollment> enrollments, String semester, int year) {
        int credits = 0;
        for (Enrollment enrollment : enrollments) {
            if (enrollment.getSemester().equals(semester) && enrollment.getYear() == year) {
                // Get actual course credits from course service
                try {
                    var course = courseService.getCourseByCode(enrollment.getCourseCode());
                    if (course.isPresent()) {
                        credits += course.get().getCreditHours();
                    } else {
                        System.err.println("Course not found: " + enrollment.getCourseCode());
                        credits += 3; // Fallback to 3 if course not found
                    }
                } catch (Exception e) {
                    System.err.println("Error getting credits for course " + enrollment.getCourseCode() + ": " + e.getMessage());
                    credits += 3; // Fallback to 3 if error
                }
            }
        }
        return credits;
    }

    /**
     * Display class for semester GPA items.
     */
    public static class SemesterGPAItem {
        private final javafx.beans.property.StringProperty semester;
        private final javafx.beans.property.IntegerProperty year;
        private final javafx.beans.property.DoubleProperty gpa;
        private final javafx.beans.property.IntegerProperty credits;

        public SemesterGPAItem(String semester, Integer year, Double gpa, Integer credits) {
            this.semester = new javafx.beans.property.SimpleStringProperty(semester);
            this.year = new javafx.beans.property.SimpleIntegerProperty(year);
            this.gpa = new javafx.beans.property.SimpleDoubleProperty(gpa);
            this.credits = new javafx.beans.property.SimpleIntegerProperty(credits);
        }

        public String getSemester() { return semester.get(); }
        public javafx.beans.property.StringProperty semesterProperty() { return semester; }

        public Integer getYear() { return year.get(); }
        public javafx.beans.property.IntegerProperty yearProperty() { return year; }

        public Double getGpa() { return gpa.get(); }
        public javafx.beans.property.DoubleProperty gpaProperty() { return gpa; }

        public Integer getCredits() { return credits.get(); }
        public javafx.beans.property.IntegerProperty creditsProperty() { return credits; }
    }
}