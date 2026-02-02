package com.university.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import com.university.roles.Student;
import com.university.data.AdvisorFeedbackDetails;
import com.university.data.AdvisorFeedbackSqlDetails;
import com.university.data.AdvisorDetails;
import com.university.data.AdvisorSqlDetails;
import com.university.data.AdvisorFeedback;
import com.university.roles.Advisor;

import java.util.List;
import java.util.Optional;

/**
 * Controller for displaying advisor feedback to students.
 * Shows all feedback that advisors have provided for the current student.
 */
