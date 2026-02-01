package com.university.data;

import com.university.services.CourseworkService;

import java.util.List;

/**
 * Utility class to initialize coursework items for courses.
 * Ensures each course has standard coursework items (assignment, mid-exam, final exam).
 */
public class CourseworkInitializer {

    private final CourseworkService courseworkService;
    private final CourseDetails courseDetails;

    public CourseworkInitializer() {
        this.courseworkService = new CourseworkService(new CourseworkSqlDetails(), new CourseSqlDetails());
        this.courseDetails = new CourseSqlDetails();
    }

    /**
     * Initializes standard coursework items for all courses in the system.
     * This method should be called after the database is initialized.
     */
    public void initializeCourseworkForAllCourses() {
        try {
            System.out.println("Initializing coursework for all courses...");

            List<String> courseCodes = courseDetails.getAllCourses().stream()
                    .map(course -> course.getCourseCode())
                    .toList();
            int successCount = 0;
            int totalCourses = courseCodes.size();

            for (String courseCode : courseCodes) {
                System.out.println("Processing course: " + courseCode);

                // Check if coursework already exists for this course
                List<com.university.courses.CourseworkItem> existingCoursework =
                        courseworkService.getCourseworkByCourse(courseCode);

                if (existingCoursework.isEmpty()) {
                    // Create standard coursework for the course
                    boolean success = courseworkService.createStandardCourseworkForCourse(courseCode);
                    if (success) {
                        System.out.println("✅ Created coursework for course: " + courseCode);
                        successCount++;
                    } else {
                        System.err.println("❌ Failed to create coursework for course: " + courseCode);
                    }
                } else {
                    System.out.println("⏭️  Coursework already exists for course: " + courseCode);
                    successCount++;
                }
            }

            System.out.println("Coursework initialization complete: " + successCount + "/" + totalCourses + " courses processed successfully.");

        } catch (Exception e) {
            System.err.println("Error initializing coursework: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Initializes coursework for a specific course.
     * @param courseCode the course code
     * @return true if initialization was successful
     */
    public boolean initializeCourseworkForCourse(String courseCode) {
        try {
            System.out.println("Initializing coursework for course: " + courseCode);

            // Check if coursework already exists
            List<com.university.courses.CourseworkItem> existingCoursework =
                    courseworkService.getCourseworkByCourse(courseCode);

            if (!existingCoursework.isEmpty()) {
                System.out.println("Coursework already exists for course: " + courseCode);
                return true;
            }

            // Create standard coursework
            boolean success = courseworkService.createStandardCourseworkForCourse(courseCode);
            if (success) {
                System.out.println("✅ Created coursework for course: " + courseCode);
            } else {
                System.err.println("❌ Failed to create coursework for course: " + courseCode);
            }

            return success;

        } catch (Exception e) {
            System.err.println("Error initializing coursework for course " + courseCode + ": " + e.getMessage());
            return false;
        }
    }
}