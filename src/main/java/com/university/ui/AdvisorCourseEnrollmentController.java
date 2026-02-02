package com.university.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.university.roles.Advisor;
import com.university.roles.Student;
import com.university.courses.Course;
import com.university.courses.Enrollment;
import com.university.data.StudentDetails;
import com.university.data.CourseDetails;
import com.university.data.EnrollmentDetails;
import com.university.data.StudentSqlDetails;
import com.university.data.CourseSqlDetails;
import com.university.data.EnrollmentSqlDetails;

import java.util.List;
import java.util.Optional;

/**
 * Controller for managing course enrollments.
 * Allows advisors to enroll students in courses and manage existing enrollments.
 */
