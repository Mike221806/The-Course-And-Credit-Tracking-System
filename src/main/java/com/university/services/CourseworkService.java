package com.university.services;

import com.university.data.CourseworkDetails;
import com.university.data.CourseDetails;
import com.university.courses.CourseworkItem;
import com.university.courses.CourseworkGrade;
import com.university.courses.Assignment;
import com.university.courses.Exam;
import com.university.courses.MidtermExam;
import com.university.courses.FinalExam;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service for managing coursework items and grades.
 * Demonstrates Single Responsibility Principle (SRP) by focusing on coursework operations.
 * Follows Dependency Inversion Principle (DIP) by depending on interfaces.
 */
public class CourseworkService {

    private final CourseworkDetails courseworkDetails;
    private final CourseDetails courseDetails;

    public CourseworkService(CourseworkDetails courseworkDetails, CourseDetails courseDetails) {
        this.courseworkDetails = courseworkDetails;
        this.courseDetails = courseDetails;
    }

    /**
     * Creates a new coursework item.
     * @param courseworkItem the coursework item to create
     * @return true if successful, false otherwise
     */
    public boolean createCourseworkItem(CourseworkItem courseworkItem) {
        if (courseworkItem == null || courseworkItem.getTitle() == null || courseworkItem.getCourseCode() == null) {
            return false;
        }

        if (!courseDetails.courseExists(courseworkItem.getCourseCode())) {
            return false;
        }

        if (courseworkItem.getTotalMarks() <= 0 || courseworkItem.getWeight() < 0 || courseworkItem.getWeight() > 100) {
            return false;
        }

        return courseworkDetails.createCourseworkItem(courseworkItem);
    }

    /**
     * Retrieves a coursework item by ID.
     * @param itemId the item ID
     * @return Optional containing the coursework item if found, empty otherwise
     */
    public Optional<CourseworkItem> getCourseworkItemById(int itemId) {
        return courseworkDetails.getCourseworkItemById(itemId);
    }

    /**
     * Retrieves all coursework items for a course.
     * @param courseCode the course code
     * @return list of coursework items for the course
     */
    public List<CourseworkItem> getCourseworkItemsByCourse(String courseCode) {
        if (courseCode == null || courseCode.trim().isEmpty()) {
            return List.of();
        }

        return courseworkDetails.getCourseworkItemsByCourse(courseCode);
    }

    /**
     * Retrieves all coursework items of a specific type.
     * @param type the coursework type
     * @return list of coursework items of the specified type
     */
    public List<CourseworkItem> getCourseworkItemsByType(String type) {
        if (type == null || type.trim().isEmpty()) {
            return List.of();
        }

        return courseworkDetails.getCourseworkItemsByType(type);
    }

    /**
     * Updates an existing coursework item.
     * @param courseworkItem the coursework item to update
     * @return true if successful, false otherwise
     */
    public boolean updateCourseworkItem(CourseworkItem courseworkItem) {
        if (courseworkItem == null || courseworkItem.getItemId() <= 0) {
            return false;
        }

        Optional<CourseworkItem> existing = courseworkDetails.getCourseworkItemById(courseworkItem.getItemId());
        if (existing.isEmpty()) {
            return false;
        }

        return courseworkDetails.updateCourseworkItem(courseworkItem);
    }

    /**
     * Deletes a coursework item.
     * @param itemId the item ID to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteCourseworkItem(int itemId) {
        if (itemId <= 0) {
            return false;
        }

        return courseworkDetails.deleteCourseworkItem(itemId);
    }

    /**
     * Creates a new coursework grade.
     * @param courseworkGrade the coursework grade to create
     * @return true if successful, false otherwise
     */
    public boolean createCourseworkGrade(CourseworkGrade courseworkGrade) {
        if (courseworkGrade == null || courseworkGrade.getStudentId() <= 0 || courseworkGrade.getItemId() <= 0) {
            return false;
        }

        Optional<CourseworkItem> item = courseworkDetails.getCourseworkItemById(courseworkGrade.getItemId());
        if (item.isEmpty()) {
            return false;
        }

        if (!courseworkGrade.isValidMarks(item.get().getTotalMarks())) {
            return false;
        }

        return courseworkDetails.createCourseworkGrade(courseworkGrade);
    }

    /**
     * Retrieves coursework grades for a student.
     * @param studentId the student ID
     * @return list of coursework grades for the student
     */
    public List<CourseworkGrade> getCourseworkGradesByStudent(int studentId) {
        if (studentId <= 0) {
            return List.of();
        }

        return courseworkDetails.getCourseworkGradesByStudent(studentId);
    }

    /**
     * Retrieves coursework grades for a student in a specific course.
     * @param studentId the student ID
     * @param courseCode the course code
     * @return list of coursework grades for the student in the course
     */
    public List<CourseworkGrade> getCourseworkGradesByStudentAndCourse(int studentId, String courseCode) {
        if (studentId <= 0 || courseCode == null || courseCode.trim().isEmpty()) {
            return List.of();
        }

        return courseworkDetails.getCourseworkGradesByStudentAndCourse(studentId, courseCode);
    }

    /**
     * Updates an existing coursework grade.
     * @param courseworkGrade the coursework grade to update
     * @return true if successful, false otherwise
     */
    public boolean updateCourseworkGrade(CourseworkGrade courseworkGrade) {
        if (courseworkGrade == null || courseworkGrade.getGradeId() <= 0) {
            return false;
        }

        Optional<CourseworkItem> item = courseworkDetails.getCourseworkItemById(courseworkGrade.getItemId());
        if (item.isEmpty()) {
            return false;
        }

        if (!courseworkGrade.isValidMarks(item.get().getTotalMarks())) {
            return false;
        }

        return courseworkDetails.updateCourseworkGrade(courseworkGrade);
    }

    /**
     * Deletes a coursework grade.
     * @param gradeId the grade ID to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteCourseworkGrade(int gradeId) {
        if (gradeId <= 0) {
            return false;
        }

        return courseworkDetails.deleteCourseworkGrade(gradeId);
    }

    /**
     * Calculates the overall coursework grade for a student in a course.
     * @param studentId the student ID
     * @param courseCode the course code
     * @return the weighted average grade (0.0 to 100.0)
     */
    public double calculateOverallCourseworkGrade(int studentId, String courseCode) {
        List<CourseworkGrade> grades = getCourseworkGradesByStudentAndCourse(studentId, courseCode);
        List<CourseworkItem> items = getCourseworkItemsByCourse(courseCode);

        if (grades.isEmpty() || items.isEmpty()) {
            return 0.0;
        }

        double totalWeightedScore = 0.0;
        double totalWeight = 0.0;

        for (CourseworkGrade grade : grades) {
            Optional<CourseworkItem> item = items.stream()
                    .filter(i -> i.getItemId() == grade.getItemId())
                    .findFirst();

            if (item.isPresent()) {
                CourseworkItem courseworkItem = item.get();
                double percentage = grade.calculatePercentage(courseworkItem.getTotalMarks());
                totalWeightedScore += percentage * courseworkItem.getWeight();
                totalWeight += courseworkItem.getWeight();
            }
        }

        return totalWeight > 0 ? totalWeightedScore / totalWeight : 0.0;
    }

    /**
     * Creates standard coursework items for a course (assignment, mid-exam, final exam).
     * @param courseCode the course code
     * @return true if all coursework items were created successfully
     */
    public boolean createStandardCourseworkForCourse(String courseCode) {
        try {
            // Create Assignment (20 marks, 30% weight, due mid-semester)
            LocalDate assignmentDueDate = LocalDate.now().plusWeeks(8);
            Assignment assignment = new Assignment(
                    0, courseCode, "Course Assignment", 20.0, 0.30, assignmentDueDate
            );

            // Create Mid-Exam (30 marks, 30% weight, due mid-semester)
            LocalDate midExamDate = LocalDate.now().plusWeeks(7);
            MidtermExam midExam = new MidtermExam(
                    0, courseCode, "Mid-Term Examination", 30.0, 0.30, midExamDate
            );

            // Create Final Exam (50 marks, 40% weight, due end of semester)
            LocalDate finalExamDate = LocalDate.now().plusWeeks(14);
            FinalExam finalExam = new FinalExam(
                    0, courseCode, "Final Examination", 50.0, 0.40, finalExamDate
            );

            // Save all coursework items
            boolean assignmentSaved = courseworkDetails.createCourseworkItem(assignment);
            boolean midExamSaved = courseworkDetails.createCourseworkItem(midExam);
            boolean finalExamSaved = courseworkDetails.createCourseworkItem(finalExam);

            return assignmentSaved && midExamSaved && finalExamSaved;

        } catch (Exception e) {
            System.err.println("Error creating standard coursework for course " + courseCode + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Gets all coursework items for a specific course.
     * @param courseCode the course code
     * @return list of coursework items for the course
     */
    public List<CourseworkItem> getCourseworkByCourse(String courseCode) {
        return courseworkDetails.getCourseworkItemsByCourse(courseCode);
    }

    /**
     * Gets assignments only for a specific course.
     * @param courseCode the course code
     * @return list of assignments for the course
     */
    public List<Assignment> getAssignmentsByCourse(String courseCode) {
        return courseworkDetails.getCourseworkItemsByCourse(courseCode).stream()
                .filter(item -> item instanceof Assignment)
                .map(item -> (Assignment) item)
                .toList();
    }

    /**
     * Gets exams only for a specific course.
     * @param courseCode the course code
     * @return list of exams for the course
     */
    public List<Exam> getExamsByCourse(String courseCode) {
        return courseworkDetails.getCourseworkItemsByCourse(courseCode).stream()
                .filter(item -> item instanceof Exam)
                .map(item -> (Exam) item)
                .toList();
    }

    /**
     * Gets mid-exams only for a specific course.
     * @param courseCode the course code
     * @return list of mid-exams for the course
     */
    public List<Exam> getMidExamsByCourse(String courseCode) {
        return getExamsByCourse(courseCode).stream()
                .filter(exam -> "MID_EXAM".equals(exam.getExamType()))
                .toList();
    }

    /**
     * Gets final exams only for a specific course.
     * @param courseCode the course code
     * @return list of final exams for the course
     */
    public List<Exam> getFinalExamsByCourse(String courseCode) {
        return getExamsByCourse(courseCode).stream()
                .filter(exam -> "FINAL_EXAM".equals(exam.getExamType()))
                .toList();
    }

    /**
     * Adds a custom assignment to a course.
     * @param courseCode the course code
     * @param title the assignment title
     * @param totalMarks total marks for the assignment
     * @param weight weight of the assignment in overall grade
     * @param dueDate due date for the assignment
     * @return true if assignment was added successfully
     */
    public boolean addAssignmentToCourse(String courseCode, String title, double totalMarks, double weight, LocalDate dueDate) {
        Assignment assignment = new Assignment(0, courseCode, title, totalMarks, weight, dueDate);
        return courseworkDetails.createCourseworkItem(assignment);
    }

    /**
     * Adds a custom exam to a course.
     * @param courseCode the course code
     * @param title the exam title
     * @param totalMarks total marks for the exam
     * @param weight weight of the exam in overall grade
     * @param examDate date of the exam
     * @param examType type of exam (MID_EXAM, FINAL_EXAM, or other)
     * @return true if exam was added successfully
     */
    public boolean addExamToCourse(String courseCode, String title, double totalMarks, double weight, LocalDate examDate, String examType) {
        Exam exam;
        if ("MID_EXAM".equals(examType)) {
            exam = new MidtermExam(0, courseCode, title, totalMarks, weight, examDate);
        } else if ("FINAL_EXAM".equals(examType)) {
            exam = new FinalExam(0, courseCode, title, totalMarks, weight, examDate);
        } else {
            // Default to midterm for other types
            exam = new MidtermExam(0, courseCode, title, totalMarks, weight, examDate);
        }
        return courseworkDetails.createCourseworkItem(exam);
    }
}