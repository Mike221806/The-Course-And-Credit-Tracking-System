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
