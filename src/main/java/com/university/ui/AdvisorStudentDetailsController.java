package com.university.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.university.roles.Advisor;
import com.university.data.StudentDetails;
import com.university.data.CourseDetails;
import com.university.data.EnrollmentDetails;
import com.university.data.StudentSqlDetails;
import com.university.data.CourseSqlDetails;
import com.university.data.EnrollmentSqlDetails;
import com.university.roles.Student;
import com.university.courses.Enrollment;
import com.university.courses.Course;

import java.util.List;

/**
 * Controller for viewing student details.
 * Shows comprehensive student academic information.
 */