package com.university.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.university.roles.Student;
import com.university.services.CourseService;
import com.university.services.CourseworkService;
import com.university.courses.Enrollment;
import com.university.courses.CourseworkItem;
import com.university.courses.CourseworkGrade;
import com.university.courses.Assignment;
import com.university.courses.MidtermExam;
import com.university.courses.FinalExam;
import com.university.data.CourseSqlDetails;
import com.university.data.EnrollmentSqlDetails;
import com.university.data.CourseworkSqlDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for the Student Coursework view.
 * Displays coursework items and grades for selected courses.
 */
