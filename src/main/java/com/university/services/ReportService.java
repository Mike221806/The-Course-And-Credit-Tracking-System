package com.university.services;

import com.university.data.StudentDetails;
import com.university.data.CourseDetails;
import com.university.data.EnrollmentDetails;
import com.university.data.CourseworkDetails;
import com.university.roles.Student;
import com.university.courses.Course;
import com.university.courses.Enrollment;
import com.university.courses.CourseworkItem;
import com.university.courses.CourseworkGrade;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * Service for generating academic reports.
 * Demonstrates Single Responsibility Principle (SRP) by focusing on report generation.
 * Follows Dependency Inversion Principle (DIP) by depending on interfaces.
 */
public class ReportService {

    private final StudentDetails studentDetails;
    private final CourseDetails courseDetails;
    private final EnrollmentDetails enrollmentDetails;
    private final CourseworkDetails courseworkDetails;
    private final GPAService gpaService;

    public ReportService(StudentDetails studentDetails, CourseDetails courseDetails,
                         EnrollmentDetails enrollmentDetails, CourseworkDetails courseworkDetails,
                         GPAService gpaService) {
        this.studentDetails = studentDetails;
        this.courseDetails = courseDetails;
        this.enrollmentDetails = enrollmentDetails;
        this.courseworkDetails = courseworkDetails;
        this.gpaService = gpaService;
    }

    /**
     * Generates a comprehensive academic report for a student.
     * @param studentId the student ID
     * @return formatted report string
     */
    public String generateStudentReport(int studentId) {
        Optional<Student> studentOpt = studentDetails.getStudentById(studentId);
        if (studentOpt.isEmpty()) {
            return "Student not found";
        }

        Student student = studentOpt.get();
        StringBuilder report = new StringBuilder();

        // Header
        report.append("=".repeat(80)).append("\n");
        report.append("ACADEMIC REPORT\n");
        report.append("=".repeat(80)).append("\n\n");

        // Student Profile
        report.append("STUDENT PROFILE\n");
        report.append("-".repeat(40)).append("\n");
        report.append(String.format("Name: %s\n", student.getName()));
        report.append(String.format("Student ID: %d\n", student.getId()));
        report.append(String.format("Program: %s\n", student.getProgram()));
        report.append(String.format("Username: %s\n", student.getUsername()));
        report.append("\n");

        // Academic Progress
        report.append("ACADEMIC PROGRESS\n");
        report.append("-".repeat(40)).append("\n");
        int completedCredits = gpaService.calculateCompletedCredits(studentId);
        report.append(String.format("Completed Credits: %d\n", completedCredits));
        report.append(String.format("Required Credits: %d\n", student.getRequiredCredits()));
        report.append(String.format("Remaining Credits: %d\n", student.getRemainingCredits()));
        report.append(String.format("CGPA: %.2f\n", gpaService.calculateCGPA(studentId)));
        report.append(String.format("Academic Standing: %s\n",
                gpaService.isOnAcademicProbation(studentId) ? "PROBATION" : "GOOD STANDING"));
        report.append(String.format("Graduation Eligibility: %s\n",
                gpaService.isEligibleForGraduation(studentId) ? "ELIGIBLE" : "NOT ELIGIBLE"));
        report.append("\n");

        // Course Enrollments
        report.append("COURSE ENROLLMENTS\n");
        report.append("-".repeat(40)).append("\n");
        List<Enrollment> enrollments = enrollmentDetails.getEnrollmentsByStudentId(studentId);

        if (enrollments.isEmpty()) {
            report.append("No course enrollments found.\n");
        } else {
            report.append(String.format("%-10s %-30s %-10s %-6s %-10s\n",
                    "Code", "Title", "Semester", "Year", "Grade"));
            report.append("-".repeat(80)).append("\n");

            for (Enrollment enrollment : enrollments) {
                Optional<Course> courseOpt = courseDetails.getCourseByCode(enrollment.getCourseCode());
                if (courseOpt.isPresent()) {
                    Course course = courseOpt.get();
                    report.append(String.format("%-10s %-30s %-10s %-6d %-10s\n",
                            course.getCourseCode(),
                            course.getTitle().length() > 28 ? course.getTitle().substring(0, 27) + "..." : course.getTitle(),
                            enrollment.getSemester(),
                            enrollment.getYear(),
                            enrollment.getFinalGrade() != null ? enrollment.getFinalGrade() : "N/A"));
                }
            }
        }
        report.append("\n");

        // Coursework Details
        report.append("COURSEWORK DETAILS\n");
        report.append("-".repeat(40)).append("\n");

        for (Enrollment enrollment : enrollments) {
            List<CourseworkGrade> grades = courseworkDetails.getCourseworkGradesByStudentAndCourse(
                    studentId, enrollment.getCourseCode());
            List<CourseworkItem> items = courseworkDetails.getCourseworkItemsByCourse(enrollment.getCourseCode());

            if (!grades.isEmpty()) {
                report.append(String.format("\n%s - %s\n", enrollment.getCourseCode(),
                        courseDetails.getCourseByCode(enrollment.getCourseCode()).map(Course::getTitle).orElse("")));
                report.append(String.format("%-30s %-10s %-10s %-10s\n",
                        "Item", "Type", "Marks", "Percentage"));
                report.append("-".repeat(70)).append("\n");

                for (CourseworkGrade grade : grades) {
                    Optional<CourseworkItem> itemOpt = items.stream()
                            .filter(item -> item.getItemId() == grade.getItemId())
                            .findFirst();

                    if (itemOpt.isPresent()) {
                        CourseworkItem item = itemOpt.get();
                        double percentage = grade.calculatePercentage(item.getTotalMarks());
                        report.append(String.format("%-30s %-10s %-10.1f %-10.1f%%\n",
                                item.getTitle().length() > 28 ? item.getTitle().substring(0, 27) + "..." : item.getTitle(),
                                item.getType(),
                                grade.getMarksObtained(),
                                percentage));
                    }
                }
            }
        }

        // Footer
        report.append("\n").append("=".repeat(80)).append("\n");
        report.append("Report generated on: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n");
        report.append("=".repeat(80)).append("\n");

        return report.toString();
    }

    /**
     * Generates a summary report for an advisor showing all students.
     * @return formatted summary report string
     */
    public String generateAdvisorSummaryReport() {
        StringBuilder report = new StringBuilder();

        // Header
        report.append("=".repeat(80)).append("\n");
        report.append("ADVISOR SUMMARY REPORT\n");
        report.append("=".repeat(80)).append("\n\n");

        // Student Summary
        report.append("STUDENT SUMMARY\n");
        report.append("-".repeat(40)).append("\n");
        List<Student> students = studentDetails.getAllStudents();

        if (students.isEmpty()) {
            report.append("No students found.\n");
        } else {
            report.append(String.format("%-25s %-15s %-10s %-10s %-10s %-15s\n",
                    "Name", "Program", "Credits", "CGPA", "Status", "Graduation"));
            report.append("-".repeat(100)).append("\n");

            for (Student student : students) {
                int completedCredits = gpaService.calculateCompletedCredits(student.getId());
                double cgpa = gpaService.calculateCGPA(student.getId());
                String status = gpaService.isOnAcademicProbation(student.getId()) ? "PROBATION" : "GOOD";
                String graduation = gpaService.isEligibleForGraduation(student.getId()) ? "ELIGIBLE" : "NOT ELIGIBLE";

                report.append(String.format("%-25s %-15s %-10d %-10.2f %-10s %-15s\n",
                        student.getName().length() > 23 ? student.getName().substring(0, 22) + "..." : student.getName(),
                        student.getProgram().length() > 13 ? student.getProgram().substring(0, 12) + "..." : student.getProgram(),
                        completedCredits, cgpa, status, graduation));
            }
        }

        // Footer
        report.append("\n").append("=".repeat(80)).append("\n");
        report.append("Report generated on: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n");
        report.append("=".repeat(80)).append("\n");

        return report.toString();
    }

    /**
     * Exports a report to a text file format.
     * @param report the report content
     * @param filename the filename to save to
     * @return true if successful, false otherwise
     */
    public boolean exportReportToText(String report, String filename) {
        try {
            java.io.FileWriter writer = new java.io.FileWriter(filename);
            writer.write(report);
            writer.close();
            return true;
        } catch (Exception e) {
            System.err.println("Error exporting report: " + e.getMessage());
            return false;
        }
    }
}