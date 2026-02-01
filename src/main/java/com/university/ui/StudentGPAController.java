package com.university.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.GridPane;
import com.university.roles.Student;
import com.university.services.CourseService;
import com.university.services.GPAService;
import com.university.courses.Enrollment;
import com.university.data.EnrollmentSqlDetails;
import com.university.data.StudentSqlDetails;
import com.university.data.CourseSqlDetails;
import com.university.ui.components.CircularGPAIndicator;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

/**
 * Controller for the Student GPA view.
 * Displays GPA information and academic progress.
 */
