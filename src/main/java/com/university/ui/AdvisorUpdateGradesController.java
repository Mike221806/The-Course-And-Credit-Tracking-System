package com.university.ui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DoubleStringConverter;
import com.university.data.StudentDetails;
import com.university.data.CourseDetails;
import com.university.data.EnrollmentDetails;
import com.university.data.CourseworkDetails;
import com.university.data.StudentSqlDetails;
import com.university.data.CourseSqlDetails;
import com.university.data.EnrollmentSqlDetails;
import com.university.data.CourseworkSqlDetails;
import com.university.roles.Student;
import com.university.roles.Advisor;
import com.university.courses.Enrollment;
import com.university.courses.Course;
import com.university.courses.CourseworkGrade;
import com.university.courses.CourseworkItem;
import com.university.services.GPAService;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller for updating student grades.
 * Allows advisors to modify course and coursework grades.
 */
