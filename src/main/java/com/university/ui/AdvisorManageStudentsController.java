package com.university.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.university.roles.Advisor;
import com.university.roles.Student;
import com.university.data.StudentDetails;
import com.university.data.StudentSqlDetails;
import com.university.services.PasswordUtil;

import java.util.List;

/**
 * Controller for managing students.
 * Allows advisors to add new students with temporary passwords and view existing students.
 */