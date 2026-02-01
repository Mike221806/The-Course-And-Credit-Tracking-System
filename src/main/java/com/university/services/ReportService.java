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
