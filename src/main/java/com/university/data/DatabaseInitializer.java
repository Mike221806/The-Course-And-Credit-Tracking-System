package com.university.data;

import com.university.roles.Advisor;
import com.university.roles.Student;
import com.university.courses.*;
import com.university.services.PasswordUtil;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

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
                new Advisor(0, "Mr. Abel Tadesse", "Abel T", PasswordUtil.hashPassword("abel1234"), "Software Engineering", "abel.tadesse@university.edu"),
                new Advisor(0, "Prof. Michael Sheleme", "Michael S", PasswordUtil.hashPassword("michael1234"), "Computer Science", "michael.sheleme@university.edu"),
                new Advisor(0, "Dr. Sarah Johnson", "Sarah J", PasswordUtil.hashPassword("sarah1234"), "Information Technology", "sarah.johnson@university.edu"),
                new Advisor(0, "Prof. David Chen", "David C", PasswordUtil.hashPassword("david1234"), "Software Engineering", "david.chen@university.edu")
        );

        for (Advisor advisor : advisors) {
            advisorDetails.createAdvisor(advisor);
        }
    }

    /**
     * Creates sample students.
     */
    private void createSampleStudents() {
        // Check if students already exist
        try {
            List<Student> existingStudents = studentDetails.getAllStudents();
            if (!existingStudents.isEmpty()) {
                System.out.println("Students already exist, skipping sample student creation");
                return;
            }
        } catch (Exception e) {
            System.err.println("Error checking existing students: " + e.getMessage());
        }

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
        // Check if enrollments already exist for Alice (student ID 1)
        try {
            List<Enrollment> aliceEnrollments = enrollmentDetails.getEnrollmentsByStudentId(1);
            if (!aliceEnrollments.isEmpty()) {
                System.out.println("Enrollments already exist, skipping sample enrollment creation");
                return;
            }
        } catch (Exception e) {
            System.err.println("Error checking existing enrollments: " + e.getMessage());
        }

        // Wait for students to be created and get their IDs
        try {
            Thread.sleep(100); // Small delay to ensure students are created
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        List<Enrollment> enrollments = Arrays.asList(
                // Alice Smith enrollments (student ID 1) - Excellent student
                new Enrollment(0, 1, "CS101", "Introduction to Computer Science", 3, "Fall", 2023, "A+"),  // 90%+
                new Enrollment(0, 1, "CS201", "Data Structures", 4, "Fall", 2023, "A"),     // 85-89%
                new Enrollment(0, 1, "MATH101", "Calculus I", 4, "Fall", 2023, "A+"),    // 90%+
                new Enrollment(0, 1, "ENG101", "English Composition", 3, "Fall", 2023, null), // No grade yet

                // Bob Johnson enrollments (student ID 2) - Average student
                new Enrollment(0, 2, "CS101", "Introduction to Computer Science", 3, "Fall", 2023, "C+"), // 60-64%
                new Enrollment(0, 2, "ENG101", "English Composition", 3, "Fall", 2023, "B"),     // 70-74%
                new Enrollment(0, 2, "CS301", "Algorithms", 4, "Fall", 2023, null), // No grade yet

                // Carol Williams enrollments (student ID 3) - Excellent student
                new Enrollment(0, 3, "CS201", "Data Structures", 4, "Fall", 2023, "A+"),    // 90%+
                new Enrollment(0, 3, "CS301", "Algorithms", 4, "Fall", 2023, "B"),     // 70-74%
                new Enrollment(0, 3, "MATH101", "Calculus I", 4, "Fall", 2023, "A+"),    // 90%+
                new Enrollment(0, 3, "CS401", "Software Engineering", 3, "Fall", 2023, null), // No grade yet

                // Biruk A enrollments (student ID 4) - New student, no grades yet
                new Enrollment(0, 4, "CS101", "Introduction to Computer Science", 3, "Fall", 2023, null),
                new Enrollment(0, 4, "CS201", "Data Structures", 4, "Fall", 2023, null),
                new Enrollment(0, 4, "MATH101", "Calculus I", 4, "Fall", 2023, null),
                new Enrollment(0, 4, "ENG101", "English Composition", 3, "Fall", 2023, null)
        );

        for (Enrollment enrollment : enrollments) {
            enrollmentDetails.createEnrollment(enrollment);
        }
    }

    /**
     * Creates sample coursework grades with realistic marks for the new marking scheme.
     * This method now works with the actual coursework items created by CourseworkInitializer.
     */
    private void createSampleCourseworkGrades() {
        try {
            System.out.println("Creating sample coursework grades...");

            // Wait for coursework items to be created
            Thread.sleep(200);

            List<CourseworkGrade> grades = new ArrayList<>();

            // Alice Smith grades (ID: 1) - Excellent student
            createGradesForStudent(grades, 1, "CS101", 92.0);    // A+ grade (90%+)
            createGradesForStudent(grades, 1, "CS201", 87.0);    // A grade (85-89%)
            createGradesForStudent(grades, 1, "MATH101", 96.0);  // A+ grade (90%+)

            // Bob Johnson grades (ID: 2) - Average student
            createGradesForStudent(grades, 2, "CS101", 62.0);    // C+ grade (60-64%)
            createGradesForStudent(grades, 2, "ENG101", 72.0);    // B grade (70-74%)

            // Carol Williams grades (ID: 3) - Excellent student
            createGradesForStudent(grades, 3, "CS201", 91.0);    // A+ grade (90%+)
            createGradesForStudent(grades, 3, "CS301", 72.0);    // B grade (70-74%)
            createGradesForStudent(grades, 3, "MATH101", 94.0);  // A+ grade (90%+)

            // Biruk A grades (ID: 4) - New student, all zeros
            createGradesForStudent(grades, 4, "CS101", 0.0);
            createGradesForStudent(grades, 4, "CS201", 0.0);
            createGradesForStudent(grades, 4, "MATH101", 0.0);
            createGradesForStudent(grades, 4, "ENG101", 0.0);

            // Save all grades
            for (CourseworkGrade grade : grades) {
                courseworkDetails.createCourseworkGrade(grade);
            }

            System.out.println("Created " + grades.size() + " sample coursework grades.");

        } catch (Exception e) {
            System.err.println("Error creating sample coursework grades: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Helper method to create grades for a student for a specific course.
     */
    private void createGradesForStudent(List<CourseworkGrade> grades, int studentId,
                                        String courseCode, double targetPercentage) {

        try {
            // Get all coursework items for this course
            List<CourseworkItem> courseItems = courseworkDetails.getCourseworkItemsByCourse(courseCode);

            for (CourseworkItem item : courseItems) {
                double marks = (targetPercentage / 100.0) * item.getTotalMarks();

                CourseworkGrade grade = new CourseworkGrade();
                grade.setStudentId(studentId);
                grade.setItemId(item.getItemId());
                grade.setCourseCode(courseCode);
                grade.setMarksObtained(marks);

                grades.add(grade);
            }
        } catch (Exception e) {
            System.err.println("Error creating grades for student " + studentId + " in course " + courseCode + ": " + e.getMessage());
        }
    }
}