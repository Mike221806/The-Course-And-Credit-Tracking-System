package com.university.data;

import com.university.roles.Advisor;
import com.university.roles.Student;
import com.university.courses.*;
import com.university.services.PasswordUtil;

import java.util.Arrays;
import java.util.List;

/**
 * Database initialization class that creates sample data for the application.
 * Demonstrates proper data seeding and follows the DIP principle by using interfaces.
 */
public class DatabaseInitializer {

    private final StudentDetails studentDetails;
    private final AdvisorDetails advisorDetails;
    private final CourseDetails courseDetails;
    private final EnrollmentDetails enrollmentDetails;
    private final CourseworkDetails courseworkDetails;

    public DatabaseInitializer() {
        this.studentDetails = new StudentSqlDetails();
        this.advisorDetails = new AdvisorSqlDetails();
        this.courseDetails = new CourseSqlDetails();
        this.enrollmentDetails = new EnrollmentSqlDetails();
        this.courseworkDetails = new CourseworkSqlDetails();
    }

    /**
     * Initializes the database with sample data.
     * @return true if initialization was successful, false otherwise
     */
    public boolean initializeSampleData() {
        try {
            // Initialize database schema
            if (!DatabaseConnection.getInstance().initializeDatabase()) {
                return false;
            }

            // Create sample advisors
            createSampleAdvisors();

            // Create sample students
            createSampleStudents();

            // Create sample courses
            createSampleCourses();

            // Create sample coursework items
            createSampleCourseworkItems();

            // Create sample enrollments
            createSampleEnrollments();

            // Create sample coursework grades
            createSampleCourseworkGrades();

            return true;
        } catch (Exception e) {
            System.err.println("Error initializing sample data: " + e.getMessage());
            return false;
        }
    }

    /**
     * Creates sample advisors.
     */
    private void createSampleAdvisors() {
        List<Advisor> advisors = Arrays.asList(
                new Advisor(0, "Mr. Abel Tadesse", "Abel T", PasswordUtil.hashPassword("abel1234"), "Software Engineering", "abel.t@university.edu"),
                new Advisor(0, "Prof. Michael Sheleme", "Michael S", PasswordUtil.hashPassword("michael1234"), "Software Engineering", "michael.s@university.edu")
        );

        for (Advisor advisor : advisors) {
            advisorDetails.createAdvisor(advisor);
        }
    }

    /**
     * Creates sample students.
     */
    private void createSampleStudents() {
        List<Student> students = Arrays.asList(
                new Student(0, "Alice Smith", "asmith", PasswordUtil.hashPassword("password123"), "Computer Science", 120, 45),
                new Student(0, "Bob Johnson", "bjohnson", PasswordUtil.hashPassword("password123"), "Computer Science", 120, 30),
                new Student(0, "Carol Williams", "cwilliams", PasswordUtil.hashPassword("password123"), "Engineering", 130, 60),
                new Student(0, "Biruk A", "biruka", PasswordUtil.hashPassword("biruk1234"), "Computer Science", 120, 35)
        );

        for (Student student : students) {
            studentDetails.createStudent(student);
        }
    }

    /**
     * Creates sample courses.
     */
    private void createSampleCourses() {
        List<Course> courses = Arrays.asList(
                new Course("CS101", "Introduction to Programming", 3),
                new Course("CS201", "Data Structures", 4),
                new Course("CS301", "Algorithms", 4),
                new Course("CS401", "Software Engineering", 3),
                new Course("MATH101", "Calculus I", 4),
                new Course("ENG101", "English Composition", 3)
        );

        for (Course course : courses) {
            courseDetails.createCourse(course);
        }
    }

    /**
     * Creates sample coursework items using the new CourseworkService.
     * Ensures each course has standard coursework items (assignment, mid-exam, final exam).
     */
    private void createSampleCourseworkItems() {
        System.out.println("Creating sample coursework items...");

        // Use the CourseworkInitializer to create standard coursework for all courses
        CourseworkInitializer courseworkInitializer = new CourseworkInitializer();
        courseworkInitializer.initializeCourseworkForAllCourses();

        System.out.println("Sample coursework items creation complete.");
    }

    /**
     * Creates sample enrollments.
     */
    private void createSampleEnrollments() {
        List<Enrollment> enrollments = Arrays.asList(
                // Alice Smith enrollments (student ID will be 1 after auto-increment)
                new Enrollment(0, 1, "CS101", "Fall", 2023, "A"),
                new Enrollment(0, 1, "CS201", "Fall", 2023, "B"),
                new Enrollment(0, 1, "MATH101", "Fall", 2023, "A"),
                new Enrollment(0, 1, "ENG101", "Fall", 2023, null), // No grade yet

                // Bob Johnson enrollments (student ID will be 2 after auto-increment)
                new Enrollment(0, 2, "CS101", "Fall", 2023, "C"),
                new Enrollment(0, 2, "ENG101", "Fall", 2023, "B"),
                new Enrollment(0, 2, "CS301", "Fall", 2023, null), // No grade yet

                // Carol Williams enrollments (student ID will be 3 after auto-increment)
                new Enrollment(0, 3, "CS201", "Fall", 2023, "A"),
                new Enrollment(0, 3, "CS301", "Fall", 2023, "B"),
                new Enrollment(0, 3, "MATH101", "Fall", 2023, "A"),
                new Enrollment(0, 3, "CS401", "Fall", 2023, null) // No grade yet
        );

        for (Enrollment enrollment : enrollments) {
            enrollmentDetails.createEnrollment(enrollment);
        }
    }

    /**
     * Creates sample coursework grades with realistic marks for the new marking scheme.
     * Assignment: out of 20, Mid-Exam: out of 30, Final Exam: out of 50
     */
    private void createSampleCourseworkGrades() {
        List<CourseworkGrade> grades = Arrays.asList(
                // Alice Smith grades for CS101 (good student)
                new CourseworkGrade(0, 1, "CS101", 1, 18),  // Assignment: 18/20 (90%)
                new CourseworkGrade(0, 1, "CS101", 2, 27),  // Mid-Exam: 27/30 (90%)
                new CourseworkGrade(0, 1, "CS101", 3, 45),  // Final: 45/50 (90%)

                // Alice Smith grades for CS201 (good student)
                new CourseworkGrade(0, 1, "CS201", 4, 17),  // Assignment: 17/20 (85%)
                new CourseworkGrade(0, 1, "CS201", 5, 26),  // Mid-Exam: 26/30 (87%)
                new CourseworkGrade(0, 1, "CS201", 6, 43),  // Final: 43/50 (86%)

                // Bob Johnson grades for CS101 (average student)
                new CourseworkGrade(0, 2, "CS101", 7, 15),  // Assignment: 15/20 (75%)
                new CourseworkGrade(0, 2, "CS101", 8, 22),  // Mid-Exam: 22/30 (73%)
                new CourseworkGrade(0, 2, "CS101", 9, 35),  // Final: 35/50 (70%)

                // Carol Williams grades for MATH101 (excellent student)
                new CourseworkGrade(0, 3, "MATH101", 10, 19), // Assignment: 19/20 (95%)
                new CourseworkGrade(0, 3, "MATH101", 11, 29), // Mid-Exam: 29/30 (97%)
                new CourseworkGrade(0, 3, "MATH101", 12, 48)  // Final: 48/50 (96%)
        );

        for (CourseworkGrade grade : grades) {
            courseworkDetails.createCourseworkGrade(grade);
        }
    }
}