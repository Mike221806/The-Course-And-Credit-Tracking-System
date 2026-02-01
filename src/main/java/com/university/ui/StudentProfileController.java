package com.university.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import com.university.roles.Student;
import com.university.services.GPAService;
import com.university.data.StudentSqlDetails;
import com.university.data.CourseSqlDetails;
import com.university.data.EnrollmentSqlDetails;

/**
 * Controller for the Student Profile view.
 * Displays student information and academic progress.
 */