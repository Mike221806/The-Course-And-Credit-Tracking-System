package com.university.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.scene.Node;
import com.university.roles.Advisor;
import com.university.roles.Student;
import com.university.services.ReportService;
import com.university.services.GPAService;
import com.university.data.StudentDetails;
import com.university.data.CourseDetails;
import com.university.data.EnrollmentDetails;
import com.university.data.CourseworkDetails;
import com.university.data.StudentSqlDetails;
import com.university.data.CourseSqlDetails;
import com.university.data.EnrollmentSqlDetails;
import com.university.data.CourseworkSqlDetails;

import java.io.IOException;
import java.util.List;

/**
 * Controller for the Advisor Dashboard.
 * Provides navigation to different advisor functions and displays advisor information.
 */