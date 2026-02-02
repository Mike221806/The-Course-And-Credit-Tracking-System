package com.university.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.university.roles.Student;
import com.university.services.CourseService;
import com.university.courses.Enrollment;
import com.university.data.CourseSqlDetails;
import com.university.data.EnrollmentSqlDetails;

import java.util.List;
import java.util.ArrayList;

/**
 * Controller for the Student Courses view.
 * Displays all courses the student is enrolled in with grades.
 */
