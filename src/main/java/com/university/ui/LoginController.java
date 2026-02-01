package com.university.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import com.university.services.AuthService;
import com.university.data.StudentSqlDetails;
import com.university.data.AdvisorSqlDetails;
import com.university.roles.Student;
import com.university.roles.Advisor;

import java.io.IOException;
import java.util.Optional;

/**
 * Controller for the Login page.
 * Handles user authentication and navigation to appropriate dashboards.
 */